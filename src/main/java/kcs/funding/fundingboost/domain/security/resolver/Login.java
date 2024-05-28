package kcs.funding.fundingboost.domain.security.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)//어노테이션이 적용될 위치를 지정
@Retention(RetentionPolicy.RUNTIME)//어노테이션이 어디까지 유지될지 지정
public @interface Login {
}
