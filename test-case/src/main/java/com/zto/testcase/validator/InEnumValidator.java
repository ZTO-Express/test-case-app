package com.zto.testcase.validator;

import com.zto.testcase.validator.anno.InEnum;
import java.lang.reflect.Field;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class InEnumValidator implements ConstraintValidator<InEnum, String> {

    private String message;

    private Class enumClass;

    private String attribute;

    @Override
    public void initialize(InEnum constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.enumClass = constraintAnnotation.enumClass();
        this.attribute = constraintAnnotation.attribute();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value)) {
            return true;
        }
        Field field = null;
        try {
            field = enumClass.getDeclaredField(attribute);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            log.error(enumClass.getName() + "不存在此字段：" + attribute);
        }

        for (Object enumObj : enumClass.getEnumConstants()) {
            try {
                if (value.equals(field.get(enumObj))) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                break;
            }
        }

        if (StringUtils.isNotBlank(message)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }
        return false;
    }
}
