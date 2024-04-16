package com.vladima.gamingrental.logger;

import com.vladima.gamingrental.security.dto.UserResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Log4j2
public class LoggerAspect {

    @AfterThrowing(pointcut = "within(@org.springframework.stereotype.Service *)", throwing = "e")
    public void logServiceOnException(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}(): {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getStackTrace());
    }

    @AfterReturning(value = "execution(* com.vladima.gamingrental.security.services.UserService.login(..))", returning = "resp")
    public void logAfterUserLogin(JoinPoint joinPoint, UserResponseDTO resp) {
        log.info("{}.{}: User {} has logged in", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), resp.getUserEmail());
    }

    @AfterReturning(pointcut = "execution(* com.vladima.gamingrental.request.exception_handlers.EntitiesExceptionHandler.*(..)) && args(e)")
    public void logCaughtExceptionHandler(JoinPoint joinPoint, Throwable e) {
        log.error("Entity operation exception: {}", e.getMessage());
    }
}
