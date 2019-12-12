package cn.xiaochi.util;


import cn.xiaochi.enums.BaseCodeEnum;

/** 作用：
 * 返回一个枚举类型，用于前台展示订单支付状态的显示
 */
public class EnumUtil {

    /**
     * 返回一个枚举类型，用于前台展示订单支付状态的显示
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends BaseCodeEnum>T getByCode(Integer code, Class<T> enumClass){
        // enumClass.getEnumConstants() 返回一个枚举所有属性的数组
        for (T each : enumClass.getEnumConstants()){
            if (code.equals(each.getCode())){
                return each;
            }
        }
        return null;
    }
}
