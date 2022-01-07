package com.liangzhicheng.common.exception;

import com.liangzhicheng.common.constant.ApiConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 自定义事务异常
 * @author liangzhicheng
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TransactionException extends RuntimeException {

    private int code = ApiConstant.BASE_FAIL_CODE;
    private String message;

    public TransactionException(int code){
        this.code = code;
        this.message = ApiConstant.getMessage(code);
    }

    public TransactionException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public TransactionException(int code, String message, Throwable e) {
        super(message, e);
        this.code = code;
        this.message = message;
    }

    public TransactionException(String message) {
        super(message);
        this.message = message;
    }

    public TransactionException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

}
