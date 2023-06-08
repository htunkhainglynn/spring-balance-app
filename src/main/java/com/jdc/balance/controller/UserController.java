package com.jdc.balance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.balance.model.domain.vo.UserVo;
import com.jdc.balance.model.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("search")  // get form submit
    public String search(@RequestParam(required = false) Boolean status, 
    					 @RequestParam(required = false) String name,
    					 @RequestParam(required = false) String phone,
    					 ModelMap model) {
		List<UserVo> userVo = userService.search(status, name, phone);
		model.addAttribute("list", userVo);
        return "users";
    }
	
	@GetMapping("status") 
	public String changeStatus(@RequestParam int id, 
    					 @RequestParam boolean status) {
		
		userService.changeStatus(id, !status);
        return "redirect:/admin/users";
    }
    
    @GetMapping
    public String users() {
    	return "users";
    }
    
    @GetMapping("delete/{id:\\d+}")
    public String delete(@PathVariable int id) {
		return "redirect:/admin/users";
    }
    
    @ModelAttribute("list")
    public List<UserVo> list() {
    	return userService.searchAll();
    }

}