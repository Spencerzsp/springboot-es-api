package com.bigdata.es.controller;

import com.bigdata.es.pojo.LolRole;
import com.bigdata.es.service.LolRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ author spencer
 * @ date 2020/6/18 11:29
 */
@RestController
public class LolRoleController {

    @Autowired
    LolRoleService lolRoleService;

    @GetMapping("/add")
    public boolean addRole() throws IOException {
        return lolRoleService.addRole();
    }

    @GetMapping("/search")
    public List<Map> searchRole() throws IOException {
        System.out.println(lolRoleService.searchRole());
        return lolRoleService.searchRole();
    }
}
