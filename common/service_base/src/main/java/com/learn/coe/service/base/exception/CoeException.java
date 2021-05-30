package com.learn.coe.service.base.exception;

import com.learn.coe.common.base.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author coffee
 * @Date 2021-05-15 17:14
 */
@Data
public class CoeException extends RuntimeException{

    private Integer code;

    public CoeException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public CoeException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "CoeException{" +
                "code=" + code +
                ",message" + this.getMessage() +
                '}';
    }
}
