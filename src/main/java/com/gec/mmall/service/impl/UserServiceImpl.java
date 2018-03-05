package com.gec.mmall.service.impl;

import com.gec.mmall.common.Const;
import com.gec.mmall.common.ServerResponse;
import com.gec.mmall.dao.UserMapper;
import com.gec.mmall.pojo.User;
import com.gec.mmall.service.IUserService;
import com.gec.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUserName(username);
		if (resultCount == 0) {
			return ServerResponse.createByErrorMessage("用户名不存在");
		}

		//todo MD5登录密码

		User user = userMapper.selectLogin(username,password);
		if (user == null){
			return ServerResponse.createByErrorMessage("密码错误");
		}

		user.setPassword(StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功",user);
	}

	@Override
	public ServerResponse<String> register(User user) {
		//校验用户名
		ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}
		//校验email
		validResponse = checkValid(user.getEmail(),Const.EMAIL);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}
		//配置用户权限
		user.setRole(Const.Role.ROLE_CUSTMOER);
		//MD5加密密码
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		//持久化数据
		int resultCount = userMapper.insert(user);
		if (resultCount == 0){
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");

	}


	@Override
	public ServerResponse<String> checkValid(String str, String type) {
		if (StringUtils.isNotBlank(type)){
			//开始校验
			if (type.equals(Const.USERNAME)){
				int resultCount = userMapper.checkUserName(str);
				if (resultCount > 0){
					return ServerResponse.createByErrorMessage("用户名已存在");
				}
			}
			if (type.equals(Const.EMAIL)){
				int resultCount = userMapper.checkEmail(str);
				if (resultCount > 0){
					return ServerResponse.createByErrorMessage("email已存在");
				}
			}

		}else {
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccess("校验成功");
	}
}
