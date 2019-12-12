# springBoot-weixin-sell

点餐系统：用到了，微信支付、微信登录、websocket推送通知

### 微信登录

微信支付用到了如下 jar 包，[git地址](https://github.com/Wechat-Group/WxJava)，这是一个别人写好的微信开发工具包，用的人还是挺多的
```
<dependency>
    <groupId>com.github.binarywang</groupId>
    <artifactId>weixin-java-mp</artifactId>
    <!-- <version>3.6.0</version> -->
    <version>2.7.0</version>
</dependency>
```

### 微信支付
这是专门用于微信、支付包支付的 jar 包，[git地址](https://github.com/Pay-Group/best-pay-sdk)，是一个大佬写的，个人感觉还是蛮好用的
```
<dependency>
    <groupId>cn.springboot</groupId>
    <artifactId>best-pay-sdk</artifactId>
    <version>1.1.0</version>
</dependency>
```

### pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.xiaochi</groupId>
    <artifactId>spingBoot_weixindiancan</artifactId>
    <version>1.0-SNAPSHOT</version>

    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.3.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- json 字符串与对象互转 -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>
        <!-- Wechat-Group/WxJava 别人写好的 微信开发工具包 -->
        <dependency>
            <groupId>com.github.binarywang</groupId>
            <artifactId>weixin-java-mp</artifactId>
            <!-- <version>3.6.0</version> -->
            <version>2.7.0</version>
        </dependency>
        <!-- 支付 sdk  别人写好的 -->
        <dependency>
            <groupId>cn.springboot</groupId>
            <artifactId>best-pay-sdk</artifactId>
            <version>1.1.0</version>
        </dependency>
        <!-- 模板 freemaker -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- websoket -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
    </dependencies>

    <build>
        <finalName>sell</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
