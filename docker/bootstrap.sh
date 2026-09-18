#!/bin/bash

cd `dirname $0`

## 加载配置文件
CONFIGFILE="config.sh"
if [ -z "${CONFIGFILE}" ] || [ ! -r "${CONFIGFILE}" ]; then
    echo "没有配置文件"
else
    echo "加载配置文件 ${CONFIGFILE}"
    cat "${CONFIGFILE}"
    echo ""
    echo ""
    source "${CONFIGFILE}"
fi

if [ ! -d "public" ]; then
    ## 避免在tmp目录下产生临时目录
    mkdir -p public
fi

LOG_DIR=${LOG_DIR:=logs/start}

## 创建日志文件目录
if [ ! -d "${LOG_DIR}" ];then
    mkdir -p "${LOG_DIR}"
fi

if [ -z "${JVM_HEAP}" ]; then
    JVM_HEAP="-Xms3G -Xmx3G -Xmn1G"
fi

## 启动参数
JAVA_OPTS="${JAVA_OPTS} -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -server ${JVM_HEAP} -XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M -Xss256K -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:TargetSurvivorRatio=50 -XX:PretenureSizeThreshold=0 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+CMSClassUnloadingEnabled -XX:-UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:${LOG_DIR}/${APP_NAME}_gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOG_DIR}/${APP_NAME}_dump.hprof -XX:OnOutOfMemoryError=\"kill -9 %p\" -XX:ErrorFile=${LOG_DIR}/${APP_NAME}_error_%p.log"

RUN_ARGS="${RUN_ARGS} --spring.profiles.active=${THS_TIER}"

echo -e "Starting the ${APP_NAME} ..."

if [ -n "$JAVA_HOME" ]; then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA=java
fi

## 启动命令
RUN_CMD="${JAVA} ${JAVA_OPTS} -jar ${JAR_NAME} ${RUN_ARGS}"
echo "${RUN_CMD}"

eval "${RUN_CMD}"