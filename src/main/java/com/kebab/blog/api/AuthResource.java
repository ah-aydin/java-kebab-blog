package com.kebab.blog.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kebab.blog.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthResource {
    @Autowired
    private final JWTUtils jwtUtils;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> responseBody = jwtUtils.refreshTokenResponse(request);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }
}
