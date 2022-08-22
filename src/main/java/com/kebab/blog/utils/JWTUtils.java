package com.kebab.blog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kebab.blog.model.AppUser;
import com.kebab.blog.model.Role;
import com.kebab.blog.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JWTUtils {
    public static final long ACCESS_TOKEN_LIFETIME = 1000 * 60 * 60;
    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    @Autowired
    private final AppUserService appUserService;

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
            try {
                return authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
            } catch (Exception e) {
                throw new RuntimeException("Failed to retrieve token from header");
            }
        }
        throw new RuntimeException("No authorization header with 'Bearer' was provided");
    }

    private String getUsername(String token) {
        Algorithm algorithm = AlgorithmUtils.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getSubject();
    }

    private String accessTokenBuilder(HttpServletRequest request, String token) {
        AppUser user = appUserService.getUser(getUsername(token));
        return JWT
                .create()
                .withSubject(user.getUsername())
                .withExpiresAt(getAccessTokenEndDate())
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(AlgorithmUtils.getAlgorithm());
    }

    public Map<String, String> refreshTokenResponse(HttpServletRequest request) {
        String token = getToken(request);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessTokenBuilder(request, token));
        tokens.put("refresh_token", token);

        return tokens;
    }

    public Map<String, String> authTokenResponse(Authentication authentication, HttpServletRequest request) {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = AlgorithmUtils.getAlgorithm();
        String accessToken = JWT
                .create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken = JWT
                .create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    private Date getAccessTokenEndDate() {
        return new Date(System.currentTimeMillis() + ACCESS_TOKEN_LIFETIME);
    }
}
