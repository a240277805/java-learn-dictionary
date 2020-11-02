# kubernetes

##  kubernates 架构

 ### 控制平面
 * API服务器
 * etcd 元数据存储 ： 为了保证冗余性，通常由三个或五个etcd实例组成一个集群。如果你丢失了etcd存储中的数据，那么你会丢掉整个集群。
 * 调度器  它实现了一种复杂的调度算法，该算法考虑到多个维度的信息，例如每个节点上的资源可用性、用户指定的各种约束、可用节点的类型、资源限制和配额以及其他因素，例如亲和性、反亲和性、容忍和污点等。
 * 控制器管理器 ： 这些控制器监控着集群事件和对集群的更改并做出响应。
 * 云控制器管理器

## kubernetes 基本概念和术语
Kubernetes中的大部分概念如Node、Pod、ReplicationController、Service等都可以被看作一种资源对象
几乎所有资源对象都可以通过Kubernetes提供的kubectl工具（或者API编程调用）执行增、删、改、查等操作并将其保存在etcd中持久化存储。

Kubernetes其实是一个高度自动化的资源控制系统，它通过跟踪对比etcd库里保存的“资源期望状态”与当前环境中的“实际资源状态”的差异来实现自动控制和自动纠错的高级功能。

## Kubernetes 资源对象
### Master
在每个Kubernetes集群里都需要有一个Master来负责整个集群的管理和控制，基本上Kubernetes的所有控制命令都发给它，它负责具体的执行过程

在Master上运行着以下关键进程。

* Kubernetes API Server（kube-apiserver）：提供了HTTP Rest接口的关键服务进程，是Kubernetes里所有资源的增、删、改、查等操作的唯一入口，也是集群控制的入口进程。
* Kubernetes Controller Manager（kube-controller-manager）：Kubernetes里所有资源对象的自动化控制中心，可以将其理解为资源对象的“大总管”。（控制啥）
* Kubernetes Scheduler（kube-scheduler）：负责资源调度（Pod调度）的进程，相当于公交公司的“调度室”。（调度啥）

另外，在Master上通常还需要部署etcd服务，因为Kubernetes里的所有资源对象的数据都被保存在etcd中。
### Node
Node可以是一台物理主机，也可以是一台虚拟机
Node是Kubernetes集群中的工作负载节点，每个Node都会被Master分配一些工作负载（Docker容器）
当某个Node宕机时，其上的工作负载会被Master自动转移到其他节点上。
在每个Node上都运行着以下关键进程。
* kubelet：负责Pod对应的容器的创建、启停等任务，同时与Master密切协作，实现集群管理的基本功能。
* kube-proxy：实现Kubernetes Service的通信与负载均衡机制的重要组件。
* Docker Engine（docker）：Docker引擎，负责本机的容器创建和管理工作。

在默认情况下kubelet会向`Master`注册自己，这也是Kubernetes推荐的Node管理方式。一旦Node被纳入集群管理范围，kubelet进程就会定时向Master汇报自身的情报，例如操作系统、Docker版本、机器的CPU和内存情况，以及当前有哪些Pod在运行等，这样Master就可以获知每个Node的资源使用情况，并实现`高效均衡的资源调度策略`。而某个Node在超过指定时间不上报信息时，会被Master判定为“失联”，Node的状态被标记为不可用（Not Ready），随后Master会触发“工作负载大转移”的自动流程。
### Pod
我们看到每个Pod都有一个特殊的被称为“根容器”的`Pause容器`

![avator](../imgSource/pod_img.png)
为什么Kubernetes会设计出一个全新的Pod的概念并且Pod有这样特殊的组成结构？
1. 判断死亡率 一杀多
2. 共享 IP ，Volume 解决通讯问题
每个Pod都分配了唯一的IP地址
Kubernetes要求底层网络支持集群内任意两个Pod之间的TCP/IP直接通信，这通常采用虚拟二层网络技术来实现，例如`Flannel、OpenvSwitch`等
在Kubernetes里，一个Pod里的容器与另外主机上的Pod容器能够直接通信。

Pod其实有两种类型：`普通的Pod`及`静态Pod（Static Pod）`。后者比较特殊，它并没被存放在Kubernetes的etcd存储里，而是被存放在某个具体的`Node上的一个具体文件中`，并且只在此Node上启动、运行

在默认情况下，当Pod里的某个容器停止时，Kubernetes会自动检测到这个问题并且重新启动这个Pod（重启Pod里的所有容器），如果Pod所在的Node宕机，就会将这个Node上的所有Pod重新调度到其他节点

可以用kubectl describe pod xxxx来查看它的描述信息，以定位问题的成因

可以对服务器资源设置限额
CPU（Core）设置数量 100~300m 即占用0.1-0.3个CPU
在Kubernetes里，一个计算资源进行配额限定时需要设定以下两个参数。
* Requests：该资源的最小申请量，系统必须满足要求
* Limits：该资源最大允许使用的量，不能被突破，当容器试图使用超过这个量的资源时，可能会被Kubernetes“杀掉”并重启。

![avator](../imgSource/pod_request_limit.png)

### Label 
一个Label是一个key=value的键值对，其中key与value由用户自己指定

