package com.idb.hmis.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.idb.hmis.entity.User;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username or u.email = :email")
    User authUser(
            @Param("username") String username,
            @Param("email") String email
    );

    @Transactional
    @Modifying
    Integer deleteByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isVerified = false, u.verifyCode = null, u.codeExpDate = null WHERE u.username = ?1")
    Integer deactivate(String username);
}
