package cn.xiaochi.dto;

import cn.xiaochi.dataobject.OrderDetail;
import cn.xiaochi.enums.OrderStatusEnum;
import cn.xiaochi.enums.PayStatusEnum;
import cn.xiaochi.util.Date2LongSerializer;
import cn.xiaochi.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto {
    /** 订单id. */
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /** 创建时间. */
    // @JsonFormat(pattern="yyyy-MM-ddHH:mm:ss") 输出格式化日期，写在gett方法上
    // @DatetimeFormat（或 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")）是将String转换成Date，一般前台给后台传值时用，写在set方法上
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 更新时间. */
    // @JsonFormat(pattern="yyyy-MM-ddHH:mm:ss")
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    // 一个订单下的所有子订单
    List<OrderDetail> orderDetailList;

    // 返回一个枚举类型，用于前台展示订单支付状态的显示
    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum(){
        return EnumUtil.getByCode(payStatus,PayStatusEnum.class);
    }
}
