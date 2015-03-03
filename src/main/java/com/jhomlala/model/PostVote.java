package com.jhomlala.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "post_vote", catalog = "test" )
public class PostVote 
{
	private int postVoteId;
	private int postId;
	private int personId;
	private int voteValue;
	
	@Id
	@GeneratedValue
	@Column(name = "postVoteId", unique = true, nullable = false, length = 11)
	public int getPostVoteId() {
		return postVoteId;
	}
	public void setPostVoteId(int postVoteId) {
		this.postVoteId = postVoteId;
	}
	@Column(name = "postId", nullable = false, length = 11)
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	@Column(name = "personId", nullable = false, length = 11)
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	@Column(name = "voteValue", nullable = false, length = 11)
	public int getVoteValue() {
		return voteValue;
	}
	public void setVoteValue(int voteValue) {
		this.voteValue = voteValue;
	}
	
	
}
