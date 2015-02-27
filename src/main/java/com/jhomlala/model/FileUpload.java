package com.jhomlala.model;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
 
public class FileUpload {
 
    private MultipartFile crunchifyFiles;

	public MultipartFile getCrunchifyFiles() {
		return crunchifyFiles;
	}

	public void setCrunchifyFiles(MultipartFile crunchifyFiles) {
		this.crunchifyFiles = crunchifyFiles;
	}
 

}