package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    public User userLogin(UserLoginDTO userLoginDTO) {
//        call weChat interface to get user openId
        String openid = getOpenId(userLoginDTO.getCode());

//        see if openId is null, throw Exception
        if(openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

//        not null check if user is new, check back to DB, yes do nothing, no then add one(register automatically)
        User user = userMapper.getUser(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.addUser(user);
        }

//        return User
        return user;
    }

    public String getOpenId(String code) {
        Map<String,String> mp = new HashMap<>();
        mp.put("appid", weChatProperties.getAppid());
        mp.put("secret", weChatProperties.getSecret());
        mp.put("js_code", code);
        mp.put("grant_type", "authorization_code");

        String jsonStr = HttpClientUtil.doGet(WX_LOGIN, mp);  // goGet returns a json str

        JSONObject jsonObject = JSON.parseObject(jsonStr);  // transfer it to json object
        String openid = jsonObject.getString("openid");// get "openid" property
        return openid;
    }
}
