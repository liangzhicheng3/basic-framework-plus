package com.liangzhicheng.common.advice;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.liangzhicheng.common.basic.BaseController;
import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.response.ResponseResult;
import com.liangzhicheng.common.utils.PrintUtil;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常捕获处理类
 * @author liangzhicheng
 */
@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = String.format("%s%s", fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        return super.buildFailedInfo(message);
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ResponseResult bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = String.format("%s%s", fieldError.getField(), fieldError.getDefaultMessage());
            }
        }
        return super.buildFailedInfo(message);
    }

    @ResponseBody
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            InvalidFormatException.class,
            JsonParseException.class,
            DataIntegrityViolationException.class,
            TransactionException.class,
            RuntimeException.class})
    public ResponseResult exception(Exception e) {
        String errorMessage = this.write(e);
        if(e instanceof MissingServletRequestParameterException){
            PrintUtil.error("参数缺失异常，异常信息为：{}", errorMessage);
            return super.buildFailedInfo(e.getMessage());

        }else if(e instanceof TypeMismatchException){

            PrintUtil.error("参数类型异常，异常信息为：{}", errorMessage);
            return super.buildFailedInfo(e.getMessage());

        }else if(e instanceof InvalidFormatException){

            PrintUtil.error("参数格式异常，异常信息为：{}", errorMessage);
            return super.buildFailedInfo(e.getMessage());

        }else if(e instanceof JsonParseException){

            PrintUtil.error("JSON格式异常，异常信息为：{}", errorMessage);
            return super.buildFailedInfo(e.getMessage());

        }else if(e instanceof DataIntegrityViolationException){

            PrintUtil.error("保存异常，异常信息为：{}", errorMessage);
            return super.buildFailedInfo(e.getMessage());

        }else if(e instanceof TransactionException){

            PrintUtil.error("事务处理异常，异常信息为：{}", errorMessage);
            return super.buildFailedInfo(e.getMessage());
        }
        PrintUtil.error("运行时异常，异常信息为：{}", errorMessage);
        return super.buildFailedInfo("服务繁忙，请稍后再试！");
    }

    private String write(Exception e){
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw,true));
        return sw.toString();
    }

}
