package haneul.haneulspring.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class PrototypeTest {

    @Test
    void prototypeBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1  = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2  = ac.getBean(PrototypeBean.class);
        //init 함수까지만 호출
        System.out.println("prototypeBean1 = "+prototypeBean1);
        System.out.println("prototypeBean2 = "+prototypeBean2);
        Assertions.assertThat(prototypeBean1).isNotSameAs(prototypeBean2);
        prototypeBean1.destroy();
        prototypeBean2.destroy();
    }

    //프로토 타입 Bean은 스프링 컨테이너가 빈 생성 후 의존관계 주입 후 초기화 처리후 클라이언트에게 넘김.
    //종료 메서드에 대한 호출은 클라이언트가 관여함

    @Scope("prototype") //prototype 초기화까지만 처리 후 클라이언트에게 넘김
    static class PrototypeBean{
        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init");
        }
        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
