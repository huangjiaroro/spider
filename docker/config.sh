HOSTS_DEF_FILE="hosts.$THS_TIER"
if [ ! -z "${HOSTS_DEF_FILE}" ] && [ -r "${HOSTS_DEF_FILE}" ]; then
    cat $HOSTS_DEF_FILE >> /etc/hosts
fi

APP_NAME=spider

JAR_NAME="${APP_NAME}-web-1.0-SNAPSHOT.jar"

## hadoop用户
export HADOOP_USER_NAME=cbas

LOG_DIR=logs/start

#JAVA_HOME

JVM_HEAP="-Xms1600M -Xmx1600M -Xmn1000M"

JAVA_OPTS="-XX:ParallelGCThreads=8"

RUN_ARGS="--server.tomcat.acceptorThreadCount=2 --server.tomcat.pollerThreadCount=2"