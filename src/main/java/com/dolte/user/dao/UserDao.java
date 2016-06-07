package com.dolte.user.dao;

import org.springframework.stereotype.Repository;

import com.dolte.user.entity.Authority;
import com.dolte.user.entity.User;

@Repository("userDao")
public interface UserDao {
	/**
	 *  사용자 조회
	 * @param loginId 로그인아이디
	 * @return
	 */
	public User findByLoginId(String loginId);
	
	/**
	 * 사용자 ROLE 조회
	 * @param userId
	 * @return
	 */
	public Authority findAuthority(long userId);
	
}
