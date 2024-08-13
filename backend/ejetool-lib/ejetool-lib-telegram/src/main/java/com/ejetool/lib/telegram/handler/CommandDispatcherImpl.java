package com.ejetool.lib.telegram.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ejetool.common.exception.BaseException;
import com.ejetool.lib.telegram.exception.CommandHandlerInvokerException;

public class CommandDispatcherImpl implements CommandDispatcher {
    private final Map<String, CommandHandlerInvoker> commandMap = new HashMap<>();

    @Override
    public List<CommandMapping> getCommandMappingList() {
        return this.commandMap.values().stream()
            .map(x->x.getMapping())
            .collect(Collectors.toList());
    }

    public void initialize() {
        // nothing
    }

    public void addCommand(String command, CommandHandlerInvoker method){
        commandMap.put(command, method);
    }

    public Object createInstance(Class<?> clazz) throws CommandHandlerInvokerException {
        Object inst;
        try {
            inst = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new CommandHandlerInvokerException("createInstance:(): exception.", e);
        }
        return inst;
    }

    public boolean foward(CommandRequest request){
        CommandHandlerInvoker invoker = this.commandMap.get(request.getCommandName());
        if(invoker != null){
            var inst = this.createInstance(invoker.getClass());
            try {
                invoker.getHandlerMethod().invoke(inst, request);
            } catch (BaseException e) {
                throw e;
            } 
            catch (Exception e) {
                throw new CommandHandlerInvokerException("dispatch:(): exception.", e);
            }
            return true;
        }
        return false;
    }

}
