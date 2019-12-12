package cn.xiaochi.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/** jpa
 * 把数据库表映射成对象
 */
@Data
@Entity // 把数据库表映射成对象
@DynamicUpdate // 开启自动更新时间
public class ProductCategory {

    @Id // 主键
    @GeneratedValue // 递增
    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

}