Label可以被附加到各种资源对象上，例如Node、Pod、Service、RC等，一个资源对象可以定义任意数量的Label

我们可以通过给指定的资源对象捆绑一个或多个不同的Label来实现多维度的资源分组管理功能

以便灵活、方便地进行资源分配、调度、配置、部署等管理工作。例如，部署不同版本的应用到不同的环境中；监控和分析应用（日志记录、监控、告警）

例如：
* 版本标签："release" : "stable"、"release" : "canary"。
* 环境标签："environment":"dev"、"environment":"qa"、"environment":"production"。
* 分区标签："partition" : "customerA"、"partition" :"customerB"。
* 质量管控标签："track" : "daily"、"track" : "weekly"。

随后可以通过LabelSelector（标签选择器）查询和筛选拥有某些Label的资源对象，Kubernetes通过这种方式实现了类似SQL的简单又通用的对象查询机制。

`当前有两种Label Selector表达式`：基于等式的（Equality-based）和基于集合的（Set-based）

多个表达式之间用“，”进行分隔即可，几个条件之间是“AND”的关系
例如： name=redis-slave,env!=production

### Replication Controller (RC)
简单来说，它其实定义了一个期望的场景，即声明某种Pod的副本数量在任意时刻都符合某个预期值

所以 RC的定义包括如下：
* Pod期待的副本数量。
* 用于筛选目标Pod的Label Selector。
* 当Pod的副本数量小于预期数量时，用于创建新Pod的Pod模板（template）。

在我们定义了一个RC并将其提交到Kubernetes集群中后，Master上的Controller Manager组件就得到通知，定期巡检系统中当前存活的目标Pod，并确保目标Pod实例的数量刚好等于此RC的期望值，如果有过多的Pod副本在运行，系统就会停掉一些Pod，否则系统会再自动创建一些Pod。

删除RC并不会影响通过该RC已创建好的Pod。为了删除所有Pod，可以设置replicas的值为0，然后更新该RC。另外，kubectl提供了stop和delete命令来一次性删除RC和RC控制的全部Pod。

通过RC机制，Kubernetes很容易就实现了这种高级实用的特性，被称为“滚动升级”

同名冲突 所以 所以在Kubernetes 1.2中，升级为另外一个新概念——Replica Set

特性与作用：
在大多数情况下，我们通过定义一个RC实现Pod的创建及副本数量的自动控制。
*  在RC里包括完整的Pod定义模板。
* RC通过Label Selector机制实现对Pod副本的自动控制。
*  通过改变RC里的Pod副本数量，可以实现Pod的扩容或缩容。
* 通过改变RC里Pod模板中的镜像版本，可以实现Pod的滚动升级。


### Deployment 
Deployment是Kubernetes在1.2版本中引入的新概念，用于更好地解决Pod的编排问题
Deployment在内部使用了Replica Set来实现目的  与 RC相似度90%

Deployment相对于RC的一个最大升级是我们可以随时知道当前Pod“部署”的进度。实际上由于一个Pod的创建、调度、绑定节点及在目标Node上启动对应的容器这一完整过程需要一定的时间，所以我们期待系统启动N个Pod副本的目标状态，实际上是一个连续变化的“部署过程”导致的最终状态。

### Horizontal Pod Autoscaler (HPA)
通过手工执行kubectl scale命令，我们可以实现Pod扩容或缩容。HPA 可以实现自动扩容
![avator](../imgSource/pod_hpa.png)
当这些Pod副本的CPUUtilizationPercentage的值超过90%时会触发自动动态扩容行为，在扩容或缩容时必须满足的一个约束条件是Pod的副本数为1～10。
通过度量指标

#### 配置
apiVersion

Kubernetes平台采用了“`核心+外围扩展`”的设计思路
1. v1 核心API
大部分常见的核心资源对象都归属于v1这个核心API: 
* Node、
* Pod、
* Service、
* Endpoints、
* Namespace、
* RC、
* PersistentVolume等
2. 而在1.9版本之后引入了apps/v1这个正式的扩展API
### spec 
Kubernetes 1.8版本以后，Init container特性完全成熟，其定义被放入Pod的spec.initContainers一节

在Kubernetes 1.8中，资源对象中的很多Alpha、Beta版本的Annotations被取消，升级成了常规定义方式，在学习Kubernetes的过程中需要特别注意。

## 命令 
* 查看集群中有多少个Node: kubectl get nodes 
* 查看某个Node详细信息: kubectl describe node  node-1 (Node启动后会做一系列的自检工作，比如磁盘空间是否不足（DiskPressure）、内存是否不足（MemoryPressure）、网络是否正常（NetworkUnavailable）、PID资源是否充足（PIDPressure）。在一切正常时设置Node为Ready状态（Ready=True），该状态表示Node处于健康状态，Master将可以在其上调度新的任务了（如启动Pod）。;Node 主机地址；Node 主机系统信息；Node可分配资源量；Node相关Event 信息)
* 创建Deployment ： kubectl create -f  demo-deployment.yaml
* 查看 deployment 信息 ： kubectl get deployments
* 查看deployment 对应的 Replica Set : kubectl get rs