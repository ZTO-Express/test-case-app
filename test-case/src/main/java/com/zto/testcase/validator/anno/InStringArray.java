package com.zto.testcase.validator.anno;

import com.zto.testcase.validator.InStringArrayValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = InStringArrayValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InStringArray {

    String[] array() default {};

    String message() default "值不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
