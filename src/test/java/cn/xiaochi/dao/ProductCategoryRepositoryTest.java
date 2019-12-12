package cn.xiaochi.dao;


import cn.xiaochi.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOne(){
        ProductCategory one = productCategoryRepository.findOne(1);
        System.out.println(one);
    }

    @Test
    public void testFindByCategoryTypeIn(){
        List list = new ArrayList();
        list.add(5);
        List categorys = productCategoryRepository.findByCategoryTypeIn(list);
        categorys.forEach(System.out::println);
    }

    // 查询 id > 1 并且进行升序且分页的数据
    @Test
    public void pageFindAll(){
        // 注意：这里的排序字段要写成映射对象的属性
        Sort sort = new Sort(Sort.Direction.ASC,"categoryId");
        // jpa page分页是从index 0开始，显示两条
        Pageable pageRequest = new PageRequest(0,2,sort);

        // 条件查询   内部类  查询 id > 1 的
        Specification<ProductCategory> specification = new Specification<ProductCategory>() {
            /**
             * @param root  要查询的映射对象
             * @param query 添加查询条件
             * @param cb 构建 Predicate
             * @return
             */
            @Override
            public Predicate toPredicate(Root<ProductCategory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path id = root.get("categoryId");
                return cb.gt(id,1);
            }
        };

        // 查询
        Page<ProductCategory> page = productCategoryRepository.findAll(specification,pageRequest);

        System.out.println("总页数：" + page.getTotalPages());
        System.out.println("总条数：" + page.getTotalElements());
        System.out.println("当前页：" + (page.getNumber()+1));// 从0页开始，+1
        System.out.println("当前页数据：" + page.getContent());
        System.out.println("当前页总条数：" + page.getNumberOfElements());

        // 打印分页数据
        page.getContent().forEach(System.out::println);
    }

    @Test
    public void add(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("小二1111");
        productCategory.setCategoryType(5);
        System.out.println(productCategoryRepository.save(productCategory));
    }

    @Test
    public void edit(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(2);
        productCategory.setCategoryName("小二1111");
        productCategory.setCategoryType(5);
        System.out.println(productCategoryRepository.save(productCategory));
    }

}
