package com.validation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.validation.entity.User;
import com.validation.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userByUserName = userRepository.getUserByUserName(username);
		
		if(userByUserName==null)
		{
			throw new UsernameNotFoundException("Coudnot found User");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(userByUserName); 
		return customUserDetails;
	}

}
