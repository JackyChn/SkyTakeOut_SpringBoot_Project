package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
//    cut in point
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autofillPointCut() {

    }

    @Before("autofillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("Start to autoFill public key words...");

//        1. check the intercepted method type: insert or update
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();  // getSignature: name, type, method...
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);  // get the annotation
        OperationType operationType = annotation.value();  // get the annotation type

//        2. get the intercepted object
        Object[] args = joinPoint.getArgs();  // get all args of autofill: update and insert, but we want to distinguish them, getting object
        if(args == null || args.length == 0) {
            return;
        }
        Object obj = args[0];  // this object may be employee or cate putting at the first place of arg

//        3. preparing the value to assign
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

//        4. assign values to their corresponding property based on type
        if(operationType == OperationType.INSERT) {
//            then those four public keywords are all to be assigned: createTime, createUser, updateTime, updateUser
            try {
                Method setCreateTime = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

//                assign values to their corresponding properties
                setCreateTime.invoke(obj, now);
                setCreateUser.invoke(obj, currentId);
                setUpdateTime.invoke(obj, now);
                setUpdateUser.invoke(obj, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if(operationType == OperationType.UPDATE) {
//            then assign updateTime and updateUser
            try {
                Method setUpdateTime = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = obj.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(obj, now);
                setUpdateUser.invoke(obj, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
