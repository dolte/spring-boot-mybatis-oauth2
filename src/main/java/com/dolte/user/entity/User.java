package com.dolte.user.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 사용자 계정 Entity
 * 
 * @author adnstyle
 * @history
 * 		adnstyle	2016.05.11 1차 완료	
 */
@SuppressWarnings("serial")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User implements UserDetails, Authentication {

	private long id;
	
	private String loginId;

	private String password;
	
	private String name;
	
	@SuppressWarnings("unused")
	private boolean enabled;
	
	private Date instDate;
	
	private Date updtDate;
	
	private Date loginDate;
	
	private String role;
	
	private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
	
	private Collection<String> agreements = new ArrayList<>();
	
	public User() {}
	
	public User(long id) { 
		this.id = id;
	}
	
	public User(String loginId) {
		this.loginId = loginId;
	}
	
	/**
	 * @return 사용자 일련번호
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return 사용자 아이디
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @Return 비밀번호
	 */
	public String getPassword() {
		return password;
	}

	public String getLoginId() {
		return loginId;
	}
	
	/**
	 * @return 사용자 이름
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return 가입일시
	 */
	public Date getInstDate() {
		return instDate;
	}

	public void setInstDate(Date instDate) {
		this.instDate = instDate;
	}

	/**
	 * @return 최종수정일시
	 */
	public Date getUpdtDate() {
		return updtDate;
	}

	public void setUpdtDate(Date updtDate) {
		this.updtDate = updtDate;
	}
	
	/**
	 * @return 최종로그인일시
	 */
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	/**
	 * @return 권한
	 */
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public Object getCredentials() {
		return this;
	}

	@Override
	public Object getDetails() {
		return this;
	}

	@Override
	public Object getPrincipal() {
		return this;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getUsername() {
		return loginId;
	}

	@Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            return loginId.equals(((User) o).loginId);
        }
        return false;
    }
	
	@Override
    public int hashCode() {
        return loginId.hashCode();
    }
}
