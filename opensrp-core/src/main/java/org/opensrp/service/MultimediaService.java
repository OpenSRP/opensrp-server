package org.opensrp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;

import org.opensrp.domain.Multimedia;
import org.opensrp.repository.MultimediaRepository;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MultimediaService {

	private final MultimediaRepository multimediaRepository;
	private String multimediaDirPath;

	@Autowired
	public MultimediaService(MultimediaRepository multimediaRepository, @Value("#{opensrp['multimedia.directory.name']}") String baseMultimediaDirPath) {
		this.multimediaRepository = multimediaRepository;
	}
	
	public String saveMultimediaFile(String filePath, Multimedia multimedia) throws FileNotFoundException
	{
		  
		  FileInputStream fis = new FileInputStream(filePath);
		  String ext = FilenameUtils.getExtension(filePath);
		  String name = FilenameUtils.getBaseName(filePath);
		  multimedia.setExtension(ext);
		  multimedia.setName(name);
		  MultipartFile multipartFile = null;
          
		try {
			multipartFile = new MockMultipartFile("file", fis);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return saveMultimediaFile(multimedia,multipartFile);
	}
	
	public String saveMultimediaFile(Multimedia multimedia, MultipartFile file) {
		
		boolean uploadStatus = uploadFile(multimedia, file);

		if (uploadStatus) {
			try {
				Multimedia multimediaFile = new Multimedia()
						.withBaseEntityId(multimedia.getBaseEntityId())
						.withProviderId(multimedia.getProviderId())
						.withContentType(multimedia.getContentType())
						.withFilePath(multimediaDirPath)
						.withFileCategory(multimedia.getFileCategory())
						.withFileName(multimedia.getName())
						.withFileSize(multimedia.getFileSize())
						.withUploadDate(multimedia.getUploadDate())
						.withDescription(multimedia.getDescription());
				
				multimediaRepository.add(multimediaFile);

				return "success";

			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.getMessage();
			}
		}

		return "fail";

	}

	public List<Multimedia> getMultimediaData(int pageSize) throws JSONException
	{
		List<Multimedia> data=multimediaRepository.all(pageSize);
		return data;
	}
	
	public List<Multimedia> getMultimediaFiles(String providerId) {
		return multimediaRepository.all(providerId,50);
	}
	
	public List<Multimedia> getMultimediaDataByName(int pageSize, String searchByName) throws JSONException
	{
		List<Multimedia> data=multimediaRepository.allByName(pageSize,searchByName);
		return data;
	}
	
	public List<Multimedia> getMultimediaDataByContent(int pageSize, String searchByContent ) throws JSONException
	{
		List<Multimedia> data=multimediaRepository.allByContent(pageSize,searchByContent);
		return data;
	}
	
	public List<Multimedia> getMultimediaDataByDate(int pageSize, DateTime startDate,DateTime endDate ) throws JSONException
	{
		List<Multimedia> data=multimediaRepository.allByDate(pageSize,startDate,endDate);
		return data;
	}
	
	public boolean uploadFile(Multimedia multimedia,
			MultipartFile multimediaFile) {
		
		if (!multimediaFile.isEmpty()) {
			try {

				File multimediaDir;
				switch (multimedia.getContentType()) {
				
				case "video":
					multimediaDirPath = "videos" + File.separator
							+ multimedia.getBaseEntityId() +"."+ multimedia.getExtension();
					break;
				case "document":
					multimediaDirPath = "documents" + File.separator
							+ multimedia.getBaseEntityId() +"."+ multimedia.getExtension();
					break;
				case "image":
					multimediaDirPath = "images" + File.separator
							+ multimedia.getBaseEntityId() +"."+ multimedia.getExtension();
					break;
				default:
					multimediaDirPath = "documents" + File.separator
							+ multimedia.getBaseEntityId() +"."+ multimedia.getExtension();
					break;
				}
				
				multimediaDir =  new File(multimediaDirPath);
				if(!multimediaDir.getParentFile().exists()) {
					multimediaDir.getParentFile().mkdirs();
				}
				multimediaFile.transferTo(multimediaDir);
				return true;
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

}
