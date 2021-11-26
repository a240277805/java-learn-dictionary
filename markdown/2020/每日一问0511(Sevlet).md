# Servlet
## 生命周期
多线程 单实例
doGet doPost 
Config ,Request ,Response
实例初始化 可以配置 tomcat 启动时<load-on-startup>，也可以在第一次调用时。

### Servlet 3.0新特性
- 引入注解配置 :@WebServlet(name = "myFirstServlet",urlPatterns = {"/aaaa"})
- 支持web模块化开发
- 程序异步处理
- 改进文件上传API
- 非阻塞式IO读取流
- Websocket实时通信


参考:

[Servlet3.0新特性](https://cloud.tencent.com/developer/article/1013528)
