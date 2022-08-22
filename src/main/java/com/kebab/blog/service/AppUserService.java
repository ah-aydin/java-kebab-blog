package com.kebab.blog.service;

import com.kebab.blog.model.AppUser;
import com.kebab.blog.model.Role;

import java.util.List;

public interface AppUserService {
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
    List<Role> getRoles();
}
