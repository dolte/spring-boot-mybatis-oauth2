package com.dolte.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dolte.user.entity.User;

public interface UserService extends UserDetailsService {

	public User findByLoginId(String loginId);
	
}
