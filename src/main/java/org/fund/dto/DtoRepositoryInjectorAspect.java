package org.fund.dto;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.fund.repository.JpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Aspect
@Component
public class DtoRepositoryInjectorAspect {

    private final JpaRepository repository;

    public DtoRepositoryInjectorAspect(JpaRepository repository) {
        this.repository = repository;
    }
    @Pointcut("execution(* org.fund..*Controller.to*(..)) || " +
              "execution(* org.fund..*Service.to*(..))    || " +
              "execution(* org.fund..*Abstract.to*(..))   || " +
              "execution(* org.fund..*Visitor.to*(..))")
    public void allControllerMethods() {
    }

    @Before("allControllerMethods()")
    public void injectRepositoryToDto(JoinPoint joinPoint) {
        boolean found = false;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseDto dto) {
                dto.setRepository(repository);
                found = true;
            }

            if (arg instanceof List<?> collection) {
                for (Object item : collection) {
                    if (item instanceof BaseDto dtoItem) {
                        dtoItem.setRepository(repository);
                        found=true;
                    }
                }
            }
            if (found) break;
        }
    }
}
