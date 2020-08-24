package com.bigdata.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ author spencer
 * @ date 2020/6/18 11:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LolRole {

    private int id;
    private String name;
    private int age;
    private String addr;
}
