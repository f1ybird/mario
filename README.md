[参考博客 作者：biezhi](https://www.jianshu.com/p/d20b4890c437)

##### 项目规划图

- 项目规划
- 路由设计
- 控制器设计
- 配置设计
- 视图设计
- 数据库设计
- 增删改查

##### 总体设计思路
```
mario 是基于servlet实现的mvc，用一个全局的Filter来做核心控制器，
使用sql2o框架作为数据库基础访问。 使用一个接口Bootstrap作为初始化启动，
实现它并遵循Filter参数约定即可。
```
##### 路由形式如下
```
/resources/:resource/actions/:action
http://bladejava.com
http://bladejava.com/docs/modules/route
```
##### 路由思路
```
Mario使用一个Filter接收所有请求，因为从Filter过来的请求有无数，如何知道哪一个请求对应哪一个路由呢？ 这时候需要设计一个路由匹配器去查找路由处理我们配置的请求， 有了路由匹配器还不够，这么多的路由我们如何管理呢？再来一个路由管理器吧，下面就创建路由匹配器和管理器2个类：
路由匹配器使用了正则去遍历路由列表，匹配合适的路由。
```
