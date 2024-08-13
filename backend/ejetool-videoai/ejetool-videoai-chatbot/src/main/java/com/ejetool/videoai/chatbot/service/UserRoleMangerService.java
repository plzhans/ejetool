package com.ejetool.videoai.chatbot.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.ejetool.lib.telegram.handler.UserRoleManger;

import lombok.NonNull;

public class UserRoleMangerService implements UserRoleManger {

    private List<Long> adminIds;

    public UserRoleMangerService(long[] adminIds){
        this.adminIds = Arrays.stream(adminIds)
            .boxed()
            .collect(Collectors.toList());
        if(adminIds == null){
            this.adminIds = Collections.emptyList();
        }
    }

    @Override
    public boolean isRole(@NonNull Long id, String[] roles) {
        if(roles == null || roles.length == 0){
            return true;
        } 
        return this.adminIds.contains(id);
    }
    
}
