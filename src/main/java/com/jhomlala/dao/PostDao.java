package com.jhomlala.dao;

import java.util.List;

import com.jhomlala.model.Post;

public interface PostDao {

	void insert(Post post);
	List <Post> getPosts(int count);
	List <Post> getPostsForPersonWithId(int id,int count);
	Post getPostWithId(int id);
}
