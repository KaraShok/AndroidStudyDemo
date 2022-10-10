package com.karashok.demoaop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-24-2022
 */
@Aspect
public class UserInfoBehaviorTraceAspect {

    /**
     * 定义切面的规则：在原来的应用中那些注解的地方放到当前切面进行处理
     * execution（注解名   注解用的地方）
     */
    @Pointcut("execution(@com.karashok.demoaop.UserInfoBehaviorTrace *  *(..))")
    public void methodAnnotationWithUserInfoBehaviorTrace() {
    }


    /**
     * 对进入切面的内容如何处理
     * @Before 在切入点之前运行
     * @After 在切入点之后运行
     * @Around 在切入点前后都运行
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("methodAnnotationWithUserInfoBehaviorTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String value = methodSignature.getMethod().getAnnotation(UserInfoBehaviorTrace.class).value();
        Log.d("DemoAspectJ",value + " - 被执行");
        return null;
    }
}
