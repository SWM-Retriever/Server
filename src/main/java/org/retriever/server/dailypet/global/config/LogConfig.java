package org.retriever.server.dailypet.global.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Aspect
public class LogConfig {

    @Around("bean(*Controller)")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        long startAt = System.currentTimeMillis();

        log.info("----------- REQUEST : {} {}", request.getMethod(), request.getRequestURL());

        Object result = pjp.proceed();

        long endAt = System.currentTimeMillis();

        log.info("----------- RESPONSE : {} {} | Latency : {}ms", response.getStatus(), result, endAt - startAt);

        return result;
    }
}
