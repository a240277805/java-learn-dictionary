package com.zmk.github.controller;

import com.zmk.github.info.es.ProjectDetailIndexInfo;
import com.zmk.github.info.es.ProjectIndexInfo;
import com.zmk.github.service.IEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zmk
 * @Date: 2020/12/17/ 15:16
 * @Description
 */
@RestController
@RequestMapping("/es")
public class EsController {
    @Autowired
    private IEsService iEsService;

    @PostMapping(value = "postProject", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object postProject(@RequestBody ProjectIndexInfo request) {

        iEsService.addProjectInfo(request);
        return "result";
    }

    @PostMapping(value = "postProjectDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object postProjectDetail(@RequestBody ProjectDetailIndexInfo request) {

        iEsService.addProjectDetailInfo( request);
        return "result";
    }
}