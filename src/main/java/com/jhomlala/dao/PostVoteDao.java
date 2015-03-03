package com.jhomlala.dao;

import java.util.List;

import com.jhomlala.model.PostVote;

public interface PostVoteDao 
{
	void insert (PostVote postVote);
	void update (PostVote postVote);
	List <PostVote> get(int postid);
	
}
