[参考博客 作者：biezhi](https://www.jianshu.com/p/d20b4890c437)

##### 项目规划图

- 项目规划
- 路由设计
- 控制器设计
- 配置设计
- 视图设计
- 数据库设计
- 增删改查

##### 设计思路
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