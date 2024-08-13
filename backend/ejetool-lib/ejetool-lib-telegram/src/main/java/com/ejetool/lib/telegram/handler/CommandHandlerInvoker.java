package com.ejetool.lib.telegram.handler;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommandHandlerInvoker {
    private final CommandMapping mapping;
    private final Class<?> handlerClass;
    private final Method handlerMethod;
}
