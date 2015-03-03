package com.jhomlala.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table( name = "post", catalog = "test" )
public class Post {
	
	private int id;
	private int authorID;
	private String authorName;
	private String authorAvatarURL;
	private Timestamp createdDate;
	private int plusCount;
	private int minusCount;
	private int totalCount;
	private String message;
	private boolean visible;
	private List<Comment> commentList;
	
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, length = 11)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "authorID", nullable = false, length = 11)
	public int getAuthorID() {
		return authorID;
	}
	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}
	

	@Column(name = "authorName", nullable = false, length = 60)
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	@Column(name = "createdDate", nullable = false)
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "plusCount", nullable = false, length = 11)
	public int getPlusCount() {
		return plusCount;
	}
	public void setPlusCount(int plusCount) {
		this.plusCount = plusCount;
	}
	
	@Column(name = "minusCount", nullable = false, length = 11)
	public int getMinusCount() {
		return minusCount;
	}
	public void setMinusCount(int minusCount) {
		this.minusCount = minusCount;
	}
	@Column(name = "message", nullable = false, length = 2000)
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Column(name = "visible", nullable = false)
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	@Transient
	public String getAuthorAvatarURL() {
		return authorAvatarURL;
	}
	public void setAuthorAvatarURL(String authorAvatarURL) {
		this.authorAvatarURL = authorAvatarURL;
	}
	@Transient
	public List<Comment> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	@Transient
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	

}
