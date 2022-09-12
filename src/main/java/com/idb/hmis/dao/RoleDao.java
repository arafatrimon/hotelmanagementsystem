package com.idb.hmis.dao;


import com.idb.hmis.entity.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long>{
    Set<Role> findByName(String name);
}
