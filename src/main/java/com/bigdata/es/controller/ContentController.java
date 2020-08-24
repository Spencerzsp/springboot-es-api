package com.bigdata.es.controller;

import com.bigdata.es.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ author spencer
 * @ date 2020/6/17 15:21
 */
@RestController
public class ContentController {

    @Autowired
    ContentService contentService;

    /**
     * 解析数据到es库中
     * @param keyword
     * @return
     * @throws Exception
     */
    @GetMapping("/parse/{keyword}")
    public boolean parse(@PathVariable("keyword") String keyword) throws Exception {
        boolean parse = contentService.parse(keyword);
        System.out.println(parse);

        return parse;
    }

    @GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String, Object>> search(
            @PathVariable("keyword") String keyword,
            @PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize) throws IOException {

        return contentService.search(keyword, pageNum, pageSize);

    }
}
