# kappak(伪内网穿透组件)

### 适用场景

1. 微信小程序发布后无法联调后端本地服务.
2. 服务部署在局域网内(能连到外网), 从外网无法直连到服务进行一些操作.
3. 前后端联调时不在同一个局域网.

### 几种可自定义的组件

- 组件的定义方式完全和`WebMvcConfigurer`的使用方式一样. 
1. 自定义地址映射器, 继承`IUriSelector`函数式接口, 并将映射器注册到client端的`KappakConfigurer.addUrISelector()`中.
2. 自定义方法参数解析器, 继承`IParamResolver`函数式接口, 并将解析器注册到client端的`KappakConfigurer.addMethodParameterResolver()`中.
3. 自定义重试机制. 使用Guava提供的retryer类, 参数自定义后注册到server端的`KappakConfigurer.addReTryEr()`中.这个单词的驼峰命名真难.

### 演示

1. Request URL, server端地址 + client端方法相对路径. 
2. 请求头 : 
    ````http
       Request-Method: POST
       Content-Type: application/json || application/x-www-form-urlencoded
       clientName: youngsapling
       
3. 不支持url路径中包含 通配符 和 ${id} 的方式.
4. 不支持Filter/Interceptor/Aspect等方式.

![image](https://github.com/youngsapling/kappak/blob/master/images/20190525162003.png)
                                                             
                                                             
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                            -- 我变强了, 也变秃了.
