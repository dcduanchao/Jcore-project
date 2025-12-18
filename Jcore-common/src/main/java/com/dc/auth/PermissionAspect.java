package com.dc.auth;

import com.dc.result.ResultVo;
import com.dc.auth.PermissionUtil;
import com.dc.auth.RequiresPermission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限校验切面
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Pointcut("@annotation(com.dc.auth.RequiresPermission)")
    public void permissionPointcut() {}

    @Around("permissionPointcut()")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);
        if (annotation != null) {
            String permissionCode = annotation.value();
            String message = annotation.message();

            if (!PermissionUtil.hasPermission(permissionCode)) {
                log.warn("权限校验失败: method={}, permission={}, message={}",
                        method.getName(), permissionCode, message);
                return ResultVo.error(message);
            }

            log.debug("权限校验通过: method={}, permission={}", method.getName(), permissionCode);
        }

        return joinPoint.proceed();
    }
}
