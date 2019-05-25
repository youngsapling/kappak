# kappak
1. server端部署在前端可以调用到的地方, 提供请求转发功能.
2. client端嵌套在各自后端的项目中, 启动时在配置文件中填写自己的name.并将server端的地址修改正确.
3. 外部请求server端时在请求头中添加 clientName : 目标后端. 请求方式为Post, 数据类型为json.
4. 暂时只支持url路径不包含通配符 和 ${id}这种方式的.
