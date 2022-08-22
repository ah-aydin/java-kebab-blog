package com.kebab.blog.api.body;

import lombok.Data;

@Data
public class AddRoleToUserBody {
    private String username;
    private String roleName;
}
