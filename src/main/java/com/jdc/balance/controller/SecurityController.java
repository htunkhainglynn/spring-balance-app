package com.jdc.balance.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jdc.balance.model.domain.entity.User.Role;
import com.jdc.balance.model.domain.form.SignUpForm;
import com.jdc.balance.model.service.SignUpService;

@Controller
@RequestMapping
public class SecurityController {
	
	@Autowired
	private SignUpService signUpService;
	
	@GetMapping("/")
	public String index() {
		var auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null && auth.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority()
								.equals(Role.Admin.name()) 
								|| a.getAuthority()
								.equals(Role.Member.name()))) {
			return "redirect:/user/home";
		}
		
		return "signin";
	}

	@PostMapping("signup")
    public String signUp(@ModelAttribute(name="form") @Valid SignUpForm form, BindingResult result) {
		if (result.hasErrors()) {
			return "signup";
		}
		
		signUpService.signUp(form);
        return "redirect:/";
    }
	
	@GetMapping("signup")
	public void loadSignUp() {}

    @PostMapping("user/changepass")
    public String changePassword() {
        return "redirect:/";
    }
    
    @ModelAttribute(name = "form")
    SignUpForm signUpForm() {
    	return new SignUpForm();
    }

}