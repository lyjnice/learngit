package com.yanxiu.demo.service;

import com.yanxiu.demo.dao.UserDao;
import com.yanxiu.demo.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by mashiwei on 2016/8/22.
 */
@Service("userServiceImpl")
public class UserServiceImpl {
    private static final Log LOG = LogFactory.getLog(UserServiceImpl.class);

    @Resource(name = "userDao")
    public UserDao userDao;

    public User createUser(User user) {
        if(user.getPassport() == null || user.getPassword() == null) {
            return null;
        }
        return userDao.createUser(user);
    }

    public User queryUser(Long uid) {
        if(uid == null || uid <= 0) {
            return null;
        }

        return userDao.queryUser(uid);
    }

    public int updateUser(User user) {
        if(user.getUid() == null || user.getUid() <= 0) {
            return 0;
        }

        return userDao.updateUser(user);
    }

}
