package com.dolte;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dolte.user.entity.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = "authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
		@Value("${security.oauth2.client.client-id}")
		private String clientId;
		
		@Value("${security.oauth2.client.client-secret}")
		private String clientSecret;
		
		@Autowired
		private AuthenticationManager auth;
		
		@Autowired
		private DataSource dataSource;
		
		@Autowired
        private UserDetailsService userDetailsService;
		
		@Bean
		public JdbcTokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}
		
		//access token 유효성 체크(/oauth/check_token 활성화)
		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception
		{
		   oauthServer.checkTokenAccess("isAuthenticated()");    
		}

		@Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                .authenticationManager(auth)
                .tokenServices(defaultTokenServices())
                .userDetailsService(userDetailsService)
                .addInterceptor(new HandlerInterceptorAdapter() {
                	
                	/**
                	 * "/oauth/token" 전처리
                	 * client_id가 없다면 token 발행 할 수 없음
                	 */
                	@Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
                		
                		return true;
                	}
                	
                	/**
                	 * "/oauth/token" 후처리 
                	 */
                	@Override
                	public void afterCompletion(
                			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
                			throws Exception {
                	}
                });
        }
		
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients
			.inMemory()
				.withClient(clientId)
					.authorizedGrantTypes("password", "refresh_token")
					.authorities("ROLE_USER")
					.scopes("read", "write")
					.accessTokenValiditySeconds(3600)
					.secret(clientSecret);
		}
		
		@Autowired
		private ClientDetailsService clientDetailsService;
		
		@Primary
        @Bean
        public AuthorizationServerTokenServices  defaultTokenServices(){
            final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore());
            defaultTokenServices.setClientDetailsService(clientDetailsService);
            defaultTokenServices.setTokenEnhancer(new CustomTokenEnhancer());
            defaultTokenServices.setSupportRefreshToken(true);
            return defaultTokenServices;
        }
        
		/*
		 * access token 발행시 함께 반환할 사용자 정보 정의
		 */
        private class CustomTokenEnhancer implements TokenEnhancer {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                
            	User user = (User) authentication.getPrincipal();
                final Map<String, Object> additionalInfo = new HashMap<>();

                additionalInfo.put("username", user.getName());
                
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

                return accessToken;
            }
        }
	}

	@Configuration
	@EnableResourceServer
	protected static class ResourceServer extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
			.authorizeRequests()
				.antMatchers("/resources/**").permitAll()
				.anyRequest().authenticated()
			.and()
			.csrf().disable();
		}
		
		@Bean
		public TokenStore JdbcTokenStore(DataSource dataSource) {
			return new JdbcTokenStore(dataSource);
		}
	}
}
