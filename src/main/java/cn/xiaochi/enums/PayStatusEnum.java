package cn.xiaochi.enums;


/**
 * 订单支付状态
 */
public enum PayStatusEnum implements BaseCodeEnum<Integer> {
    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功");

    private Integer code;
    private String message;

    PayStatusEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
