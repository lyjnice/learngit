package com.yanxiu.demo.controller;

/**
 * Created by mashiwei on 2016/8/22.
 */
import com.yanxiu.demo.model.User;
import com.yanxiu.demo.service.UserServiceImpl;
import com.yanxiu.workselect.api.commons.enums.Result;
import com.yanxiu.workselect.api.commons.enums.ResultCode;
import com.yanxiu.workselect.api.commons.enums.ResultCodeEnum;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by mashiwei on 2016/4/27.
 */
@Controller
@RequestMapping("/userData/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/get",produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public Result getUserByUid (
            @RequestParam(value = "uid", required = false) Long uid) {
        if (uid == null || uid <= 0) {
            return new Result(ResultCodeEnum.PARAM_ERROR);
        }

        User user;
        Result result = new Result();

        try {
            user = userService.queryUser(uid);
            if(user != null) {
                result.setData(user);
                return result;
            } else {
                LOG.debug("redis key set, value=" + JSONObject.fromObject(user).toString());
                return new Result(ResultCodeEnum.PARAM_ERROR);
            }
        } catch (Exception e) {
            LOG.error("getUserByUid error: "+e.getMessage());
            return new Result(ResultCodeEnum.RESULT_ERROR);
        }
    }

    @RequestMapping(value = "/update",produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public Result updateUserByUid (
            @RequestParam(value = "uid", required = true) Long uid,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "idCard", required = false) String idCard) {
        if (uid == null || uid <= 0) {
            return new Result(ResultCodeEnum.RESULT_ERROR);
        }

        if(StringUtils.isEmpty(password) && StringUtils.isEmpty(mobile)
                && StringUtils.isEmpty(email) && StringUtils.isEmpty(idCard)){
            return new Result(ResultCodeEnum.PARAM_ERROR);
        }

        try {
            User user = new User();
            user.setUid(uid);
            user.setPassword(password);
            user.setMobile(mobile);
            user.setEmail(email);
            user.setPersonalId(idCard);
            int num = userService.updateUser(user);
            return new Result();
        } catch (Exception e) {
            LOG.error("updateUser", e);
            return new Result(ResultCodeEnum.RESULT_ERROR);
        }
    }
}
