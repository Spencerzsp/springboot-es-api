package com.bigdata.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @ author spencer
 * @ date 2020/6/17 11:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {

    private String name;

    private int age;
}
