package com.example.aii.shiro;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

@Component
public class PermissionDirectory implements BeanPostProcessor {

    private static final Map<String, Set<String>> permission = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        RequiresPermissions typeAnn = clazz.getDeclaredAnnotation(RequiresPermissions.class);
        if (typeAnn != null) {
            Method[] methods = clazz.getDeclaredMethods();
            Set<String> resourceLs = new HashSet<>();
            for (Method m : methods) {
                RequiresPermissions methodAnn = m.getAnnotation(RequiresPermissions.class);
                if (methodAnn != null) {
                    resourceLs.add(methodAnn.value()[0]);
                }
            }
            permission.put(typeAnn.value()[0], resourceLs);
        }
        return bean;
    }

    public static Map<String, Set<String>> getPermission() {
        return Collections.unmodifiableMap(permission);
    }
}
