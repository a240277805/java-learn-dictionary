# mysql 锁
## 锁的类型 

![avatar](https://img-blog.csdn.net/20180902191802677?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29xa2R3cw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
分类图:
![avatar](https://pic2.zhimg.com/80/v2-eec522a8cf7d8a38eaea29192edbb2f5_hd.jpg)

![avatar](https://pic3.zhimg.com/80/v2-5cf8b96fdca1428e6f3cce863fdfa73e_hd.jpg)

![avatar](https://www.javazhiyin.com/wp-content/uploads/2019/01/java1-1547261661.jpeg)
### Shared（乐观锁/共享锁/读锁/S锁)
行锁

若事务T对数据对象A加上S锁，则其他事务只能再对A加S锁，而不能X锁，直到T释放A上的锁。这就保证了其他事务可以读A，但在T释放A上的S锁之前不能对A做任何修改。

###  Exclusive Locks（互斥锁/排他锁/X锁/写锁）
行锁

若事务T对数据对象A加上X锁，则只允许T读取和修改A，其他任何事务都不能再对加任何类型的锁，知道T释放A上的锁。这就保证了其他事务在T释放A上的锁之前不能再读取和修改A。
### 自增锁
> 自增锁是一种特殊的表级别锁（table-level lock），专门针对事务插入AUTO_INCREMENT类型的列。最简单的情况，如果一个事务正在往表中插入记录，所有其他事务的插入必须等待，以便第一个事务插入的行，是连续的主键值。

## 死锁


### 什么是FullText index??
### 怎么出现死锁？？
### 怎么解决死锁？？

并发的问题就少不了死锁，在MySQL中同样会存在死锁的问题。

但一般来说MySQL通过回滚帮我们解决了不少死锁的问题了，但死锁是无法完全避免的，可以通过以下的经验参考，来尽可能少遇到死锁：

1）以固定的顺序访问表和行。比如对两个job批量更新的情形，简单方法是对id列表先排序，后执行，这样就避免了交叉等待锁的情形；将两个事务的sql顺序调整为一致，也能避免死锁。

2）大事务拆小。大事务更倾向于死锁，如果业务允许，将大事务拆小。

3）在同一个事务中，尽可能做到一次锁定所需要的所有资源，减少死锁概率。

4）降低隔离级别。如果业务允许，将隔离级别调低也是较好的选择，比如将隔离级别从RR调整为RC，可以避免掉很多因为gap锁造成的死锁。

5）为表添加合理的索引。可以看到如果不走索引将会为表的每一行记录添加上锁，死锁的概率大大增大。

## 什么是 两阶段锁
### 行锁为什么需要索引？
### 行锁与聚簇索引的关系

### 各种锁在事务中的应用？？


### 为什么innodb 支持行锁，MyISAM 不行
我的理解: 因为innoDB 有一个聚簇索引(索引上有行内容),然后加锁时需要定位相关行然后进行操作，聚簇索引可以快速定位要加锁的行的内容，另外就是双向链表的插入操作比较快，定位位置之后，可以直接对该行内容进行处理,1）减少查找行的性能开销。 2） 行锁 是建立在索引上的，MyISAM 中 的索引都是等价的，不知道用哪个(可能会死锁)。
### 行锁为什么会出现死锁 表锁不会？
## 参考
* `**`[InnoDB使用的七种锁](https://blog.csdn.net/oqkdws/article/details/82318157)
* [mysql锁原理](https://blog.csdn.net/linuxheik/article/details/68067042)
* `***`[MySQL优化系列（八）--锁机制超详细解析（锁分类、事务并发、引擎并发控制）](https://blog.csdn.net/jack__frost/article/details/73347688)
* `*****`[mysql 行锁的实现](https://lanjingling.github.io/2015/10/10/mysql-hangsuo/)
* `*****`[读《MySQL 实战》03 锁和性能](http://www.linkedkeeper.com/1332.html)
