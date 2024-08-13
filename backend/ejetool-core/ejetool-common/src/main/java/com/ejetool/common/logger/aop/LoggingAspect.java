package com.ejetool.common.logger.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@SuppressWarnings("java:S2737")
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * com.ejetool..*(..)) && @target(org.springframework.stereotype.Service) && !execution(public static * com.ejetool..*(..))")
    private void serviceMethodCut(){}
  
    @Around("serviceMethodCut()")
    public Object debugLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        if(!logger.isDebugEnabled()){
            return joinPoint.proceed();
        }

        // MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // String method = signature.getMethod().getName();
        String method = joinPoint.getSignature().toShortString();
        logger.debug("======= method name = {} =======", method);

        Object[] args = joinPoint.getArgs();
        var length = args.length;
        for (int i = 0; i < length; i++){
            var clazz = args[i].getClass();
            logger.debug("parameters:{}:{}: {}", i, clazz.getSimpleName(), args[i]);
        }

        // proceed()를 호출하여 실제 메서드 실행
        try {
            Object returnObj = joinPoint.proceed();

            // 메서드의 리턴값 로깅
            logger.debug("return:{}: {}", returnObj.getClass().getSimpleName(), returnObj);

            return returnObj;
        } catch(Exception e){
            throw e;
        }
    }

}
