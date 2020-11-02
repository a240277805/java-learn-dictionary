# docker
## 镜像存储方式
![avator](https://pic2.zhimg.com/80/v2-f3109ae2689c869b28f5033e2dff9669_720w.jpg)

1. 镜像中心域名 
3. 命名空间
3. 仓库名称
4. 标签

## 快捷键方式
* 拉取镜像: docker pull {imageName}
* 查看镜像: docker images 查看镜像
* 在容器中执行命令: docker exec -it {containerName} /bin/bash   ? /bin/sh
* 查看基本信息: docker inspect {containerName}
* 查看内存: docker stats 
* 查看日志： docker logs
* 查看镜像的历史版本可以执行以下命令：docker history &lt;image_name> 
* 将容器的状态保存为镜像： docker commit $sample_job job1 
* 在registry中的镜像可以使用以下命令查找到： docker search &lt;image-name> 
* docker 查看挂载目录 docker inspect container_name | grep Mounts -A 20



<meta http-equiv="refresh" content="1">


