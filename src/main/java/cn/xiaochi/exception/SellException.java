package cn.xiaochi.exception;

/** 注意：
 * springBoot中的异常必须继承 RuntimeException，事务才能生效，继承 Exception 事务不生效
 */
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(String message){
        super(message);
    }

    public SellException(String message,Integer code){
        super(message);
        this.code = code;
    }
}
