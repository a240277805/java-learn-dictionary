# mysql 命令 
## mysql 软件相关
* 查看mysql 安装路径 ps -ef|grep mysql 
* 连接:mysql  -h  主机名(ip)  -u  用户名 -P 端口 -p 
* 查看Mysql服务器上的版本  select version();
## 库/表操作
* 查看所有的数据库 show databases; ，
* 查看当前所在数据库  select database(); 
* 查看某表 show create table 表名;
或 show table status from db_name where name='table_name';
* 表锁定 show status like '%table_lock%';
* 行锁定 show status like 'innodb_row_lock%';
## 缓存相关
* 查询缓存情况 show status like '%qcache%'; 
* show variables like "%query_cache%";
## 优化操作

## 监控操作
* 查询数据库连接:show full  processlist; 
* show status like '%Max_used_connections%';
* 当前连接数: show status like '%Threads_connected%';
* SHOW STATUS LIKE 'Qcache%';

* 由于客户没有正确关闭连接已经死掉，已经放弃的连接数量:show status like 'Aborted_clients';
* 查看最大连接数量:show variables like '%max_connections%';
* 查看超时时间:show variables like '%timeout%';

## 引擎操作
* 查看存储引擎  show engines;

## binlog日志相关


* 查看日志是否启动:show variables like 'log_%'; 
* 查看所有binlog日志列表:　show master logs;
* 查看master状态，即最后（最新）一个binlog日志的编号名称，及其最后一个操作事件pos结束点(Position)值: 　show master status;
* flush 刷新log日志，自此刻开始产生一个新编号的binlog日志文件: 　flush logs;
* 重置（清空）所有binlog日志 reset master;
* 查看binlog日志内容(1) (命令外部): mysqlbinlog+binlog文件名
* `查看binlog日志内容(2)`:show binlog events [IN 'log_name'] [FROM pos] [LIMIT [offset,] row_count]; eg:show binlog events in 'mysql-bin.000002' from 624\G;
* show variables like "%binlog%";
* 查看 binlog 内容 show binlog events;
* 查看具体一个binlog文件的内容 （in 后面为binlog的文件名）show binlog events in 'master.000003';
* 设置binlog文件保存事件，过期删除，单位天 set global expire_log_days=3; 
* 删除当前的binlog文件: reset master; 
* 删除slave的中继日志: reset slave;
* 删除指定日期前的日志索引中binlog日志文件:purge master logs before '2019-03-09 14:00:00';
* 删除指定日志文件: purge master logs to 'master.000003';













