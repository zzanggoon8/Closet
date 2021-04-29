package com.ssu.project.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildCategoryRepository extends JpaRepository<ChildCategory, Long> {

    public ChildCategory findByname(String name);

}