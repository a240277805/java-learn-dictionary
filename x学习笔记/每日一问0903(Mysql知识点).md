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
 
[腾讯面试：一条SQL语句执行得很慢的原因有哪些？---不看后悔系列](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485185&idx=1&sn=66ef08b4ab6af5757792223a83fc0d45&chksm=cea248caf9d5c1dc72ec8a281ec16aa3ec3e8066dbb252e27362438a26c33fbe842b0e0adf47&token=79317275&lang=zh_CN%23rd) 

## 参考：

[详解B+树及其正确打开方式](https://juejin.im/post/5d591c0a6fb9a06b1a5688e8?utm_source=gold_browser_extension)
[MySQL的索引](https://mp.weixin.qq.com/s?__biz=MzAxODcyNjEzNQ==&mid=2247486241&idx=1&sn=b9110c9d5be352f115c0d8cf6a0a520e&chksm=9bd0a6b9aca72faff0fe2f1ea1c3f43d6716f882bde357a999fe0094aa4e1f880f46473d1b98&scene=27#wechat_redirect)
