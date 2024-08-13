package com.ejetool.lib.telegram.handler;

import java.util.List;

import com.ejetool.lib.telegram.exception.CommandHandlerInvokerException;

public interface CommandDispatcher {
    void initialize();
    
    void addCommand(String command, CommandHandlerInvoker method);
    
    Object createInstance(Class<?> clazz) throws CommandHandlerInvokerException;
    
    boolean foward(CommandRequest request);

    List<CommandMapping> getCommandMappingList();
}
