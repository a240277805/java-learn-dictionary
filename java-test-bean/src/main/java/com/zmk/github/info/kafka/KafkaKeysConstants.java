package com.zmk.github.info.kafka;

import java.util.HashSet;

/**
 * @Author zmk
 * @Date: 2020/12/11/ 16:29
 * @Description kafka 发消息 相关
 */
public class KafkaKeysConstants {
    //项目部
    public final static String QF_PROJECT = "devops";
    //服务
    public final static String SERVICE = "devplatform";
    //服务类型-方法
    public final static String SERVICE_CACHE_TYPE_METHOD = "method";
    //服务类型-字典
    public final static String SERVICE_CACHE_TYPE_DICTIONARY = "dictionary";

    // TOPIC
    public static final String DEVOPS_PLATFORM_EVENT_TOPIC = "DEVOPS_PLATFORM_EVENT_TOPIC";
    // 分组
    public static final String DEVOPS_PLATFORM_CACHE_GROUP = "DEVOPS_PLATFORM_CACHE_GROUP";
    //项目相关
    public static final String PROJECT_UPDATE_EVENT = "project_update_event";
    public static final String PROJECT_ADD_EVENT = "project_add_event";
    public static final String PROJECT_DELETE_EVENT = "project_delete_event";
    //任务消息事件
    public static final String TASK_DELETE_EVENT = "task_delete_event";
    public static final String TASK_ADD_EVENT = "task_add_event";
    public static final String TASK_UPDATE_EVENT = "task_update_event";
    //缺陷消息事件
    public static final String DEFECT_DELETE_EVENT = "defect_delete_event";
    public static final String DEFECT_ADD_EVENT = "defect_add_event";
    public static final String DEFECT_UPDATE_EVENT = "defect_update_event";
    //需求消息事件
    public static final String DEMAND_DELETE_EVENT = "demand_delete_event";
    public static final String DEMAND_ADD_EVENT = "demand_add_event";
    public static final String DEMAND_UPDATE_EVENT = "demand_update_event";
    //测试用例消息事件
    public static final String TEST_CASE_DELETE_EVENT = "test_case_delete_event";
    public static final String TEST_CASE_ADD_EVENT = "test_case_add_event";
    public static final String TEST_CASE_UPDATE_EVENT = "test_case_update_event";
    //代码库相关
    public static final String GITLAB_UPDATE_EVENT = "gitlab_update_event";
    public static final String GITLAB_ADD_EVENT = "gitlab_add_event";
    public static final String GITLAB_DELETE_EVENT = "gitlab_delete_event";
    public static final String GITLAB_ADD_RELATION_EVENT = "gitlab_add_relation_event";
    public static final String GITLAB_DELETE_RELATION_EVENT = "gitlab_delete_relation_event";
    //代码库代理相关更新（增删改）操作事件
    public static final String GITLAB_AGENT_UPDATE_EVENT = "gitlab_agent_update_event";


    /**
     * 获取 项目相关事件
     *
     * @return
     */
    public static HashSet<String> getProjectEventSets() {
        HashSet<String> keysSet = new HashSet<String>();
        //项目相关
        keysSet.add(KafkaKeysConstants.PROJECT_UPDATE_EVENT);
        keysSet.add(KafkaKeysConstants.PROJECT_ADD_EVENT);
        keysSet.add(KafkaKeysConstants.PROJECT_DELETE_EVENT);
        return keysSet;
    }

    /**
     * 获取 事务相关事件
     *
     * @return
     */
    public static HashSet<String> getAffairEventSets() {
        HashSet<String> keysSet = new HashSet<String>();
        //任务相关
        keysSet.add(KafkaKeysConstants.TASK_DELETE_EVENT);
        keysSet.add(KafkaKeysConstants.TASK_ADD_EVENT);
        keysSet.add(KafkaKeysConstants.TASK_UPDATE_EVENT);
        //缺陷相关
        keysSet.add(KafkaKeysConstants.DEFECT_DELETE_EVENT);
        keysSet.add(KafkaKeysConstants.DEFECT_ADD_EVENT);
        keysSet.add(KafkaKeysConstants.DEFECT_UPDATE_EVENT);
        //需求相关
        keysSet.add(KafkaKeysConstants.DEMAND_DELETE_EVENT);
        keysSet.add(KafkaKeysConstants.DEMAND_ADD_EVENT);
        keysSet.add(KafkaKeysConstants.DEMAND_UPDATE_EVENT);
        //测试用例相关
        keysSet.add(KafkaKeysConstants.TEST_CASE_DELETE_EVENT);
        keysSet.add(KafkaKeysConstants.TEST_CASE_ADD_EVENT);
        keysSet.add(KafkaKeysConstants.TEST_CASE_UPDATE_EVENT);
        return keysSet;
    }

    /**
     * 获取 代码库相关 事件
     *
     * @return
     */
    public static HashSet<String> getGitlabEventSets() {
        HashSet<String> keysSet = new HashSet<String>();
        //GITLAB 相关
        keysSet.add(KafkaKeysConstants.GITLAB_UPDATE_EVENT);
        keysSet.add(KafkaKeysConstants.GITLAB_ADD_EVENT);
        keysSet.add(KafkaKeysConstants.GITLAB_DELETE_EVENT);
        keysSet.add(KafkaKeysConstants.GITLAB_ADD_RELATION_EVENT);
        keysSet.add(KafkaKeysConstants.GITLAB_DELETE_RELATION_EVENT);
        keysSet.add(KafkaKeysConstants.GITLAB_AGENT_UPDATE_EVENT);
        return keysSet;
    }

}
