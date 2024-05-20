package aleksey.aop.aspectOP;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Order(2)
public class FirstAspect {
    /**
     * Данный поинткат проверяет является ли этот класс контролером
     *
     * @within - будет смотреть анатации над данным класом
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void isControllerLayer() {
    }

    /**
     * within - ищет все классы которые заканчиваются на Service
     */
    @Pointcut("within(aleksey.aop.service.*Service)")
    public void isServiceLayer() {
    }


    /**
     * две анатации позволяющие определить что класс является репозиторием
     */
    @Pointcut("this(org.springframework.stereotype.Repository)")
    //@Pointcut("target(org.springframework.stereotype.Repository)")
    public void isRepositoryLayer() {
    }

    /**
     * будет смотреть все анатации @GetMapping() во всем проекте
     * все метода всех классов будет работать долго
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void hasGetMapping() {
    }


    /**
     * мы можем добовлять условия что бы искалось только в определенном месте
     * как паказанно ниже мы добавили метод который ищет классы контроллеры и только
     * в них теперь будет искатся анатацыя @GetMapping()
     */
    @Pointcut("isControllerLayer() && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void hasGetMappingOnController() {
    }

    /**
     * args - проверяет входщие аргументы в методах контроллера где стоит @PathVariable и еще какие то
     * параметры за это отвечают две точки если их убрать будет проверятся только с одним параметром
     * можно указать ...PathVariable,*)") тогда будет два пораметра еще одна звездочка еще пораметр
     */
    @Pointcut("isControllerLayer() && @args(org.springframework.web.bind.annotation.PathVariable, ..)")
    public void hasPathVariable() {
    }


    /**
     * проверяет входной параметр входит ли этот класс
     */
    @Pointcut("isControllerLayer() && args(aleksey.aop.dto.UserRequest, ..)")
    public void hasUserRequest() {
    }

    /**
     * ищет определенный бин загруженный в контекст
     */
    @Pointcut("bean(userService)")
    public void isUserServiceBean() {
    }

    /**
     * ищет все бины которые заканчиваются на Service
     */
    @Pointcut("bean(*Service)")
    public void isServiceLayerBean() {
    }


    /**
     * execution : позволяет найти любой метод с нужными пораметрами
     * public : указываем индификатор метода
     * UserResponse : возврощяемый параметр метода
     *      можно указать * и тогда будет искатся с любыми входными параметрами
     * aleksey.aop.service.*Service... : путь где нужно искать
     *      *Service все что оканчивается на Service
     * .get(Long) : метод который нужно найти со входным параметром
     *      так же можно указать get(Long, ..) сколько угодно параметров
     *      get(Long, *) : один параметр
     *      get(Long, *, *, ....) : столько сколько нужно параметров
     */
    @Pointcut("execution(public UserResponse aleksey.aop.service.*Service.get(Long))")
    public void anyServiceFindByIdMethod() {
    }

    /**
     * таким образом мы будем искать метод get везде где он только есть с любым одним входным параметром
     */
    @Pointcut("execution(public * get(*))")
    public void allFindByIdMethod() {
    }


    // Advice

    /**
     * @Before отлавливает в самом начале на уровне когда приходят пораметры
     * JoinPoint - позволяет получать дополнительную информацию сигнатуру прокси
     * args(id) - позволяет получать входные параметры
     * target(service) - получаем доступ к нашему обьекту сервис
     * this(serviceProxy) - получаем доступ к прокси
     * @within(transactional) - получаем информацию о транзакции
     */
    @Before("anyServiceFindByIdMethod() " +
            "&& args(id) " +
            "&& target(service) " +
            "&& this(serviceProxy) && @within(transactional)")
    public void addBeforeAnyServiceFindByIdMethod(JoinPoint joinPoint,
                                            Object id,
                                            Object service,
                                            Object serviceProxy,
                                            Transactional transactional) {}


    /**
     * данный метод вызывается на уровне завершения метода перед ретурном
     * @param service возврощает обьект service
     * @param result возврощает return
     */
    @AfterReturning(value = "anyServiceFindByIdMethod() && target(service)", returning = "result")
    public void addAfterReturningAnyServiceFindByIdMethod(Object service, Object result){};


    /**
     * аналогично AfterReturning только если появится исключение
     */
    @AfterThrowing(value = "anyServiceFindByIdMethod() && target(service)", throwing = "throwable")
    public void addAfterThrowingAnyServiceFindByIdMethod(Object service, Throwable throwable){};

    /**
     * отлавливает после завершения
     */
    @After("anyServiceFindByIdMethod() && target(service)")
    public void addAfterAnyServiceFindByIdMethod(Object service){};


    /**
     * сробатывает на всех этапах
     */
    @Around("anyServiceFindByIdMethod() && target(service) && args(id)")
    public void addAfterAnyServiceFindByIdMethod(Object service, Object id){};

}
