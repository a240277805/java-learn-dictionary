Java 测试项目 

# 动态
2021/07/27
1. 配置迁移 nacos

* mysql-dev.yaml
```yaml

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    password: mysql@2020
    url: jdbc:mysql://172.20.60.23:3306/devops?useUnicode=true&characterEncoding=utf-8&useSSL=false&useTimezone=true&serverTimezone=GMT%2B8
    # spring 数据源设
    username: root
```
* redis-dev.yaml
```yaml

#jetcache 相关
jetcache:
  statIntervalMinutes: 15   # 每隔多久统计信息的时长配置
  areaInCacheName: false   #是否配置前缀
  local:
    default: #默认area
      type: caffeine   #本地缓存类型
      keyConvertor: fastjson   #key的序列化转化的协议
      limit: 100              #本地缓存最大个数
      defaultExpireInMillis: 10000   #缓存的时间全局 默认值
  remote:
    default:
      type: redis.lettuce          #缓存数据库类型
      keyConvertor: fastjson
      uri:
        - redis://redis%402020@172.20.60.23:6379/  #这里支持集群配置
      valueDecoder: java
      valueEncoder: java
      poolConfig:
        maxIdle: 20
        maxTotal: 50
        minIdle: 5
      # 前缀
      keyPrefix: 'devops:devplatform:'
---
spring:
  # redis 相关
  redis:
    # Redis默认情况下有16个分片，这里配置具体使用的分片
    database: 0
    host: 172.20.60.23
    lettuce:
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 8
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: 1
        # 连接池中的最小空闲连接
        min-idle: 0
    password: redis@2020
    port: 6379
    # 连接超时时间（毫秒）
    timeout: 3000
---
# redisson lock
redisson:
  address: redis://172.20.60.23:6379
  password: redis@2020
```
kafka-dev.yaml
```yaml
#kafka相关
spring:
  kafka:
    bootstrap-servers: 172.20.60.23:9092
    producer:
      acks: 1
      batch-size: 16384
      retries: 0
      buffer-memory: 33554432
    enable:
      # 自动生产消息幂等性
      idempotence: true
    consumer:
      enable-auto-commit: true
      #      group-id: devops_local_wld
      auto-commit-interval: 10

---
kafka:
  constants: 
    gitlab_system_webhook_group: DEV_GITLAB_SYSTEM_WEBHOOK_GROUP
    devops_platform_cache_group: DEV_DEVOPS_PLATFORM_CACHE_GROUP
    gitlab_system_webhook_topic: DEV_GITLAB_SYSTEM_WEBHOOK_TOPIC
    devops_platform_event_topic: DEV_DEVOPS_PLATFORM_EVENT_TOPIC
```
* mongo-dev.yaml
```yaml
# mongo
spring:
  data:
    mongodb:
      uri: mongodb://ctfo:pipelineCTFO!2020@devops-mongodb.devops.svc.cluster.local/pipeline
```
* es-dev.yaml
```yaml
---
spring:
  data:
    es:
      # es url
      url: http://172.20.60.23:9200
      userName: 1
      password: 1
```
* java-test.yaml
```yaml
# server:
#   servlet:
#     context-path: /java-test
server:
  # server 端口
  port: 8181
---
# 日志文件名称
logback-config:
  logName: ${HOSTNAME:logName_undefined}
---
spring:
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  # serverlet 相关
  servlet:
    multipart:
      max-file-size: 101MB
      max-request-size: 101MB
---
# 允许/actuator/bus-refresh接口被外部调用
management:
  endpoint:
    health:
      # health/detail 细节（）
      show-details: always
    shutdown:
      #启用shutdown端点
      enabled: true
  endpoints:
    web:
      exposure:
        # 开放监控内容
        include: "*"
mapper:
  enumAsSimpleType: true
---
# mybatis 配置
mybatis:
  #  # 加载全局的配置文件
  config_location: classpath:mybatis/mybatis-config.xml
  mapper-locations: classpath:mapper/*Mapper.xml
  type-aliases-package: com.*.DO
---
# pagehelper 配置
pagehelper:
  helper-dialect: mysql
---
# swagger 配置
swagger:
  enabled: true
---
# logback 配置
logging:
  config: classpath:logs/logback-spring.xml
logback-config:
  #该日志路径为绝对路径
  logHomeDir: /devops/logs
```
