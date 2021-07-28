# 构建无外部挂载配置文件的镜像
FROM harbor.ctfo.com/openjdk/openjdk:8-jre-alpine

# local config
ENV WORK_PATH /devops
RUN mkdir $WORK_PATH

# set timezone.
COPY deploy-k8s/docker-build/Shanghai /etc/localtime

# copy app file. todo 待修改（脚本读本次打包的jar包名）
COPY start/target/start-1.0.1-SNAPSHOT.jar $WORK_PATH
# copy agent to image
COPY agent $WORK_PATH/agent


# set java opt.
ENV JAVA_OPT -Xmx512M -Xms512M -Xmn256M -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -Duser.timezone=GMT+08
# 增加 skywalking agent 环境变量
ENV AGENT_OPT -javaagent:$WORK_PATH/agent/skywalking-agent.jar -Dskywalking.agent.service_name=products-server
# set wordir
WORKDIR $WORK_PATH

# 暴露端口
EXPOSE 8282

# 启动
CMD java $JAVA_OPT $AGENT_OPT $AGENT_HOST -jar start*.jar $DEVOPS_SRPING_ACTIVE
 #--spring.cloud.bootstrap.location=conf/bootstrap.yml  --spring.config.location=conf/application.yaml