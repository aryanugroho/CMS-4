package com.jhomlala.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jhomlala.dao.CommentDao;
import com.jhomlala.dao.PersonDao;
import com.jhomlala.dao.PostDao;
import com.jhomlala.model.Comment;
import com.jhomlala.model.FileUpload;
import com.jhomlala.model.Person;
import com.jhomlala.model.Post;

@Service
public class DashboardService 
{
  
    @Autowired
    private PostDao postDao;
    
    @Autowired
    private CommentDao commentDao;
    
    @Autowired
    private PersonService personService;
    
    private String saveDirectory = "C:/Users/Kuba/Desktop/spring-security-hibernate-2annotation"
    		+ "/src/main/webapp/resources/avatars/";
    
	public Post getPostWithId(int id)
	{
		if (id>0)
		{
			Post post = postDao.getPostWithId(id);
			loadAvatarForPost(post);
			loadCommentsForPost(post);
			return post;
		}
		else
			throw new IllegalArgumentException("ID must be greather than 0");
	}
    
	private void loadCommentsForPost(Post post) 
	{
		List <Comment> commentList = commentDao.getCommentsForPost(post.getId());
		for (Comment comment:commentList)
			loadAvatarForComment(comment);
		post.setCommentList(commentList);
		
	}

	private Post loadAvatarForPost(Post post)
	{
		Person postAuthor = personService.findById(post.getAuthorID());
		String postAuthorAvatar = postAuthor.isAvatarSet() ? "av_"+ postAuthor.getId() + ".jpg" 
				: "av_0.jpg";
		post.setAuthorAvatarURL(postAuthorAvatar);
		return post;
	}
	private Comment loadAvatarForComment(Comment comment)
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
			loadAvatarForPost(post);
		
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

	public List<String> addAvatar(FileUpload uploadForm)
	{
		MultipartFile file = uploadForm.getCrunchifyFiles();
        List <String> errorList = fileAvatarTransferCheck(file);
        if (errorList.size() == 0 )
        {
        	Person uploader = getPersonFromSession();
        	addFileToDisk(file,uploader);
        	if (getFileFormat(file.getOriginalFilename()).equals("png"))
        	{
        		changeFormat(saveDirectory,"av_"+uploader.getId());
        	}
        	uploader.setAvatarSet(true);
        	personService.update(uploader);
        }

		
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
	
	  public List<String> fileAvatarTransferCheck(MultipartFile file)
	    {
	    	List <String> errorList = new ArrayList<String>();
	    	if (checkIsFileUploaded(file))
	    	{
	    		if (checkFileSize(file))
	    		{
	    			if (checkFileFormat(file.getOriginalFilename()))
	    			{
	    				if (checkFileDimensions(file))
	    				{ }
	    				else
	    					errorList.add("Wrong file dimensions. File height and width must be between 50 and 100 px.");
	    			}
	    			else
	    			{
	    				errorList.add("Wrong file format.You can upload only .png and .jpg");
	    			}
	    			
	    		}
	    		else
	    		{
	    			errorList.add("Maximal file size is 10 KB.");
	    		}
	    		
	    	}
	    	else
	    	{
	    		errorList.add("Please upload file.");
	    	}

	    	return errorList;
	    }
	    
	    private void addFileToDisk(MultipartFile file, Person uploader) 
	    {
	    	
	    	try {
				file.transferTo(new File(saveDirectory+"av_"+uploader.getId()+"."+
						getFileFormat(file.getOriginalFilename())));
			} catch (IllegalStateException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			} 
			
		}



		public boolean checkIsFileUploaded(MultipartFile file)
	    {
	    	if (file != null )
	    		return true;
	    	else
	    		return false;
	    }
	    
	    public boolean checkFileSize(MultipartFile file)
	    {
	    	if (file.getSize()<=102400)
	    		return true;
	    	else
	    		return false;
	    }
	    
	    
	    public boolean checkFileFormat(String fileName)
	    {
	    	String fileFormat = getFileFormat(fileName);
	    	if (fileFormat.equals("png") || fileFormat.equals("jpg"))
	    	{
	    		return true;
	    	}
	    	else
	    		return false;
	    	
	    }
	    
	    public String getFileFormat(String fileName)
	    {
	    	System.out.println(fileName);
	    	return fileName.substring(fileName.length()-3,fileName.length());
	    }
	    
	    public boolean checkFileDimensions(MultipartFile file)
	    {
	    	BufferedImage bimg;
			try {
		
				bimg = ImageIO.read(file.getInputStream());
				int width          = bimg.getWidth();
		    	int height         = bimg.getHeight();
		    	
		    	if (width>=50 && width<=100)
		    	{
		    		if(height>=50 && height<=100)
		    			return true;
		    	}
		    	
			} catch (Exception e) {
				return false;
			}
	    	return false;
	    }
	    
	    public Person getPersonFromSession()
	    {
	    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    String name = auth.getName(); //get logged in username
		    Person uploader = personService.findByLogin(name);
		    return uploader;
	    }
	    
	    public void changeFormat(String filePath,String fileName)
	    {
	    	BufferedImage bufferedImage;
	    	 
	    	try {
	     
	    	  //read image file
	    	  File imageFile = new File(filePath+"/"+fileName+".png");
	    	  bufferedImage = ImageIO.read(new File(filePath+"/"+fileName+".png"));
	     
	    	  // create a blank, RGB, same width and height, and a white background
	    	  BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
	    			bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
	    	  newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
	     
	    	  // write to jpeg file
	    	  ImageIO.write(newBufferedImage, "jpg", new File(filePath+"/"+fileName+".jpg"));
	    	  imageFile.delete();
	    	
		    } catch (IOException e) {
		    	 
		  	  e.printStackTrace();
		   
		  	}
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
	    
    
    
}
