package com.kebab.blog.utils;

import com.auth0.jwt.algorithms.Algorithm;

public class AlgorithmUtils {

    // TODO remove this to an environment variable
    private static String SECRET_KEY = "my-super-duper-secret-key";

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes());
    }
}
