

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

## Kafka中的消息是否会丢失和重复消费：

###   消息是否会重复消费：

   Kafka消息消费有两个consumer接口，Low-level API和High-level API：

> *Low-level API：消费者自己维护offset等值，可以实现对Kafka的完全控制； ***\*不存在消息丢失\*****
>
> *High-level API：封装了对parition和offset的管理，使用简单；           ***\*会存在消息丢失\*****

​    如果使用***\*高级接口High-level API\****，可能存在一个问题就是当消息消费者从集群中把消息取出来、并提交了新的消息offset值后，还没来得及消费就挂掉了，那么下次再消费时之前没消费成功的消息就“*诡异*”的消失了； 

### kafka的负载均衡算法
```aidl
       1. A=(partition数量/同组内消费者总个数) 
       2. M=对上面所得到的A值小数点第一位向上取整 
       3. 计算出该消费者拉取数据的patition合集：Ci = [P(M*i )~P((i + 1) * M -1)]
```

### *** offset（位移管理）\****

​    老版本的位移是提交到zookeeper中的，但是zookeeper其实并不适合进行大批量的读写操作，尤其是写操作。因此kafka提供了另一种解决方案：增加__consumeroffsets topic，将offset信息写入这个topic，摆脱对zookeeper的依赖(指保存offset这件事情)。__consumer_offsets中的消息保存了每个consumer group某一时刻提交的offset信息。

 ***\*早期版本的 kafka 用 zk 做 meta 信息存储\****，consumer 的消费状态，group 的管理以及 offse t的值。考虑到zk本身的一些因素以及整个架构较大概率存在单点问题，***\*新版本中确实逐渐弱化了zookeeper的作用\****。新的consumer使用了kafka内部的group coordination协议，也减少了对zookeeper的依赖。

###  ***\*（7）Zookeeper 在 Kafka 中的作用\****

   kafaka集群的 broker，和 Consumer 都需要连接 Zookeeper。Producer 直接连接 Broker，Topic 分区被放在不同的 Broker 中，保证 Producer 和 Consumer 错开访问 Broker，避免访问单个 Broker造成过度的IO压力，使得负载均衡。

### partition是如何与group对应的

原理:Zookeerper中保存这每个topic下的每个partition在每个group中消费的offset 。***\*新版kafka把这个offsert保存到了一个__consumer_offsert的topic下\**** ，这个__consumer_offsert 有50个分区，通过将***\*group的id哈希值%50的值来确定要保存到那一个分区\****。

```shell
计算方法：（放到哪个分区）
int hashCode=Math.abs("${group_name}".hashCode())
int partition= hashCode%50
先计算group的hashCode ,再取余50，可以得到 partition的值
```

## 生产者的负载均衡

 使用Zookeeper进行负载均衡，由于***\*每个Broker启动\****时，都会完成***\*Broker注册过程\****，生产者会通过该***\*节点的变化来动态地感知到Broker服务器列表的变更\****，这样就可以实现***\*动态的负载均衡机制\****。

### 消费者负载均衡

与生产者类似，Kafka中的消费者同样需要进行负载均衡来实现多个消费者合理地从对应的Broker服务器上接收消息，每个消费者分组包含若干消费者，**每条消息都只会发送给分组中的一个消费者**，不同的消费者分组消费自己特定的Topic下面的消息，互不干扰。

## 在集群负载均衡方面

​     kafka采用zookeeper对集群中的broker、consumer进行管理，可以***\*注册topic到zookeeper上\****；通过***\*zookeeper的协调机制\****，producer保存对应topic的broker信息，可以随机或者轮询发送到broker上；并且producer可以基于语义指定分片，消息发送到broker的某分片上。

### 交互过程



1. Partition的多个replica中一个为Leader，其余为follower
2. Producer只与Leader交互，把数据写入到Leader中
3. Followers从Leader中拉取数据进行数据同步
4. Consumer只从Leader拉取数据

### kafka 幂等性 

参考 **Kafka 事务性之幂等性实现**

## 不利的地方

