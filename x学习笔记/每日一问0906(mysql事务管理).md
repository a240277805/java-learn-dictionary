# mysql 事务管理
##事务的实现方式

数据库事务的实现方式主要有两种：
* 基于`锁`的；
* 基于时间戳的，现在主流的实现就是基于时间戳的方式的一种，就是大家熟悉的`MVCC`机制；

## ACID是衡量事务的四个特性：
* 原子性：（Atomicity）
* 一致性：（Consistency）
* 隔离性：（Isolation）
* 持久性：（Durability）
## mysql 日志
MySQL的日志有很多种，如二进制日志、错误日志、查询日志、慢查询日志等，此外InnoDB存储引擎还提供了两种事务日志：redo log(重做日志)和undo log(回滚日志)
* binlog 
* Redo log
* Undo log
* MVCC

## 原理
 undo log。实现原子性的关键，当发生回滚时，InnoDB会根据undo log的内容做与之前相反的工作
## 问题

### ACID问题：

* 为什么InnoDB能够保证原子性？用的什么方式？
* 为什么InnoDB能够保证一致性？用的什么方式？
* 为什么InnoDB能够保证持久性？用的什么方式？

### 隔离性里隔离级别的问题：

* 为什么RU级别会发生脏读，而其他的隔离级别能够避免？
* 为什么RC级别不能重复读，而RR级别能够避免？
* 为什么InnoDB的RR级别能够防止幻读？


## 参考
* [一篇文章带你学习分布式事务](https://www.infoq.cn/article/g1avP9FUA6CDOYRAlv4R)
* [讲清楚分布式事务选型：XA、2PC、TCC、Saga、阿里Seata](https://mp.weixin.qq.com/s?__biz=MzIxMzEzMjM5NQ==&mid=2651033136&idx=1&sn=9a91289141bf24bf242ada7e676f0ddb&chksm=8c4c5b34bb3bd2227d025f2c9f1043ed07594bd08072f41336fb591e479e444c3ffba5bf8582&scene=27#wechat_redirect)
* [深入分析MySQL InnoDB的事务ACID特性](https://mp.weixin.qq.com/s?__biz=MzIwMzY1OTU1NQ==&mid=2247484137&idx=1&sn=f79302b061418771fc413c4b19a6218e&chksm=96cd42a5a1bacbb3dc9f2b6cc923b6a1fb021e467b8c4726078b499cef576b80c0dd86a1c131&scene=27#wechat_redirect)
* [MySQL 乱七八糟的可重复读隔离级别实现](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247484915&idx=2&sn=a4c247a6bde0b3897be871a9706f3f1c&chksm=fa497a42cd3ef3541743cd9c835bf8a7a2100d0a0ab38ad63a4f943077f375e5bff75a937af9&scene=27#wechat_redirect)
* [一文了解InnoDB事务实现原理](https://zhuanlan.zhihu.com/p/48327345)
