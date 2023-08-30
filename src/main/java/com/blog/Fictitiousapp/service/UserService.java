package com.blog.Fictitiousapp.service;

import com.blog.Fictitiousapp.payloads.UserDto;

public interface UserService {
	
	//Creating new user
	UserDto createUser(UserDto user);
}
