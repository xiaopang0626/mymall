package com.xp.mall.mallmodel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;

    private String name;

    private String password;

    private String email;
}
