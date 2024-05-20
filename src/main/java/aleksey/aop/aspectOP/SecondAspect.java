package aleksey.aop.aspectOP;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Order(1) задает порядок выполнения аспектов сначала выполнится  SecondAspect потом SecondAspect
 */
@Aspect
@Component
@Order(1)
public class SecondAspect {

    @Around("anyServiceFindByIdMethod() && target(service) && args(id)")
    public void addAfterAnyServiceFindByIdMethod(Object service, Object id){};

    @Pointcut("execution(public * aleksey.aop.service.*Service.get(Long))")
    public void anyServiceFindByIdMethod() {
    }
}
