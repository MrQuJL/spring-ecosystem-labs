#!/bin/bash

# 定义颜色
GREEN='\033[0;32m'
NC='\033[0m' # No Color

PROJECT_ROOT=$(pwd)
PIDS_FILE="$PROJECT_ROOT/running_pids.txt"

start() {
    echo -e "${GREEN}正在构建项目...${NC}"
    mvn -q clean package -DskipTests
    
    if [ $? -ne 0 ]; then
        echo "构建失败，请检查代码或环境。"
        exit 1
    fi

    echo -e "${GREEN}启动 Eureka Server...${NC}"
    nohup java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar > eureka.log 2>&1 &
    echo $! >> "$PIDS_FILE"
    
    echo "等待 Eureka 启动 (10s)..."
    sleep 10

    echo -e "${GREEN}启动 Service Order...${NC}"
    nohup java -jar service-order/target/service-order-0.0.1-SNAPSHOT.jar > order.log 2>&1 &
    echo $! >> "$PIDS_FILE"

    echo -e "${GREEN}启动 Service Product...${NC}"
    nohup java -jar service-product/target/service-product-0.0.1-SNAPSHOT.jar > product.log 2>&1 &
    echo $! >> "$PIDS_FILE"

    echo -e "${GREEN}启动 Gateway...${NC}"
    nohup java -jar gateway/target/gateway-0.0.1-SNAPSHOT.jar > gateway.log 2>&1 &
    echo $! >> "$PIDS_FILE"

    echo -e "${GREEN}所有服务已启动！日志文件位于当前目录：eureka.log, order.log, product.log, gateway.log${NC}"
    echo "请访问 http://localhost:8761 查看服务注册情况"
}

stop() {
    if [ -f "$PIDS_FILE" ]; then
        echo -e "${GREEN}正在停止服务...${NC}"
        while read pid; do
            if kill -0 "$pid" 2>/dev/null; then
                echo "Killing PID $pid"
                kill "$pid"
            fi
        done < "$PIDS_FILE"
        rm "$PIDS_FILE"
        echo -e "${GREEN}所有服务已停止。${NC}"
    else
        echo "未发现运行中的服务 PID 记录。"
        # 尝试兜底查杀（慎用，这里只查特定的 jar）
        pkill -f "eureka-server-0.0.1-SNAPSHOT.jar"
        pkill -f "service-order-0.0.1-SNAPSHOT.jar"
        pkill -f "service-product-0.0.1-SNAPSHOT.jar"
        pkill -f "gateway-0.0.1-SNAPSHOT.jar"
    fi
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        sleep 2
        start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart}"
        exit 1
esac
