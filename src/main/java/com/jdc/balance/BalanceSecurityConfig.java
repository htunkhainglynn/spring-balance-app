package com.jdc.balance;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.jdc.balance.model.domain.entity.User.Role;

@EnableWebSecurity
public class BalanceSecurityConfig {

	@Bean
	SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
		
		http.formLogin(form -> form.loginPage("/signin").defaultSuccessUrl("/user/home"));
		http.logout(logout -> logout.logoutUrl("/signout").logoutSuccessUrl("/signin"));
		
		http.authorizeHttpRequests(auth -> auth
				.mvcMatchers("/signin", "/signup", "/home").permitAll()
				.mvcMatchers("/user/**").hasAnyAuthority(Role.Member.name(), Role.Admin.name())
				.mvcMatchers("/admin/**").hasAuthority(Role.Admin.name()).anyRequest().authenticated());
		
		http.exceptionHandling().accessDeniedPage("/denied");
		
		
		return http.build();
	}
	
	@Bean
	PasswordEncoder passWordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationEventPublisher authenticationEventpublisher() {
		return new DefaultAuthenticationEventPublisher();
		
	}
	
	@Bean  // for logout event activity
	HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
}
