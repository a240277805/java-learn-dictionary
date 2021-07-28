#!/bin/bash
docker_tag=`cat $1`
kubectl set image deployment/ctfo-devops-server ctfo-devops-server=harbor.ctfo.com/devops/devplatform-server:$docker_tag --record --namespace devops

#pod_name=`kubectl get pods -l app=ctfo-devops-server -n devops -o jsonpath="{.items[*].metadata.name}"`
#kubectl delete po $pod_name -n devops