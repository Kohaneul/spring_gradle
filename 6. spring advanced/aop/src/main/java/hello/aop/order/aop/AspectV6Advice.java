package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
@Slf4j
public class AspectV6Advice {

    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            //@Before
            log.info("[트랜잭션 시작] {}",joinPoint.getSignature());
            Object result = joinPoint.proceed();    //Around : 반드시 타겟 클래스를 호출해야함
            //@AfterReturning
            log.info("[트랜잭션 커밋] {}",joinPoint.getSignature());
            return result;
        }
        catch(Exception e){
            //@AfterThrowing
            log.info("[트랜잭션 롧백] {}",joinPoint.getSignature());
            throw e;
        }
        finally{
            //@After
            log.info("[트랜잭션 릴리즈] {}",joinPoint.getSignature());
        }
    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint){  //joinpoint 실행 전 로직 실행
      log.info("[before] {} ",joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint,Object result){
        log.info("[return] {} return={}",joinPoint.getSignature(),result);
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.allOrder()", returning = "result")
    public void doReturn2(JoinPoint joinPoint,String result){   //repository.save 메서드가 String 으로 반환 되기때문에 로그 찍힘
        log.info("[return2] {} return={}",joinPoint.getSignature(),result);
    }
    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint,Exception ex){
        log.info("[ex] {} message={}",joinPoint.getSignature(),ex);
    }


    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint){
        log.info("[after] {}",joinPoint.getSignature());
    }
}
