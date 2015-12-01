#MyCalendar

##应用说明

###场景
- 进入系统后默认显示当前周，所有预定情况 （Done）
- 每个预定显示对应时间/人物/删除链接
- 用户可以选择任何空闲区域进行预定，cookie保留用户名和密码
- 用户可以取消预订，需要输入原有密码
- 

###运行说明
1. 执行WebRoot/DBInit下的数据库初始脚本；
2. 修改WebRoot/WEB-INF/jdbcConfig.properties中的数据库连接；
3. 运行com.jin.calendar.SysConfig即可启动服务；
4. 打开浏览器，直接输入[localhost](http://127.0.0.1)查看效果。

##开发环境及技术

* window7，Eclipse4.3，JDK7，MySql5.6
* JFinal，FullCalendar，Jquery,qTip2
