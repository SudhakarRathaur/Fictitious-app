package com.blog.Fictitiousapp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blog.Fictitiousapp.constants.AppConstants;
import com.blog.Fictitiousapp.entities.Post;
import com.blog.Fictitiousapp.entities.User;
import com.blog.Fictitiousapp.exception.ResourceNotFoundException;
import com.blog.Fictitiousapp.payloads.PostDto;
import com.blog.Fictitiousapp.payloads.PostResponse;
import com.blog.Fictitiousapp.repositories.PostRepo;
import com.blog.Fictitiousapp.repositories.UserRepo;
import com.blog.Fictitiousapp.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User ", " UserId", userId));
		
		// Using DTO's to avoid direct interaction with database
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setAddedDate(new Date());
		post.setUser(user);
		// Saving post in the database
		Post newPost = this.postRepo.save(post);
		int id = newPost.getPostId();
		log.info("Post with {} is saved in the database", id);

		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public String updatePost(PostDto postDto, Integer userId, Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", " Id ", postId));

		User author = post.getUser();
		
		// cheking if original author is same
		if (author.getId() != userId) {
			log.info("Author is not same for post with {}, so it can not be updated", postId);
			return AppConstants.NOT_AUTHORIZED;
		}

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());

		this.postRepo.save(post);
		
		log.info("Post with {} is updated in the database", postId);

		return AppConstants.SUCCESS;
	}

	@Override
	public String deletePost(Integer userId, Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " Id ", postId));

		User author = post.getUser();
		
		// checking if original author is same
		if (author.getId() != userId) {
			log.info("Author is not same for post with {}, so it can not be deleted", postId);
			return AppConstants.NOT_AUTHORIZED;
		}

		this.postRepo.delete(post);
		log.info("Post with {} is deleted in the database", postId);
		return AppConstants.SUCCESS;

	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		
		// Implementing pagination and sorting
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Post> pagePost = this.postRepo.findAll(p);
		
		// List of all post
		List<Post> allPosts = pagePost.getContent();
		List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		log.info("Successfully fetched all the post");
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setLastPage(pagePost.isLast());

		return postResponse;
	}
}
