package cn.xiaochi.dao;

import cn.xiaochi.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer>, JpaSpecificationExecutor<ProductCategory> {

    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
