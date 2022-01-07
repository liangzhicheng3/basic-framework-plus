package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.exception.TransactionException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * spring validator参数校验工具类
 * @author liangzhicheng
 */
public class ValidateUtil {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidateUtil() {}

    private static ValidateUtil instance;

    public static ValidateUtil getInstance() {
        if(instance == null){
            instance = new ValidateUtil();
        }
        return instance;
    }

    /**
     * 校验对象
     * @param object 待校验对象
     * @param clazz 待校验的class
     * @throws TransactionException 校验不通过，则报TransactionException异常
     */
    public void validate(Object object, Class<?> ... clazz) throws TransactionException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, clazz);
        if(ListUtil.sizeGT(constraintViolations)){
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>) constraintViolations.iterator().next();
            throw new TransactionException(constraint.getMessage());
        }
    }

}
