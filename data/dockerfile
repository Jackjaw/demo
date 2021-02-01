#使用jdk11作为基础镜像
FROM openjdk:11.0-jre-slim
#指定作者
MAINTAINER Dong Wen
#暴漏容器的8080端口
EXPOSE 8080
#文件拷贝到容器中，并取了个别名
ADD data-1.0.0.jar /dataJob.jar
#设置时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#相当于在容器中用cmd命令执行jar包  指定外部配置文件
ENTRYPOINT ["java","-jar","/dataJob.jar"]