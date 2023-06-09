package com.jdc.balance.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class BalanceAppListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@EventListener
	void onSuccess(AuthenticationSuccessEvent event) {
		var time = LocalDateTime.ofInstant(
				new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
		var username = event.getAuthentication().getName();
		log.info("{} signed in at {}", username, time);
	}

	@EventListener
	void onFailuer(AbstractAuthenticationFailureEvent event) {
		var time = LocalDateTime.ofInstant(
				new Date(event.getTimestamp()).toInstant(), ZoneId.systemDefault());
		var username = event.getAuthentication().getName();
		log.info("{} failed to sign in at {}", username, time);
	}

}
