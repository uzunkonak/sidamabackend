package com.siemens.sidama.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siemens.sidama.entity.File;

/**
 * @author FBG
 *
 */
/* this the user Repository interface */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByUserId(Long userId);
}
