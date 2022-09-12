package com.idb.hmis.service;

import com.idb.hmis.entity.User;

/**
 *
 * @author Yasir Araphat
 */
public interface UserService {

    String register(User user, String roleName);

    String update(User newUser, String oldPassword);

//    User login(User user);

    User findByUsername(String username);

    String getUsername();

    Integer delete(String username);

    Integer deactivate(String username);

}