- 重复消息。Kafka 只保证每个消息至少会送达一次，虽然几率很小，但一条消息有可能会被送达多次。 
- 消息乱序。虽然一个Partition 内部的消息是保证有序的，但是如果一个Topic 有多个Partition，Partition 之间的消息送达不保证有序。 
- 复杂性。Kafka需要zookeeper 集群的支持，Topic通常需要人工来创建，部署和维护较一般消息队列成本更高

## 部署

```shell
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper:latest
    hostname: zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    #定义挂载点         
    volumes:
      - /etc/localtime:/etc/localtime
  kafka:
    image: wurstmeister/kafka:2.11-0.11.0.3
    hostname: kafka
    container_name: kafka
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_ADVERTISED_HOST_NAME: "172.20.60.23"
      KAFKA_ADVERTISED_PORT: "9092"
    #定义挂载点         
    volumes:
      - /etc/localtime:/etc/localtime
```



```shell
//创建topic
bin/kafka-topics.sh --create --zookeeper 172.20.60.23:2181 --replication-factor 1 --partitions 1 --topic mykafka

//查看topic
bin/kafka-topics.sh --list --zookeeper 172.20.60.23:2181

//创建生产者
bin/kafka-console-producer.sh --broker-list 172.20.60.23:9092 --topic mykafka 

//创建消费者
kafka-console-consumer.sh --bootstrap-server 172.20.60.23:9092 --topic DEVOPS_PLATFORM_EVENT_TOPIC --from-beginning
```



### confluent kafka 安装

有问题 待修复

部署参考   Quick Start for Apache Kafka using Confluent Platform (Docker)

```shell
# Start Zookeeper and expose port 2181 for use by the host machine
docker run -d --name zookeeper -p 2181:2181 confluent/zookeeper

# Start Kafka and expose port 9092 for use by the host machine
docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper confluent/kafka

# Start Schema Registry and expose port 8081 for use by the host machine
docker run -d --name schema-registry -p 8081:8081 --link zookeeper:zookeeper \
    --link kafka:kafka confluent/schema-registry

# Start REST Proxy and expose port 8082 for use by the host machine
docker run -d --name rest-proxy -p 8082:8082 --link zookeeper:zookeeper \
    --link kafka:kafka --link schema-registry:schema-registry confluent/rest-proxy
```





## Spring-kafka 配置

