package com.jnu.commmon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.jnu.constants.ConfigConstants.FAIL;
import static com.jnu.constants.ConfigConstants.SUCCESS;

/**
 * 公共响应类
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static<T>  Result<T> success(){
        return new Result<>(SUCCESS,"success",null);
    }

    public static<T>  Result<T> success(T data){
        return new Result<>(SUCCESS,"success",data);
    }

    public static<T>  Result<T> success(T data, String message){
        return new Result<>(SUCCESS,message,data);
    }

    public static<T>  Result<T> success(String message){
        return new Result<>(SUCCESS,message,null);
    }

    public static<T>  Result<T> fail(){
        return new Result<>(FAIL,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code){
        return new Result<>(code,"fail",null);
    }

    public static<T>  Result<T> fail(Integer code, String message){
        return new Result<>(code,message,null);
    }

    public static<T>  Result<T> fail( String message){
        return new Result<>(FAIL,message,null);
    }
}