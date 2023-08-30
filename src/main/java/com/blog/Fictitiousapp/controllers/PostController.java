package com.blog.Fictitiousapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.Fictitiousapp.constants.AppConstants;
import com.blog.Fictitiousapp.payloads.ApiResponse;
import com.blog.Fictitiousapp.payloads.PostDto;
import com.blog.Fictitiousapp.payloads.PostResponse;
import com.blog.Fictitiousapp.service.PostService;

@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	private PostService postService;

	// create
	@PostMapping("user/{userId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId) {
		PostDto createPost = this.postService.createPost(postDto, userId);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);

	}

	// Update the post
	@PutMapping("user/{userId}/posts/{postId}")
	public ApiResponse updatePost(@RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer postId) {
		 String success = this.postService.updatePost(postDto, userId, postId);
		 
		 if(success.equalsIgnoreCase(AppConstants.NOT_AUTHORIZED)) {
			 return new ApiResponse("Sorry!! You are not authorized to make changes in this post", false);
		 }
		 
		 return new ApiResponse("Post is succesfully updated !!", true);
		
	}

	// delete Post
	@DeleteMapping("user/{userId}/posts/{postId}")
	public ApiResponse deeletePost(@PathVariable Integer userId, @PathVariable Integer postId) {
		String success = this.postService.deletePost(userId, postId);
		
		if(success.equalsIgnoreCase(AppConstants.NOT_AUTHORIZED)) {
			 return new ApiResponse("Sorry!! You are not authorized to delete this post", false);
		 }
		 
		 return new ApiResponse("Post is succesfully deleted !!", true);
	}

	// get all posts
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllpost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

}
