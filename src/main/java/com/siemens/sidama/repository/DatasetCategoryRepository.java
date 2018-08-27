package com.siemens.sidama.repository;

import com.siemens.sidama.entity.DatasetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasetCategoryRepository extends JpaRepository<DatasetCategory, Long> {
}
