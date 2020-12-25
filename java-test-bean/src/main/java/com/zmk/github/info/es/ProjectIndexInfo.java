package com.zmk.github.info.es;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:33
 * @Description
 */
@Data
public class ProjectIndexInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @JSONField(serialize = false)
    private String _id;
    /**
     * 时间格式 2020-12-17
     */
    private String date;
    /**
     * 部门  devops
     */
    private String department;
    /**
     * 效能平台项目ID
     */
    private Long ctfo_project_id;

    /**
     * 代码库信息
     */
    private CodeProjectInfo code_info;

}
