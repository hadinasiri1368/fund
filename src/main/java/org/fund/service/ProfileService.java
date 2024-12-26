package org.fund.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ProfileService {
    @Autowired
    private Environment environment;

    public boolean isDebugMode() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }
}
