package com.example.aii.aspectj;

import com.example.aii.entity.BaseEntity;
import com.example.aii.entity.User;
import com.example.aii.shiro.LoginUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class OperationAspect {

    @Pointcut("execution(* com.example.aii.mapper.*.update*(..))")
    protected void updateOperation() {
    }

    @Pointcut("execution(* com.example.aii.mapper.*.insert*(..))")
    protected void insertOperation() {
    }

    @Before("updateOperation()")
    public void setEditor(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            if (c instanceof BaseEntity) {
                User loginUser = LoginUtils.getLoginUser();
                ((BaseEntity) c).setEditor(loginUser.getUsername());
            }
        });
    }

    @Before("insertOperation()")
    public void setCreator(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            if (c instanceof BaseEntity) {
                User loginUser = LoginUtils.getLoginUser();
                ((BaseEntity) c).setCreator(loginUser.getUsername());
            }
        });
    }
}
