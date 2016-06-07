package com.dolte.user.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dolte.user.dao.UserDao;
import com.dolte.user.entity.Authority;
import com.dolte.user.entity.User;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		User user = dao.findByLoginId(loginId);
		if (user == null) {
			throw new UsernameNotFoundException(loginId);
		}

		Authority authority = dao.findAuthority(user.getId());

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
		user.setAuthorities(authorities);

		return user;
	}

	@Override
	public User findByLoginId(String loginId) {
		return dao.findByLoginId(loginId);
	}

}
