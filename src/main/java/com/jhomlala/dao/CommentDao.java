package com.jhomlala.dao;

import java.util.List;

import com.jhomlala.model.Comment;

public interface CommentDao {
	void insert(Comment comment);
	List<Comment> getCommentsForPost(int postID);
}
