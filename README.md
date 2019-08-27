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

     见上节

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

2. 