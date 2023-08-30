package com.blog.Fictitiousapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.Fictitiousapp.entities.Post;

public interface PostRepo extends JpaRepository<Post, Integer>{

}
