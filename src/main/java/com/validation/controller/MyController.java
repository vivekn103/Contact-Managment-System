package com.validation.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.validation.entity.User;
import com.validation.message.Messages;
import com.validation.repository.UserRepository;



@Controller
public class MyController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/")
	public String home()
	{
		return "home";
	}
	@RequestMapping("/signup")
	public String signUp(Model model)
	{	
		model.addAttribute("title", "Sign-In");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, 
            @RequestParam(value = "agrement", defaultValue = "false") boolean agreement,
            Model model, HttpSession session){	
		try {
			
			if(bindingResult.hasErrors())
			{	
				model.addAttribute("user", user);
				System.out.println(bindingResult);
				return "signup";
			}
			if(!agreement)
			{
				System.out.println("You Have Not Ticked The Agrement Check Box");
				throw new Exception("You Have Not Ticked The Agrement Check Box");
			}
			
			
			user.setEnable(true);
			user.setImageUrl("default.png");
			user.setRole("ROLE_USER");
			user.setHashedPassword(passwordEncoder.encode(user.getPassword()));
//			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result=this.userRepository.save(user);
			model.addAttribute("user", result);
			session.setAttribute("message", new Messages("User Successfully Registered", "alert-success"));
			return "signup";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Messages("Somethimg Went Wrong!! "+e.getMessage(), "alert-error"));
			return "signup";
		}
	}
	}
