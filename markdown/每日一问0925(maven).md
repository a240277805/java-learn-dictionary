# maven  

## Maven 默认处理策略

* 最短路径优先

Maven 面对 D1 和 D2 时，会默认选择最短路径的那个 jar 包，即 D2。E->F->D2 比 A->B->C->D1 路径短 1。
* 最先声明优先

如果路径一样的话，举个�： A->B->C1, E->F->C2 ，两个依赖路径长度都是 2，那么就选择最先声明。

## 检测包冲突工具

```aidl
mvn dependency:help
mvn dependency:analyze
mvn dependency:tree
mvn dependency:tree -Dverbose
```
