package com.zmk.github.service;

import com.zmk.github.info.es.ProjectDetailIndexInfo;
import com.zmk.github.info.es.ProjectIndexInfo;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:15
 * @Description
 */
public interface IEsService {
    void test();

    void addProjectInfo(ProjectIndexInfo projectIndexInfo);

    void addProjectDetailInfo(ProjectDetailIndexInfo projectDetailIndexInfo);
}
