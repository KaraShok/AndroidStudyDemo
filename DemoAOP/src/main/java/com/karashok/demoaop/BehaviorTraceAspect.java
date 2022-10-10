package com.karashok.demoaop;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
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
public class BehaviorTraceAspect {

    /**
     * 定义切面的规则：在原来的应用中那些注解的地方放到当前切面进行处理
     * execution（注解名   注解用的地方）
     */
    @Pointcut("execution(@com.karashok.demoaop.BehaviorTrace *  *(..))")
    public void methodAnnotationWithBehaviorTrace() {

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
    @Around("methodAnnotationWithBehaviorTrace()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String value = methodSignature.getMethod().getAnnotation(BehaviorTrace.class).value();

        long begin = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - begin;
        Log.d("DemoAspectJ", String.format("%s功能：%s类的%s方法执行了，用时%d ms",
            value, className, methodName, duration));
        return proceed;
    }
}
