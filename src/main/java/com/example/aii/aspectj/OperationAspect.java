package com.example.aii.aspectj;

import com.example.aii.entity.BaseEntity;
import com.example.aii.entity.User;
import com.example.aii.shiro.LoginUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Aspect
@Component
public class OperationAspect {

    Logger logger = LoggerFactory.getLogger(OperationAspect.class);

    @Pointcut("execution(* com.example.aii.mapper.*.update*(..))")
    protected void updateOperation() {
    }

    @Pointcut("execution(* com.example.aii.mapper.*.insert*(..))")
    protected void insertOperation() {
    }

    @Pointcut("execution(* com.example.aii.service.*.saveBatch(..))")
    protected void saveBatchOperation() {
    }

    @Pointcut("execution(* com.example.aii.service.*.updateBatch(..))")
    protected void updateBatchOperation() {
    }

    @Pointcut("execution(* com.example.aii.mapper.*.delete*(..)) || execution(* com.example.aii.*.delete*(..))")
    protected void deleteOperation() {
    }

    @Pointcut("execution(* com.example.aii.*.removeBatch(..)) || execution(* com.example.aii.*.deleteBatch(..))")
    protected void deleteBatchOperation() {
    }


    @Before("updateOperation()")
    public void setEditor(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            if (c instanceof BaseEntity) {
                User loginUser = LoginUtils.getLoginUser();
                ((BaseEntity) c).setEditor(loginUser.getUsername());
                logger.info("[{}] update: "+ c.toString(), loginUser.traceId());
            }
        });
    }

    @Before("insertOperation()")
    public void setCreator(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            if (c instanceof BaseEntity) {
                User loginUser = LoginUtils.getLoginUser();
                ((BaseEntity) c).setCreator(loginUser.getUsername());
                logger.info("[{}] insert: "+ c.toString(), loginUser.traceId());
            }
        });
    }

    @Before("saveBatchOperation()")
    public void setCreatorBatch(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            if (c instanceof Collection) {
                for (Object o : ((Collection<?>) c).toArray()) {
                    if (o instanceof BaseEntity) {
                        User loginUser = LoginUtils.getLoginUser();
                        ((BaseEntity) o).setCreator(loginUser.getUsername());
                        logger.info("[{}] insert: "+ c.toString(), loginUser.traceId());

                    }
                }
            }
        });
    }

    @Before("updateBatchOperation()")
    public void setEditorBatch(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            if (c instanceof Collection) {
                for (Object o : ((Collection<?>) c).toArray()) {
                    if (o instanceof BaseEntity) {
                        User loginUser = LoginUtils.getLoginUser();
                        ((BaseEntity) o).setEditor(loginUser.getUsername());
                        logger.info("[{}] update: "+ c.toString(), loginUser.traceId());
                    }
                }
            }
        });
    }

    @Before("deleteOperation()")
    public void delete(JoinPoint joinPoint) {
        User loginUser = LoginUtils.getLoginUser();
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            logger.info("[{}] delete: "+c.toString(), loginUser.traceId());
        });
    }

    @Before("deleteBatchOperation()")
    public void deleteBatch(JoinPoint joinPoint) {
        User loginUser = LoginUtils.getLoginUser();
        Arrays.stream(joinPoint.getArgs()).forEach(c -> {
            if (c instanceof Collection) {
                for (Object o : ((Collection<?>) c).toArray()) {
                    logger.info("[{}] delete: "+o.toString(), loginUser.traceId());
                }
            }
        });
    }
}
