package com.animals.app.service;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rostyslav.Viner on 15.09.2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SqlInjection.Validator.class)
public @interface SqlInjection {

    String message() default "SQL injection detected.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<SqlInjection, String> {
        private final String[] regex = {"\\w*((\\%27)|(\\'))\\s*((\\%6F)|o|(\\%4F))((\\%72)|r|(\\%52))" //1'or'1'='1
                                        , "((\\%3D)|(=))[^\\n]*((\\%27)|(\\')|(\\-\\-)|(\\%3B)|(;))"}; //=', =--, =;

        @Override
        public void initialize(final SqlInjection sqlInjection) {
        }

        @Override
        public boolean isValid(final String str, final ConstraintValidatorContext constraintValidatorContext) {
            if (str == null) {
                return true;
            }

            for (int i = 0; i < regex.length; i++) {
                Pattern pattern = Pattern.compile(regex[i], Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(str);

                if (matcher.find()) {
                    return false;
                }
            }

            return true;
        }
    }
}
