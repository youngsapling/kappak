# kappak
1. server端部署在前端可以调用到的地方, 提供请求转发功能.
2. client端嵌套在各自后端的项目中, 启动时在配置文件中填写自己的name.并将server端的地址修改正确.
3. 外部请求server端时在请求头中添加 clientName : 目标后端. 请求方式为Post, 数据类型为json.
4. 暂时只支持url路径不包含通配符 和 ${id}这种方式的.
实现功能: 前端和后端不在同一个局域网无法直接联通, 这时在双方都可以连接到的网络部署server端, client端启动后自动注册到server端. 前端按正常请求后端接口的方式将请求地址修改为server端地址即可.
