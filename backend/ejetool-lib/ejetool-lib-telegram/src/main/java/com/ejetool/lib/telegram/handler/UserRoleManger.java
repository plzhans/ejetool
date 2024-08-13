package com.ejetool.lib.telegram.handler;

import lombok.NonNull;

public interface UserRoleManger {

    boolean isRole(@NonNull Long id, String[] roles);

    
} 