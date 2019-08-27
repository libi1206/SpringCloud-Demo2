# 搭建SpringCloud

### 1、SpringCloud依赖的基本写法

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.libi.eureka.server</groupId>
    <artifactId>eureka-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <!--参数-->
    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.SR2</spring-cloud.version>
    </properties>

  	<!--引用的具体的依赖，不用写版本号，下面的一贯管理组件已经管理好了-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>

    <!--版本管理，限定了只用这个版本的spring cloud-->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

</project>
```

### 2、服务治理组件Eureka

1. **注册中心Eureka Server**

   * 导入依赖

     见上节，核心依赖如下

     ```xml
         <dependencies>
             <dependency>
                 <groupId>org.springframework.cloud</groupId>
                 <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
             </dependency>
         </dependencies>
     ```

   * 添加配置文件

     ```yml
     server:
       port: 8000
     eureka:
       instance:
         hostname: localhost
       client:
         # 不向注册中心反注册自己
         register-with-eureka: false
         # 不检索服务
         fetch-registry: false
         eureka-server-u-r-l-context: http://${eureka.instance.hostname}:${server.port}/eureka/
     ```

   * 在启动类上加上Eureka Server的注解

     ```java
     @EnableEurekaServer
     @SpringBootApplication
     public class EurekaServerApplication {
         public static void main(String[] args) {
             SpringApplication.run(EurekaServerApplication.class, args);
         }
     }
     ```

   * 直接运行这个项目，访问eureka的地址就可以看见Eureka的相关界面

2. **向Eureka注册服务**

   * 导入依赖

     核心依赖如下

     ```xml
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
     </dependency>
     
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
     </dependency>
     ```

   * 添加配置文件

     ```yaml
     server:
       port: 9000
     
     spring:
       # 服务名称，这个名称会显示在Eureka上
       application:
         name: hello-provider
     
     eureka:
       client:
         # 填写EurekaServer所在的地址和Uri，除了defaultZone以外还可以填写多个，defaultZone是自定义的名称
         service-url:
           defaultZone: http://localhost:8000/eureka
     
     ```

   * 编写业务代码（Restful接口）

     ```java
     @Slf4j
     @RestController
     public class HelloController {
     
         @GetMapping("/hello")
         public String hello() {
             log.info("hello controller 初始化完成");
             return "hello world";
         }
     }
     ```

   * 启动类上添加注解

     ```java
     @SpringBootApplication
     @EnableEurekaClient
     public class HelloApplication {
         public static void main(String[] args) {
             SpringApplication.run(HelloApplication.class, args);
         }
     }
     ```

   * 启动这个项目，就可以在Eureka上看见这个项目的实例