```yaml
#################consumer的配置参数（开始）#################
#如果'enable.auto.commit'为true，则消费者偏移自动提交给Kafka的频率（以毫秒为单位），默认值为5000。
spring.kafka.consumer.auto-commit-interval;
 
#当Kafka中没有初始偏移量或者服务器上不再存在当前偏移量时该怎么办，默认值为latest，表示自动将偏移重置为最新的偏移量
#可选的值为latest, earliest, none
spring.kafka.consumer.auto-offset-reset=latest;
 
#以逗号分隔的主机：端口对列表，用于建立与Kafka群集的初始连接。
spring.kafka.consumer.bootstrap-servers;
 
#ID在发出请求时传递给服务器;用于服务器端日志记录。
spring.kafka.consumer.client-id;
 
#如果为true，则消费者的偏移量将在后台定期提交，默认值为true
spring.kafka.consumer.enable-auto-commit=true;
 
#如果没有足够的数据立即满足“fetch.min.bytes”给出的要求，服务器在回答获取请求之前将阻塞的最长时间（以毫秒为单位）
#默认值为500
spring.kafka.consumer.fetch-max-wait;
 
#服务器应以字节为单位返回获取请求的最小数据量，默认值为1，对应的kafka的参数为fetch.min.bytes。
spring.kafka.consumer.fetch-min-size;
 
#用于标识此使用者所属的使用者组的唯一字符串。
spring.kafka.consumer.group-id;
 
#心跳与消费者协调员之间的预期时间（以毫秒为单位），默认值为3000
spring.kafka.consumer.heartbeat-interval;
 
#密钥的反序列化器类，实现类实现了接口org.apache.kafka.common.serialization.Deserializer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
 
#值的反序列化器类，实现类实现了接口org.apache.kafka.common.serialization.Deserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
 
#一次调用poll()操作时返回的最大记录数，默认值为500
spring.kafka.consumer.max-poll-records;
#################consumer的配置参数（结束）#################
#################producer的配置参数（开始）#################
#procedure要求leader在考虑完成请求之前收到的确认数，用于控制发送记录在服务端的持久化，其值可以为如下：
#acks = 0 如果设置为零，则生产者将不会等待来自服务器的任何确认，该记录将立即添加到套接字缓冲区并视为已发送。在这种情况下，无法保证服务器已收到记录，并且重试配置将不会生效（因为客户端通常不会知道任何故障），为每条记录返回的偏移量始终设置为-1。
#acks = 1 这意味着leader会将记录写入其本地日志，但无需等待所有副本服务器的完全确认即可做出回应，在这种情况下，如果leader在确认记录后立即失败，但在将数据复制到所有的副本服务器之前，则记录将会丢失。
#acks = all 这意味着leader将等待完整的同步副本集以确认记录，这保证了只要至少一个同步副本服务器仍然存活，记录就不会丢失，这是最强有力的保证，这相当于acks = -1的设置。
#可以设置的值为：all, -1, 0, 1
spring.kafka.producer.acks=1
 
#每当多个记录被发送到同一分区时，生产者将尝试将记录一起批量处理为更少的请求， 
#这有助于提升客户端和服务器上的性能，此配置控制默认批量大小（以字节为单位），默认值为16384
spring.kafka.producer.batch-size=16384
 
#以逗号分隔的主机：端口对列表，用于建立与Kafka群集的初始连接
spring.kafka.producer.bootstrap-servers
 
#生产者可用于缓冲等待发送到服务器的记录的内存总字节数，默认值为33554432
spring.kafka.producer.buffer-memory=33554432
 
#ID在发出请求时传递给服务器，用于服务器端日志记录
spring.kafka.producer.client-id
 
#生产者生成的所有数据的压缩类型，此配置接受标准压缩编解码器（'gzip'，'snappy'，'lz4'），
#它还接受'uncompressed'以及'producer'，分别表示没有压缩以及保留生产者设置的原始压缩编解码器，
#默认值为producer
spring.kafka.producer.compression-type=producer
 
#key的Serializer类，实现类实现了接口org.apache.kafka.common.serialization.Serializer
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
 
#值的Serializer类，实现类实现了接口org.apache.kafka.common.serialization.Serializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
 
#如果该值大于零时，表示启用重试失败的发送次数
spring.kafka.producer.retries
#################producer的配置参数（结束）#################
#################listener的配置参数（结束）#################
#侦听器的AckMode,参见https://docs.spring.io/spring-kafka/reference/htmlsingle/#committing-offsets
#当enable.auto.commit的值设置为false时，该值会生效；为true时不会生效
spring.kafka.listener.ack-mode;
 
#在侦听器容器中运行的线程数
spring.kafka.listener.concurrency;
 
#轮询消费者时使用的超时（以毫秒为单位）
spring.kafka.listener.poll-timeout;
 
#当ackMode为“COUNT”或“COUNT_TIME”时，偏移提交之间的记录数
spring.kafka.listener.ack-count;
 
#当ackMode为“TIME”或“COUNT_TIME”时，偏移提交之间的时间（以毫秒为单位）
spring.kafka.listener.ack-time;
#################listener的配置参数（结束）#################
```



### 参考：

[Kafka的Consumer负载均衡算法](https://www.codercto.com/a/29055.html)

[Kafka 高性能吞吐揭秘](https://segmentfault.com/a/1190000003985468)

[kafka官方文档](https://kafka.apachecn.org/)

# [docker 安装kafka](https://www.cnblogs.com/hunternet/p/11017000.html)

 [Spring Kafka中关于Kafka的配置参数 ](https://blog.csdn.net/fenglibing/article/details/82117166)

 [关于消息队列Kafka的一些常见问题](https://blog.csdn.net/qq_35571554/article/details/82593159)

[Kafka 事务性之幂等性实现](http://matt33.com/2018/10/24/kafka-idempotent/)

[Quick Start for Apache Kafka using Confluent Platform (Docker)](https://docs.confluent.io/platform/current/quickstart/ce-docker-quickstart.html)

