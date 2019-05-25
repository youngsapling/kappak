实现功能

- 前端和后端不在同一个局域网无法直接联通, 这时在双方都可以连接到的网络部署server端, client端启动后自动注册到server端. 前端按正常请求后端接口的方式将请求地址修改为server端地址即可.

几种可自定义的组件

  -组件的定义方式完全和WebMvcConfigurer的使用方式一样. 

1. 自定义地址映射器, 继承IUriSelector函数式接口, 并将映射器注册到client端的KappakConfigurer.addUrISelector()中.
2. 自定义方法参数解析器, 继承IParamResolver函数式接口, 并将解析器注册到client端的KappakConfigurer.addMethodParameterResolver()中.
3. 自定义重试机制. 使用Guava提供的retryer类, 参数自定义后注册到server端的KappakConfigurer.addReTryEr()中.这个单词的驼峰命名真难.

使用POST调用演示

1. url , 请求地址是 server端地址 + client端方法相对路径. 
2. 在请求头中添加 clientName : 目标后端. 请求方式为Post, 数据类型为json.
3. 暂时不支持url路径中包含 通配符 和 ${id} 的方式.
![](https://github.com/youngsapling/images/TIM图片20190525162003.png)
