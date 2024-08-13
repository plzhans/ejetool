package com.ejetool.account.service.application;

import com.ejetool.jwt.generator.JwtKeyStoreGenerator;
import com.ejetool.jwt.generator.JwtKeyType;
import com.ejetool.account.service.dto.project.CreateProjectApiKeyParam;
import com.ejetool.account.service.dto.project.CreateProjectApiKeyResult;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final JwtKeyStoreGenerator jwtGenerator;

    @Override
    public CreateProjectApiKeyResult createApiKey(CreateProjectApiKeyParam param){

        String sub = String.valueOf(param.getProjectId());
        String jwtToken = jwtGenerator.builder(JwtKeyType.SYMMETRIC, sub)
            .claim("roles", List.of("*"))
            .compact();

        var result = new CreateProjectApiKeyResult();
        result.setApiKey(jwtToken);
        return result;
    }
}
