package cn.xiaochi.exception;


import cn.xiaochi.util.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(Exception.class)
    public Response handle(Exception e){
        return Response.error(e.getMessage());
    }
}
