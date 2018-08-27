package com.siemens.sidama.repository;

import com.siemens.sidama.entity.Dataset;
import com.siemens.sidama.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    List<Dataset> findAllByUploadedBy_Id(Long userId);
}
