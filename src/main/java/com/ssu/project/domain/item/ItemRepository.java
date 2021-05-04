package com.ssu.project.domain.item;

import com.ssu.project.domain.category.ChildCategory;
import com.ssu.project.domain.category.ParentCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository <Item, Long> {
    @Query("SELECT i FROM Item i")
    List<Item> findItem(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1)")
    List<Item> findItemByParentCategory(String category, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1 or pc.name = ?2)")
    List<Item> findItemByParentCategory(String category_1, String category_2, Pageable pageable);

    @Query(value = "SELECT count(i) FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1)")
    Long countItemByParentCategory(String category);

    @Query(value = "SELECT count(i) FROM Item i WHERE i.parentCategory IN (SELECT pc.id FROM ParentCategory pc WHERE pc.name = ?1 or pc.name = ?2)")
    Long countItemByParentCategory(String category_1, String category_2);

    @Query(value = "select * from  Item e where e.Name like %:keyword% or e.Brand like %:keyword%", nativeQuery = true)
    List<Item> findByAllKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from  Item e where e.Name like %:keyword%", nativeQuery = true)
    List<Item> findByNameKeyword(@Param("keyword") String keyword);

    @Query(value = "select * from  Item e where e.Brand like %:keyword%", nativeQuery = true)
    List<Item> findByBrandKeyword(@Param("keyword") String keyword);

    List<Item> findAllByParentCategoryAndChildCategory(ParentCategory parent, ChildCategory child);
}
