package cn.xiaochi.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import java.util.Map;

@Data
public class OrderForm {

    @NotBlank(message = "姓名必填")
    private String name;

    @NotBlank(message = "手机号必填")
    private String phone;

    @NotBlank(message = "地址必填")
    private String address;

    @NotBlank(message = "openid必填")
    private String openid;

    @NotBlank(message = "购物车不能为空")
//    private List<Map<String,String>> items;
    private String items;
}
