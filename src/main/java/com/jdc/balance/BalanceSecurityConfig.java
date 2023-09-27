package com.jdc.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.jdc.balance.model.domain.entity.User.Role;
import com.jdc.balance.security.AppUserDetailService;

@EnableWebSecurity
public class BalanceSecurityConfig {
	
	@Autowired
	AppUserDetailService userDetailService;

	@Bean
	SecurityFilterChain httpSecurity(
			HttpSecurity http, 
			PersistentTokenBasedRememberMeServices rememberMeServices,
			AuthenticationManager authenticationManager) throws Exception {
		
		http.formLogin(form -> form.loginPage("/signin").defaultSuccessUrl("/user/home"));
		http.logout(logout -> logout.logoutUrl("/signout").logoutSuccessUrl("/signin"));
		
		http.authenticationManager(authenticationManager);
		
		http.authorizeHttpRequests(auth -> auth
				.mvcMatchers("/signin", "/signup", "/home")
				.permitAll()
				.mvcMatchers("/user/**")
				.hasAnyAuthority(Role.Member.name(), Role.Admin.name())
				.mvcMatchers("/admin/**")
				.hasAuthority(Role.Admin.name())
				.anyRequest()
				.permitAll());
		
		http.exceptionHandling().accessDeniedPage("/signin");
		
		http.rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices));
		
		return http.build();
	}
	
	@Bean
	PersistentTokenBasedRememberMeServices rememberMeServices(AppUserDetailService userDetailsService) {
		return new PersistentTokenBasedRememberMeServices("SGVsbG8gSmF2YQ==", userDetailsService, new InMemoryTokenRepositoryImpl());
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
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
	
	@Bean
	SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}
	
	@Bean(name = "authenticationManager")
	AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		
		builder.authenticationProvider(programaticProvider(passwordEncoder));
		
		return builder.build();
	}
	
	AuthenticationProvider programaticProvider(PasswordEncoder passwordEncoder) {
		var provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userDetailService);
		return provider;
	}
}
