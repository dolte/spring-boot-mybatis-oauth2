package com.dolte.user.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * 사용자 권한 Entity
 * 
 * @author adnstyle
 * @history
 * 		adnstyle	2016.05.11 1차 완료	
 */
@SuppressWarnings("serial")
public class Authority implements GrantedAuthority {
	private long userId;

	private String authority;

	/**
	 * @return 사용자 일련번호
	 */
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return 사용자 권한코드
	 */
	public String getAuthority() {
		return this.authority;
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
