package com.ejetool.account.service.application.interfaces;

import org.springframework.stereotype.Service;

import com.ejetool.account.service.dto.auth.GetPublicKeyListResult;

@Service
public interface AuthService {

    GetPublicKeyListResult getPublicKeys();
    
}
