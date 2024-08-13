package com.ejetool.account.service.dto.project;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateProjectApiKeyParam {
    private int projectId;
}
