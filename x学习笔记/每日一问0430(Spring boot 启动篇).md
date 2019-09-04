# Spring boot 启动篇
## WHY
 为啥要写: 因为Spring boot 启动太慢了 ，想测试一个东西 可能要等他启动很久，然后看到JetCache 他的测试就很快 向大神看齐
## HOW

* 首先要了解他启动都干了啥
* 排除没必要的 东西 去繁就简
* 看大神是怎么实现的
## WHAT

常用的几种启动方式:
* SpringBootApplication  SpringBootApplication.run(*.class,args);
* > new SpringBootApplication(*.class) then a.run(args);
* 方式三
 ```
 public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(Parent.class)
        .child(Application.class)
        .run(args);
}
```

启动起来了 然后立即停止了 为什么 



