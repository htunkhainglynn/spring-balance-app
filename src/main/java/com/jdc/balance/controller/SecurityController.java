package com.jdc.balance.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jdc.balance.model.domain.entity.User.Role;
import com.jdc.balance.model.domain.form.ChangePasswordForm;
import com.jdc.balance.model.domain.form.SignUpForm;
import com.jdc.balance.model.service.SignUpService;
import com.jdc.balance.model.service.UserService;

@Controller
@RequestMapping
public class SecurityController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	@Autowired
	SecurityContextRepository repository;

	@Autowired
	private SignUpService signUpService;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String index() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null && auth.getAuthorities().stream().anyMatch(
				a -> a.getAuthority().equals(Role.Admin.name()) || a.getAuthority().equals(Role.Member.name()))) {
			return "redirect:/user/home";
		}

		return "signin";
	}

	@PostMapping("signup")
	public String signUp(@ModelAttribute(name = "form") @Valid SignUpForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
		if (result.hasErrors()) {
			return "signup";
		}

		signUpService.signUp(form);

		// Authenticate
		var authentication = authenticationManager.authenticate(form.authentication());
		System.out.println("authentication: " + authentication);
		
		// Set Authentication Result to Security Context
		var securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);

		// Save Security Context to Security Context Repository
		repository.saveContext(securityContext, request, response);

		// Redirect to Saved Request
		var rediredtUrl = getSavedRequest(request, response).map(SavedRequest::getRedirectUrl).orElse("/");

		System.out.println("rediredtUrl: " + rediredtUrl);
		
		return "redirect:%s".formatted(rediredtUrl);

	}
	
	// sp3-04-52-About-Saved-Request
	private Optional<SavedRequest> getSavedRequest(HttpServletRequest request, HttpServletResponse response) {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		return Optional.ofNullable(savedRequest);
	}

	@GetMapping("signup")
	public String loadSignUp(Model model, @ModelAttribute("message") String message) {
		model.addAttribute("message", message);
		return "signup";
	}

	@PostMapping("user/changepass")
	public String changePassword(@ModelAttribute ChangePasswordForm form, RedirectAttributes redirect) {

		userService.changePassword(form);
		redirect.addFlashAttribute("message", "Your password has been changed successfully.");
		return "redirect:/";
	}

	@PostMapping("/signout")
	public String signOut() {
		SecurityContextHolder.clearContext();
		return "redirect:/";
	}


	@ModelAttribute(name = "form")
	SignUpForm signUpForm() {
		return new SignUpForm();
	}

}