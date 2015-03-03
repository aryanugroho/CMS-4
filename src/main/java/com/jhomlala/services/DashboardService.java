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
public class DashboardService {

	@Autowired
	private PostDao postDao;

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private PersonService personService;

	private String saveDirectory = "C:/Users/Kuba/Desktop/spring-security-hibernate-2annotation"
			+ "/src/main/webapp/resources/avatars/";

	public List<String> addAvatar(FileUpload uploadForm) {
		MultipartFile file = uploadForm.getCrunchifyFiles();
		List<String> errorList = fileAvatarTransferCheck(file);
		if (errorList.size() == 0) {
			Person uploader = getPersonFromSession();
			addFileToDisk(file, uploader);
			if (getFileFormat(file.getOriginalFilename()).equals("png")) {
				changeFormat(saveDirectory, "av_" + uploader.getId());
			}
			uploader.setAvatarSet(true);
			personService.update(uploader);
		}

		return errorList;

	}

	private boolean checkMessageLength(String message) {
		return (message.length() > 0 && message.length() <= 2000);
	}

	public List<String> fileAvatarTransferCheck(MultipartFile file) {
		List<String> errorList = new ArrayList<String>();
		if (checkIsFileUploaded(file)) {
			if (checkFileSize(file)) {
				if (checkFileFormat(file.getOriginalFilename())) {
					if (checkFileDimensions(file)) {
					} else
						errorList
								.add("Wrong file dimensions. File height and width must be between 50 and 100 px.");
				} else {
					errorList
							.add("Wrong file format.You can upload only .png and .jpg");
				}

			} else {
				errorList.add("Maximal file size is 10 KB.");
			}

		} else {
			errorList.add("Please upload file.");
		}

		return errorList;
	}

	private void addFileToDisk(MultipartFile file, Person uploader) {

		try {
			file.transferTo(new File(saveDirectory + "av_" + uploader.getId()
					+ "." + getFileFormat(file.getOriginalFilename())));
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public boolean checkIsFileUploaded(MultipartFile file) {
		if (file != null)
			return true;
		else
			return false;
	}

	public boolean checkFileSize(MultipartFile file) {
		if (file.getSize() <= 102400)
			return true;
		else
			return false;
	}

	public boolean checkFileFormat(String fileName) {
		String fileFormat = getFileFormat(fileName);
		if (fileFormat.equals("png") || fileFormat.equals("jpg")) {
			return true;
		} else
			return false;

	}

	public String getFileFormat(String fileName) {
		System.out.println(fileName);
		return fileName.substring(fileName.length() - 3, fileName.length());
	}

	public boolean checkFileDimensions(MultipartFile file) {
		BufferedImage bimg;
		try {

			bimg = ImageIO.read(file.getInputStream());
			int width = bimg.getWidth();
			int height = bimg.getHeight();

			if (width >= 50 && width <= 100) {
				if (height >= 50 && height <= 100)
					return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public Person getPersonFromSession() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String name = auth.getName(); // get logged in username
		Person uploader = personService.findByLogin(name);
		return uploader;
	}

	public void changeFormat(String filePath, String fileName) {
		BufferedImage bufferedImage;

		try {

			// read image file
			File imageFile = new File(filePath + "/" + fileName + ".png");
			bufferedImage = ImageIO.read(new File(filePath + "/" + fileName
					+ ".png"));

			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(
					bufferedImage.getWidth(), bufferedImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
					Color.WHITE, null);

			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File(filePath + "/"
					+ fileName + ".jpg"));
			imageFile.delete();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

}
