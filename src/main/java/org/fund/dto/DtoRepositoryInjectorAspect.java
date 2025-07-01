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

    @Pointcut("execution(* org.fund..*Dto.*(..))")
    public void allControllerMethods() {
    }

    @Before("allControllerMethods()")
    public void injectRepositoryToDto(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BaseDto dto) {
                dto.setRepository(repository);
            }

            if (arg instanceof List<?> collection) {
                for (Object item : collection) {
                    if (item instanceof BaseDto dtoItem) {
                        dtoItem.setRepository(repository);
                    }
                }
            }
        }
    }
}
