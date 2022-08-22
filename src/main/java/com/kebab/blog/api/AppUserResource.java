package com.kebab.blog.api;

import com.kebab.blog.api.body.AddRoleToUserBody;
import com.kebab.blog.model.AppUser;
import com.kebab.blog.model.Role;
import com.kebab.blog.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")

public class AppUserResource {
    private final AppUserService appUserService;

    @GetMapping()
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(appUserService.getUsers());
    }

    @PostMapping()
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/auth/users").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveUser(user));
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUser(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(appUserService.getUser(username));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok().body(appUserService.getRoles());
    }
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/auth/roles").toUriString());
        return ResponseEntity.created(uri).body(appUserService.saveRole(role));
    }

    @PostMapping("/add_role")
    public ResponseEntity<?> addRoleToUser(@RequestBody AddRoleToUserBody form) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/auth/users/add_role").toUriString());
        appUserService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.created(uri).build();
    }
}
