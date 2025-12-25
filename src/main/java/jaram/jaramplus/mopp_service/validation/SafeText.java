package jaram.jaramplus.mopp_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SafeTextValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeText {
    String message() default "입력값에 허용되지 않는 문자가 포함되어 있습니다!!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
