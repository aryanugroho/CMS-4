package com.jhomlala.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jhomlala.dao.CommentDao;
import com.jhomlala.dao.PostDao;
import com.jhomlala.model.Comment;
import com.jhomlala.model.Person;
import com.jhomlala.model.Post;

@Service
public class PostService {
	  @Autowired
	    private PostDao postDao;
	    
	    @Autowired
	    private CommentDao commentDao;
	    
	    @Autowired
	    private PersonService personService;
	
	public Post getPostWithId(int id)
	{
		if (id>0)
		{
			Post post = postDao.getPostWithId(id);
			if (post != null)
			{
				loadAvatar(post);
				loadCommentsForPost(post);
				countTotalCount(post);
				
			}
			return post;
		}
		else
			throw new IllegalArgumentException("ID must be greather than 0");
	}
    
	private void loadCommentsForPost(Post post) 
	{
		List <Comment> commentList = commentDao.getCommentsForPost(post.getId());
		for (Comment comment:commentList)
		{
			loadAvatar(comment);
			countTotalCount(comment);
		}
			
		post.setCommentList(commentList);
		
	}
	private void countTotalCount(Post post)
	{
		post.setTotalCount(post.getPlusCount()-post.getMinusCount());
		System.out.println(post.getTotalCount());
	}
	
	private void countTotalCount(Comment comment)
	{
		comment.setTotalCount(comment.getPlusCount()-comment.getMinusCount());
	}

	private Post loadAvatar(Post post)
	{
		
		Person postAuthor = personService.findById(post.getAuthorID());
		String postAuthorAvatar = postAuthor.isAvatarSet() ? "av_"+ postAuthor.getId() + ".jpg" 
				: "av_0.jpg";
		post.setAuthorAvatarURL(postAuthorAvatar);
		return post;
	}
	private Comment loadAvatar(Comment comment)
	{
		Person postAuthor = personService.findById(comment.getAuthorID());
		String postAuthorAvatar = postAuthor.isAvatarSet() ? "av_"+ postAuthor.getId() + ".jpg" 
				: "av_0.jpg";
		comment.setAuthorAvatarURL(postAuthorAvatar);
		return comment;
	}
	public List<Post> loadPosts(int i) {
		List <Post> postList = postDao.getPosts(i);
		for (Post post: postList)
		{
			loadAvatar(post);
			countTotalCount(post); 
		}
		
		return postList;
	}
	
	public List<Post> getPostsForPersonWithId(int id,int count)
	{
		return postDao.getPostsForPersonWithId(id, count);
	}
	


	private Post constructPost(String message)
	{
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String name = auth.getName(); //get logged in username
	    Person author = personService.findByLogin(name);

	    
	    Post post = new Post();
	    post.setId(0);
	    post.setAuthorID(author.getId());
		post.setAuthorName(author.getVisibleName());
		post.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		post.setMessage(message);
		post.setMinusCount(0);
		post.setPlusCount(0);
		post.setVisible(true);
		
		return post;
		
	}

	public List<String> addPost(String message) 
	{
		Post post = constructPost(message);
		List <String> errorList = checkPost(post);
		if (errorList.size()>0)
			return errorList;
		else
		{
			postDao.insert(post);
		    Person author = personService.findById(post.getAuthorID());
		    personService.setLastPostTime(author); 
			return errorList;
		}
	}
	
	

	private List<String> checkPost(Post post)
	{
		List <String> errorList = new ArrayList<String>();
		if (!checkMessageLength(post.getMessage()))
			errorList.add("Message must contains 1 to 2000 characters.");
		if (checkPersonPostLimit(personService.findById(post.getAuthorID())))
			errorList.add("Please wait 30 minutes before you post another message.");
		return errorList;
	}
	private boolean checkPersonPostLimit(Person person)
	{
		Timestamp personTimestamp = person.getLastPostTime();
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis()-1800000);

		if (currentTimestamp.compareTo(personTimestamp)<=0 )
			return true;
		else
			return false;
		
	}
	
	
	 private boolean checkMessageLength(String message)
	 {
		 return (message.length() > 0 && message.length() <=2000);
	 }
	
	 public List<String> addComment(String id, String message) {
			List<String> errorList = new ArrayList<String>();
			if (checkStringLength(message,2000))
			{
				int idInt = Integer.parseInt(id);
				Post post = postDao.getPostWithId(idInt);
				if (post !=null)
				{
					if (post.isVisible())
					{
						Person person = getPersonFromSession();
						Comment comment = new Comment();
						comment.setAuthorID(person.getId());
						comment.setAuthorName(person.getVisibleName());
						comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
						comment.setMessage(message);
						comment.setMinusCount(0);
						comment.setPlusCount(0);
						comment.setPostID(post.getId());
						comment.setVisible(true);
						System.out.println(comment.getMessage());
						commentDao.insert(comment);
						
					}
					else
					{
						errorList.add("Post is not available!");
					}
				
				}
				else
				{
					errorList.add("Post is not available!");
				}
				
			}
			else
			{
				errorList.add("Please add message (1-2000 characters).");
			}
			
			return errorList;
		}
 
		
		public boolean checkStringLength(String string, int maxLength)
		{
			return string.length() > 0 && string.length()  <= maxLength;
		}
	
	    public Person getPersonFromSession()
	    {
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String name = auth.getName(); //get logged in username
		    Person uploader = personService.findByLogin(name);
		    return uploader;
	    }
	    
}
