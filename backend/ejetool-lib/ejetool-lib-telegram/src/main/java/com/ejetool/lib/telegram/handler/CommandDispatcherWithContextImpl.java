package com.ejetool.lib.telegram.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.ejetool.common.exception.BaseException;
import com.ejetool.lib.telegram.exception.CommandHandlerInvokerException;

public class CommandDispatcherWithContextImpl implements CommandDispatcher{
    private final UserRoleManger userRoleManger;
    private final ApplicationContext applicationContext;
    private final Map<String, CommandHandlerInvoker> commandMap;

    public CommandDispatcherWithContextImpl(UserRoleManger userRoleManger, ApplicationContext context){
        this.userRoleManger = userRoleManger;
        this.applicationContext = context;
        this.commandMap = new HashMap<>();
    }

    @Override
    public List<CommandMapping> getCommandMappingList() {
        return this.commandMap.values().stream()
            .map(x->x.getMapping())
            .collect(Collectors.toList());
    }

    @Override
    public void initialize(){
        if(!this.commandMap.isEmpty()){
            return;
        }

        synchronized(this.commandMap){
            if(this.commandMap.isEmpty()){
                Map<String, Object> beans = applicationContext.getBeansWithAnnotation(CommandHandler.class);
                for (Object bean : beans.values()) {
                    Class<?> clazz = bean.getClass();
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(CommandMapping.class)) {
                            CommandMapping commandAnnotation = method.getAnnotation(CommandMapping.class);
                            String command = commandAnnotation.value();
                            CommandHandlerInvoker invoker = new CommandHandlerInvoker(commandAnnotation, clazz, method);
                            this.commandMap.put(command, invoker);
                        }
                    }
                }
            }
        }
    }

    public void addCommand(String command, CommandHandlerInvoker method){
        commandMap.put(command, method);
    }

    public Object createInstance(Class<?> clazz) throws CommandHandlerInvokerException {
        try {
            var inst = applicationContext.getBean(clazz);
            return inst;
        } catch (BeansException e){
            throw new CommandHandlerInvokerException("createInstance:(): exception.", e);
        }
    }

    public boolean foward(CommandRequest request){
        CommandHandlerInvoker invoker = this.commandMap.get(request.getCommandName());
        if(invoker != null){
            var inst = this.createInstance(invoker.getHandlerClass());
            var method = invoker.getHandlerMethod();
            var mapping = invoker.getMapping();
            var roles = mapping.roles();
            try {
                if (roles != null && roles.length > 0){
                    if(this.userRoleManger == null){
                        request.sendReplyMessage("권한 정보를 찾을 수 없습니다.");
                        return true;
                    }
                    if(!this.userRoleManger.isRole(request.getFrom().getId(), roles)){
                        if(request.getUpdate().hasCallbackQuery()){
                            request.sendAnswerCallbackQuery(String.format("권한이 없습니다. (id=%s)", request.getFrom().getId()), false, 5);    
                        } else if(request.getUpdate().hasMessage()){
                            request.sendReplyMessage(String.format("권한이 없습니다. (id=%s)", request.getFrom().getId()));
                        }
                        return true;
                    }
                }
                method.invoke(inst, request);
            } catch (InvocationTargetException e) {
                if ( e.getTargetException() instanceof BaseException baseException){
                    throw baseException;
                } else {
                    throw new CommandHandlerInvokerException(e.getClass().getSimpleName(), e);
                }
            } catch (Exception e) {
                throw new CommandHandlerInvokerException(e.getClass().getSimpleName(), e);
            } 
            return true;
        }
        return false;
    }
}
