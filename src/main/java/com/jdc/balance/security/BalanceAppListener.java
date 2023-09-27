package com.jdc.balance.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.jdc.balance.model.domain.entity.UserAccessLog;
import com.jdc.balance.model.domain.entity.UserAccessLog.Type;
import com.jdc.balance.model.repo.UserAccessRepo;

@Component
public class BalanceAppListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserAccessRepo repo;
	
	@EventListener
	@Transactional
	void onSuccess(AuthenticationSuccessEvent event) {
		var time = LocalDateTime.ofInstant(
				new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
		var username = event.getAuthentication().getName();
		log.info("{} signed in at {}", username, time);
		repo.save(new UserAccessLog(username, Type.SIGNIN, time));
	}

	@EventListener
	@Transactional
	void onFailuer(AbstractAuthenticationFailureEvent event) {
		var time = LocalDateTime.ofInstant(
				new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
		var username = event.getAuthentication().getName();
		log.info("{} failed to sign in at {}", username, time);
		repo.save(new UserAccessLog(username, Type.ERROR, time, event.getException().getMessage()));
	}
	
	@EventListener
	@Transactional
	void onSignOut(HttpSessionDestroyedEvent event) {
		event.getSecurityContexts().stream().findAny()
			.ifPresent(auth -> {
				var time = LocalDateTime.ofInstant(
						new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
				var username = auth.getAuthentication().getName();
				log.info("{} failed to sign in at {}", username, time);
				repo.save(new UserAccessLog(username, Type.SIGNOUT, time));
			});
		
	}

}
