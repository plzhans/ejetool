package com.ejetool.account.service.application;

import com.ejetool.account.service.dto.project.CreateProjectApiKeyParam;
import com.ejetool.account.service.dto.project.CreateProjectApiKeyResult;

public interface ProjectService {

    CreateProjectApiKeyResult createApiKey(CreateProjectApiKeyParam param);
    
}