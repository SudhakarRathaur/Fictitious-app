package com.blog.Fictitiousapp.service;

import com.blog.Fictitiousapp.payloads.PostDto;
import com.blog.Fictitiousapp.payloads.PostResponse;

public interface PostService {

	//create
	PostDto createPost(PostDto postDto, Integer userId);
	
	//update
	String updatePost(PostDto postDto, Integer userId, Integer postId);
	
	//delete
	String deletePost(Integer userId, Integer postId);
	
	//get all post
	PostResponse getAllPost(Integer  pageNumber, Integer pageSize, String sortBy, String sortDir);
	
}

