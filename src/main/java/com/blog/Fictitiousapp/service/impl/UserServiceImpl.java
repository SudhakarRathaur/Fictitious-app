package com.blog.Fictitiousapp.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.Fictitiousapp.entities.User;
import com.blog.Fictitiousapp.payloads.UserDto;
import com.blog.Fictitiousapp.repositories.UserRepo;
import com.blog.Fictitiousapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;


	@Override
	public UserDto createUser(UserDto userDto) {
		
		// Using DTO's to avoid direct interaction with database
		User user = this.dtoToUser(userDto);
		
		// Svaing user to database
		User savedUser = this.userRepo.save(user);
		
		log.info("succesfully created the user with id {}", savedUser.getId());
		return this.usertoDto(savedUser);
	}

	private User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);

		return user;

	}

	private UserDto usertoDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);

		return userDto;
	}
}
