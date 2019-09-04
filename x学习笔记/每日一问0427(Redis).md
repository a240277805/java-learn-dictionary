 <h1>redis</h1>
 1.redis  坑  
 JetCache 使用  坑

 配置： Jedis, 连接池5  
 
 
 问题描述：在使用JetCache 获取 key 的时候 报错 [com.alicp.jetcache.AbstractCache]jetcache(RedisCache) GET error. key=61. redis.clients.jedis.exceptions.JedisConnectionException:Unexpected end of stream.

 处理：
 * 进行错误排查 ，发现 该key 通过 redis-cli 是可以取到的 

 * 百度 查询异常 JedisConnectionException   Unexpected end of stream
    找到一篇较好的文章 ：https://blog.csdn.net/aubdiy/article/details/53511410

    得出，问题可能出在连接池数量不足， 接下来怎么解决这个问题 ，由于 JetCache 连接池 用的是 Jedis 连接池 ，
**优化方案1**
* 是优化连接池参数 ，所以 请看下面这这篇文章：https://www.cnblogs.com/benwu/articles/8616141.html
* maxTotal 资源池大小 ，这个得配置是一个很难回答的问题，考虑的因素有以下几点:
*  业务希望并发量 ，客户端执行命令时间 ，Redis资源：例如 nodes(例如应用个数) * maxTotal 是不能超过redis的最大连接数。   QPS（服务器在一秒的时间内处理了多少个请求 ）
*  一次命令时间（borrow|return resource + Jedis执行命令(含网络) ）的平均耗时约为1ms，一个连接的QPS大约是1000 ，业务期望的QPS是50000，那么理论上需要的资源池大小是50000 / 1000 = 50个

**优化方案2** 

替换 客户端为 	Lettuce（生菜）  
     Lettuce 相对Jedis 优势： https://gitbook.cn/gitchat/activity/5b286ab6328c342a9ff715c6?utm_source=csdn_blog  https://yq.aliyun.com/articles/584971</br>
      Lettuce 是一个可伸缩的线程安全的 Redis 客户端，支持同步、异步和响应式模式。多个线程可以共享一个连接实例，而不必担心多线程并发问题。它基于优秀 Netty NIO 框架构建，支持 Redis 的高级功能，如 Sentinel，集群，流水线，自动重新连接和 Redis 数据模型  一篇番外 redis 如何配置Lettuce  https://blog.csdn.net/winter_chen001/article/details/80614331</br>redisson,jedis,Lettuce 三者的区别 ：https://www.cnblogs.com/liyan492/p/9858548.html
