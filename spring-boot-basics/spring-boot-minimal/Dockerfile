# 使用官方的基础镜像，这个镜像包含了运行 Java 程序所需的环境
FROM openjdk:8-jre-alpine

# 在容器内创建一个目录，作为应用的家目录
WORKDIR /app

# 将刚才打包好的 JAR 文件从你的电脑复制到容器的工作目录中，并重命名为 app.jar
COPY ./*.jar app.jar

# 告诉 Docker，容器启动时应该运行什么命令
ENTRYPOINT ["java", "-Xms512m", "-Xmx512m", "-jar", "app.jar"]