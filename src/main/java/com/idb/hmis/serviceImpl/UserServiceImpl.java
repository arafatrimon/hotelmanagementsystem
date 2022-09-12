 package com.idb.hmis.serviceImpl;

import com.idb.hmis.dao.RoleDao;
import com.idb.hmis.dao.UserDao;
import com.idb.hmis.entity.Role;
import com.idb.hmis.entity.User;
import com.idb.hmis.service.UserService;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yasir Araphat
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String register(User user, String roleName) {
        String status = this.alreadyExists(user);
        if (status != null) {
            return status;
        }
        this.setUnhandledValues(user, roleName);
        this.userDao.save(user);
        return "1";
    }

//    @Override
//    public User login(User user) {
//        String rawPassword = user.getPassword();
//        user = userDao.findByUsernameOrEmail(user.getUsername(), user.getEmail());
//        boolean isValidPassword = bCryptPasswordEncoder.matches(rawPassword, user.getPassword());
//        return (isValidPassword) ? user : null;
//    }
    @Override
    public String update(User newUser, String oldRawPassword) {
        User oldUser = this.userDao.findByUsername(this.getUsername());
        String email = newUser.getEmail();
        if (email != null && !email.isEmpty() && !email.equals(oldUser.getEmail())) {
            boolean emailExists = this.userDao.existsByEmail(email);
            if (emailExists) {
                return "00";
            }
            oldUser.setEmail(email);
        }
        String password = newUser.getPassword();
        if (password != null && !password.isEmpty()) {
            boolean validPassowrd = bCryptPasswordEncoder.matches(oldRawPassword, oldUser.getPassword());
            if (validPassowrd) {
                oldUser.setPassword(bCryptPasswordEncoder.encode(password));
            } else {
                return "01";
            }
        }
        this.userDao.save(oldUser);
        return "11";
    }

    public void setUnhandledValues(User user, String roleName) {
        String vCode = (int) (Math.random() * 1000000) + "";
        Set<Role> roles = this.getRolesByName(roleName);
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, 3);
        Date expDate = c.getTime();
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        user.setId(null);
        user.setPassword(password);
        user.setRoles(roles);
        user.setVerifyCode(vCode);
        user.setCodeExpDate(expDate);
        if (roleName.equals("ADMIN")) {
            user.setIsVerified(true);// Requires email verification if false
        } else {
            user.setIsVerified(true);
        }
    }

    @Override
    public User findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    @Override
    public Integer delete(String username) {
        return this.userDao.deleteByUsername(username);
    }

    @Override
    public Integer deactivate(String username) {
        return this.userDao.deactivate(username);
    }

    @Override
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String alreadyExists(User user) {
        if (this.userDao.existsByUsername(user.getUsername())) {
            return "Failed to register. Username already exists";
        }
        if (this.userDao.existsByEmail(user.getEmail())) {
            return "Failed to register. Email already used";
        }
        return null;
    }

    private Set<Role> getRolesByName(String roleName) {
        Set<Role> roles = this.roleDao.findByName(roleName);
        if (roles.isEmpty()) {
            Role role = this.roleDao.save(new Role(roleName));
            return (role != null) ? this.getRolesByName(roleName) : null;
        }
        return roles;
    }

}
