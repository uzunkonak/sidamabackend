package com.siemens.sidama.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siemens.sidama.entity.User;

/**
 * @author FBG
 *
 */
/* this the user Repository interface */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findOneByUsername(String username);
}
