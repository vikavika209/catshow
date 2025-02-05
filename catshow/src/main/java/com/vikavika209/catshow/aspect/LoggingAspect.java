package com.vikavika209.catshow.aspect;

//import com.vikavika209.catshow.exception.CatNotFoundException;
//import com.vikavika209.catshow.exception.OwnerNotFoundException;
//import com.vikavika209.catshow.exception.ShowNotFoundException;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import java.util.Arrays;
//
//@Aspect
//@Component
//public class LoggingAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//
//    @Before("execution(* com.vikavika209.catshow..*(..))")
//    public void logMethodStart(JoinPoint joinPoint){
//        String methodName = joinPoint.getSignature().toShortString();
//        Object[] args = joinPoint.getArgs();
//        logger.info("Method {} with arguments {} starts", methodName, Arrays.toString(args));
//    }
//
//    @After("execution(* com.vikavika209.catshow..*(..))")
//    public void logMethodFinish(JoinPoint joinPoint){
//        String methodName = joinPoint.getSignature().toShortString();
//        logger.info("Method {} has been finished", methodName);
//    }
//
//    @AfterThrowing(pointcut = "execution(* com.vikavika209.catshow..*(..))", throwing = "ex")
//    public void logCatNotFoundException(CatNotFoundException ex) {
//        logger.error("CatNotFoundException occurred: {}", ex.getMessage());
//    }
//
//    @AfterThrowing(pointcut = "execution(* com.vikavika209.catshow..*(..))", throwing = "ex")
//    public void logOwnerNotFoundException(OwnerNotFoundException ex) {
//        logger.error("OwnerNotFoundException occurred: {}", ex.getMessage());
//    }
//
//    @AfterThrowing(pointcut = "execution(* com.vikavika209.catshow..*(..))", throwing = "ex")
//    public void logShowNotFoundException(ShowNotFoundException ex) {
//        logger.error("ShowNotFoundException occurred: {}", ex.getMessage());
//    }
//}
