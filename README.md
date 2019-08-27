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

3. **注册中心高可用**

   启动多个Eureka实例，Eureka实例之间相互注册 **（待实践）**



### 3 服务消费和负载均衡组件Ribbon

* 准备工作：把我们之前的服务提供者从不同的端口启动两份，现在，在eureka上就可以看见这个服务有两个这样的服务启动着

  ```shell
  java -jar hello-provider/target/hello-provider-0.0.1-SNAPSHOT.jar --server.port=9000
  java -jar hello-provider/target/hello-provider-0.0.1-SNAPSHOT.jar --server.port=9001
  ```

1. **导入依赖**

   ```xml
       <dependencies>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
           </dependency>
   
           <!-- 载均衡器Ribbon的依赖-->
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
           </dependency>
       </dependencies>
   ```

2. **编写配置文件**

   ```yaml
   server:
     port: 10000
   
   spring:
     application:
       name: ribbon-consumer
   
   eureka:
     client:
       service-url: http://localhost:8000/eureka
   ```

3. **在启动类上加上允许发现服务的注解**

   ```java
   @EnableEurekaClient
   @SpringBootApplication
   public class RibbonApplication {
       public static void main(String[] args) {
           SpringApplication.run(RibbonApplication.class, args);
       }
       
       @LoadBalanced
       @Bean
       /**
        * 这里使用一个Rest模板帮我们调用服务的Rest接口，打上开启负载均衡的注解来开启负载均衡
        */
       public RestTemplate restTemplate() {
           return new RestTemplate();
       }
   }
   ```

   这里需要一个发现服务的注解并且需要一个RestTemplate的Bean来通过Http调用不同的服务。在创建这个Bean的时候就需要加上开启负载均衡的注解，这样Ribbon就会帮我做负载均衡

4. **编写业务代码并且使用Ribbon**

   ```java
   @RestController
   public class RibbonController {
       @Autowired
       private RestTemplate restTemplate;
   
       private static String SERVICE_NAME = "HELLO-PROVIDER";
   
       @GetMapping("/ribbon-hello")
       public String hello() {
           //填写地址的时候需要使用服务在eureka上填写的服务名称进行调用
           return restTemplate.getForEntity("http://" + SERVICE_NAME + "/hello", String.class).getBody();
       }
   }
   ```



做完以上步骤，启动ribbon服务，访问相关接口时，ribbon内部就会默认使用轮询的方式进行负载均衡。