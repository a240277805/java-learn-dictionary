# kafka

## kafka 集群结构
![avatar](https://mmbiz.qpic.cn/mmbiz_jpg/UdK9ByfMT2O97piaBAkbLvms7mTItc3GmyS0V0gNoPibpYUSMMgUoGCk68FibiaroWSibbus2QYevuKK3OCcgWRjeuw/640)

从图上可以看出
- Producer 是单独的多个
- 多了zookeeper 集群，用来收发消息
- kafka 的工作单元是 broker ，它会在 zk上注册
- 消费者可以有 组 group,group 内的消费者可以并行消费，但不会重复消费

### 为什么分区(Partition)能保证并行消费

![avatar](https://mmbiz.qpic.cn/mmbiz_jpg/UdK9ByfMT2O97piaBAkbLvms7mTItc3GmiabIvjWL8LzbwiaJIz90gBXyZ6G5X32ysNqFEHJbP9IyurwjhuH1smaA/640)
如果有两个分区，最多两个消费者同时消费，消费的速度肯定会更快。分区的设计大大的提升了kafka的吞吐量！！
![avatar](https://mmbiz.qpic.cn/mmbiz_jpg/UdK9ByfMT2O97piaBAkbLvms7mTItc3Gmqn6ZCnNM1NOZlxtcnuHQlKuGSAvrCPHxYcPBDTep0QRnLrrBwGtUlA/640)

从这个图可以看出什么？
- 一个partition只能被同组的一个consumer消费（图中只会有一个箭头指向一个partition）
- 同一个组里的一个consumer可以消费多个partition（图中第一个consumer消费Partition 0和3）
- 消费效率最高的情况是partition和consumer数量相同。这样确保每个consumer专职负责一个partition。
- consumer数量不能大于partition数量。由于第一点的限制，当consumer多于partition时，就会有consumer闲置。

5、consumer group可以认为是一个订阅者的集群，其中的每个consumer负责自己所消费的分区
### 副本怎么高可用
一个topic 下有多个分区 ，每个分区可以有多个副本 ，分区下都是副本 ，但只有一个leader 其他的是 follower，
工作流程:小心进来 先进入leader replica,然后从leader 复制到 follower。`只有复制完成时，consumer 才可以消费此消息`，这是为了确保意外发生，数据可以恢复

### "如果不同partition的leader replica在kafka集群的broker上分布不均匀，就会造成负载不均衡。" 怎么理解这句话？
先看下图
![avatar](https://mmbiz.qpic.cn/mmbiz_jpg/UdK9ByfMT2O97piaBAkbLvms7mTItc3Gm6DGaD0gL28vhqia3DXg7xqLMpvZ3VR94wWXesa33dkvdQsDmSoEiaBicg/640)
- broker 注册在  zk 上，是最小的工作单元
- 每个broker 上可以有多个 分区，
- 如果有多个分区，每个分区有一个leader replica ，在 broker上分布的越均匀，负载就越好。

### 怎么保证leader replica 均匀分布在broker 上呢？
kafka通过轮询算法保证leader replica是均匀分布在多个broker上。

- Replica均匀分配在Broker上，同一个partition的replica不会在同一个borker上
- 同一个partition的Replica数量不能多于broker数量。多个replica为了数据安全，一台server存多个replica没有意义。server挂掉，上面的副本都要挂掉。
- 分区的leader replica均衡分布在broker上。此时集群的负载是均衡的。这就叫做分区平衡

#### 怎么实现分区平衡？
- AR  最初的分配的副本，只会增加，不会变化
- PR (优先 replica) 在分区平衡时 可做参照，优先选择 leader 的 副本。一开始PR是和Leader replica 一致的，当leader 宕机，换了以后，一段时间再进行分区平衡时，就会以这个做参照。
- ISR 同步最新的副本情况。每个patition 都有自己的 ISR 

分区平衡步骤：
当一开始进行分区选举 是平衡的；过一段时间 leader 宕机，follower 当上了leader ，此时应该是不平衡的；一会原来的replica 恢复了，但此时只能做小弟；
kafka 会定时触发分区加平衡操作，也可以主动触发；触发后重新通过选举 ，然后 PR里的有优先选举权，之前的replica又恢复了leader ,平衡操作就是让leader副本归位。

### Partition的读和写
![avatar](https://mmbiz.qpic.cn/mmbiz_jpg/UdK9ByfMT2O97piaBAkbLvms7mTItc3Gmt3ptpu0egz49oLjYdMmaKicoZVIGD1P2Coq7I2PxSxPJNIX3StfRVGA/640)
- producer 采用round-robin算法 ，轮训往 partition里写入
- 每个 consumer 都维护了自己的 offset ，就是消费到了 patition什么位置，一个patition可以供多个consumer 消费，

### kafka 实现高吞吐的原理
- 读写文件依赖OS文件系统的页缓存，而不是在JVM内部缓存数据，利用OS来缓存，内存利用率高
- sendfile技术（零拷贝），避免了传统网络IO四步流程
- 支持End-to-End的压缩
- 顺序IO以及常量时间get、put消息
- Partition 可以很好的横向扩展和提供高并发处理

### kafka 怎么保证不重复消费
- 正常情况下 ,kafka 有 offset ，记录 patition 当前消费的位置
- 异常情况，offset 没更新宕机了，这时要在业务里加幂等性判断。

### kafka的负载均衡算法
```aidl
       1. A=(partition数量/同组内消费者总个数) 
       2. M=对上面所得到的A值小数点第一位向上取整 
       3. 计算出该消费者拉取数据的patition合集：Ci = [P(M*i )~P((i + 1) * M -1)]
```
       
### 参考：
[Kafka的Consumer负载均衡算法](https://www.codercto.com/a/29055.html)

[Kafka 高性能吞吐揭秘](https://segmentfault.com/a/1190000003985468)


