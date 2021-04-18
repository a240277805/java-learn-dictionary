# Mysql知识点
![avatar](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567764108237&di=f9e123ee1767a719e0cd665dc397a5b2&imgtype=0&src=http%3A%2F%2Faliyunzixunbucket.oss-cn-beijing.aliyuncs.com%2Fjpg%2F54444bc7077289d9330b6d63653afba0.jpg%3Fx-oss-process%3Dimage%2Fresize%2Cp_100%2Fauto-orient%2C1%2Fquality%2Cq_90%2Fformat%2Cjpg%2Fwatermark%2Cimage_eXVuY2VzaGk%3D%2Ct_100)
## 存储引擎
### innerDB

### 大表优化
- 限定数据的范围 （增删改查）
- 读写分离
- 垂直分区 优点:减少读取的Block数，减少I/O 次数。缺点：主键冗余 
- 水平分区 表的行数超过200万 就会变慢

### 一条SQL执行很慢 可能原因？

一个 SQL 执行的很慢，我们要分两种情况讨论：

1、大多数情况下很正常，偶尔很慢，则有如下原因

(1)、数据库在刷新脏页，例如 redo log 写满了需要同步到磁盘。

(2)、执行的时候，遇到锁，如表锁、行锁。

2、这条 SQL 语句一直执行的很慢，则有如下原因。

(1)、没有用上索引：例如该字段没有索引；由于对字段进行运算、函数操作导致无法用索引。

(2)、数据库选错了索引。

### 慢查询
慢查询日志：响应时间超过阈值的语句，默认时间10秒，默认不开，会影响性能，开启参数  slow_query_log=1

slow_query_log_file 这个表中记录


[腾讯面试：一条SQL语句执行得很慢的原因有哪些？---不看后悔系列](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485185&idx=1&sn=66ef08b4ab6af5757792223a83fc0d45&chksm=cea248caf9d5c1dc72ec8a281ec16aa3ec3e8066dbb252e27362438a26c33fbe842b0e0adf47&token=79317275&lang=zh_CN%23rd) 

### explain作用

查看sql的执行计划，帮助我们分析mysql是如何解析sql语句的。

- 查看表的加载顺序。
- 查看sql的查询类型。
- 哪些索引可能被使用，哪些索引又被实际使用了。
- 表之间的引用关系。
- 一个表中有多少行被优化器查询。
- 其他额外的辅助信息。

####id详解

含义：select查询的序列号，是一组数字，表示的是查询中执行select子句或者是操作表的顺序。

id的情况有三种，分别是：

- id相同表示加载表的顺序是从上到下。
- id不同id值越大，优先级越高，越先被执行。
- id有相同，也有不同，同时存在。id相同的可以认为是一组，从上往下顺序执行；在所有的组中，id的值越大，优先级越高，越先执行。

#### key:表示实际使用的索引
#### type
最好到最差的连接类型为const、eq_reg、ref、range、indexhe和ALL 
### mysql单表数据达到一定上限查询为啥会慢
- 假如MySQL服务器有足够的内存能将前三层索引缓存在内存中，索引只有三层，那么通过聚簇索引访问数据只需一次磁盘I/O。而当我们数据量过大，索引层级达到四层或四层以上时，通过聚簇索引访问就需要两次以上的磁盘I/O了。
- 当单表数据库到达某个量级的上限时，导致内存无法存储其索引，使得之后的 SQL 查询会产生磁盘 IO，从而导致性能下降。当然，这个还有具体的表结构的设计有关，最终导致的问题都是内存限制。这里，增加硬件配置，可能会带来立竿见影的性能提升哈。
### 主备复制,数据迁移

根据binlog

canal

## 参考：

[详解B+树及其正确打开方式](https://juejin.im/post/5d591c0a6fb9a06b1a5688e8?utm_source=gold_browser_extension)
[MySQL的索引](https://mp.weixin.qq.com/s?__biz=MzAxODcyNjEzNQ==&mid=2247486241&idx=1&sn=b9110c9d5be352f115c0d8cf6a0a520e&chksm=9bd0a6b9aca72faff0fe2f1ea1c3f43d6716f882bde357a999fe0094aa4e1f880f46473d1b98&scene=27#wechat_redirect)
[分库分表的一些思考](http://www.luyixian.cn/news_show_359744.aspx)
