package com.oc.microservice.dao;

import com.oc.microservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    @Query("SELECT user FROM User user WHERE user.username like :username")
    User findUserByUsername(@Param("username") String username);
}
