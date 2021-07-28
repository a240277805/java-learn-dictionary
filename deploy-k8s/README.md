# k8s 部署文件夹
## jenkins 
 jenkins 部署相关
 * git-version.sh： 生成最新 commit,branch,tag ，生成到同级别目录
 * git-version git-version.sh： 生成，用来copy 到 容器里，做自动化
 * PROD-Server-DevOps-PipeLine.jenkinsfile：  jenkins 生产环境执行流水线
 * run_k8s_yaml： (test,prod 流水线)使用：k8s 重启使用，删除旧Service,拉取新镜像重启
 
 ## manifest
 k8s  资源清单
 ## docker-build 
 docker 打镜像相关
 * Shanghai  docker 镜像改时区用
 
#  te1
 