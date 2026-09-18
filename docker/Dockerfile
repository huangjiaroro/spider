#FROM hub.hexin.cn/baseimages/jdk-8
FROM hub-dev.hexin.cn/baseimages/hexin-centos7-java8:latest

COPY ./docker /data/cbas-spider

ENV LANG zh_CN.UTF-8

RUN chmod 755 /data/cbas-spider/bootstrap.sh && \
	ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
	
CMD ["/data/cbas-spider/bootstrap.sh"]
