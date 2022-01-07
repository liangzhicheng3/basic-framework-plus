package com.liangzhicheng.config.mvc.validate;

import com.liangzhicheng.config.mvc.validate.annotation.FlagValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 自定义校验器
 * @author liangzhicheng
 */
public class FlagValidator implements ConstraintValidator<FlagValidate, Integer> {

    private String[] values;

    @Override
    public void initialize(FlagValidate flagValidate) {
        this.values = flagValidate.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;
        if(value == null){
            return true; //当状态为空时使用默认值
        }
        for(int i = 0; i < values.length; i++){
            if(values[i].equals(String.valueOf(value))){
                isValid = true;
                break;
            }
        }
        return isValid;
    }

}
