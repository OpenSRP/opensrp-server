package org.opensrp.web.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.google.gson.Gson;
import org.opensrp.domain.Multimedia;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.service.MultimediaService;
import org.opensrp.web.security.DrishtiAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/multimedia")
public class MultimediaController {

	private static Logger logger = LoggerFactory.getLogger(MultimediaController.class.toString());

	public static final int MAX_ERROR_RETRY = 5;

	public static final int MAX_CONNECTIONS = 5000;

	public static final int MAX_CONNECTION_TIME_OUT = 5000;

	public static final int SOCKET_TIME_OUT = 5000;

	@Value("#{opensrp['multimedia.directory.name']}")
	private String multiMediaDir;

	@Value("#{opensrp['aws.access.key.id']}")
	private String awsAccessKeyId;

	@Value("#{opensrp['aws.secret.access.key']}")
	private String awsSecretAccessKey;

	@Value("#{opensrp['aws.region']}")
	private String awsRegion;

	@Value("#{opensrp['aws.bucket']}")
	private String awsBucket;

	@Value("#{opensrp['aws.key.folder']}")
	private String mediaKeyFolder;

	@Value("#{opensrp['multimedia.directory.location']}")
	private String multimediaDirectoryLocation;

	@Autowired
	@Qualifier("drishtiAuthenticationProvider")
	DrishtiAuthenticationProvider provider;

	private String fileExtension = ".jpg";

	@Autowired
	MultimediaService multimediaService;

	/**
	 * Download a file from the multimedia directory. The method also assumes two file types mp4 and
	 * images whereby all images are stored in the images folder and videos in mp4 in the multimedia
	 * directory This method is set to bypass spring security config but authenticate through the
	 * username/password passed at the headers
	 *
	 * @param response
	 * @param fileName
	 * @param userName
	 * @param password
	 * @throws IOException
	 */
	@RequestMapping(value = "/download/{fileName:.+}", method = RequestMethod.GET)
	public void downloadFile(HttpServletResponse response, @PathVariable("fileName") String fileName,
	                         @RequestHeader(value = "username") String userName,
	                         @RequestHeader(value = "password") String password) throws Exception {

		try {
			if (authenticate(userName, password).isAuthenticated()) {
				File file = new File(multiMediaDir + File.separator + "images" + File.separator + fileName);
				if (fileName.endsWith("mp4")) {
					file = new File(multiMediaDir + File.separator + "videos" + File.separator + fileName);
				}

				downloadFile(file, response);
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * This method downloads a file from the server given the client id. A search is made to the
	 * multimedia repo to see if any file exists mapped to the user whereby the filepath is recorded
	 *
	 * @param response
	 * @param baseEntityId
	 * @param userName
	 * @param password
	 * @throws Exception
	 */
	@RequestMapping(value = "/profileimage/{baseEntityId}", method = RequestMethod.GET)
	public void downloadFileByClientId(HttpServletResponse response, @PathVariable("baseEntityId") String baseEntityId,
	                                   @RequestHeader(value = "username") String userName,
	                                   @RequestHeader(value = "password") String password) throws Exception {
		try {
			if (multimediaDirectoryLocation.equalsIgnoreCase("s3")) {
				downloadFileFromS3(new File(baseEntityId + fileExtension), awsAccessKeyId, awsSecretAccessKey, awsRegion,
						awsBucket, mediaKeyFolder, response);
			} else {
				if (authenticate(userName, password).isAuthenticated()) {
					Multimedia multiMedia = multimediaService.findByCaseId(baseEntityId);
					if (multiMedia == null || multiMedia.getFilePath() == null) {
						//see if the file exists in the disk with the assumption that it's .jpg otherwise return error msg
						File file = new File(
								multiMediaDir + File.separator + MultimediaService.IMAGES_DIR + File.separator + baseEntityId
										+ fileExtension);
						if (file.exists()) {
							downloadFile(file, response);
						} else {
							setErrorMessage(response);
							return;
						}
					}
					String filePath = multiMedia.getFilePath();

					File file = new File(filePath);
					downloadFile(file, response);
				}
			}
		}
		catch (Exception e) {
			logger.error("", e);
		}

	}

	@RequestMapping(headers = { "Accept=multipart/form-data" }, method = POST, value = "/upload")
	public ResponseEntity<String> uploadFiles(@RequestParam("anm-id") String providerId,
	                                          @RequestParam("entity-id") String entityId,
	                                          @RequestParam("file-category") String fileCategory,
	                                          @RequestParam("file") MultipartFile file) {

		String contentType = file.getContentType();

		MultimediaDTO multimediaDTO = new MultimediaDTO(entityId, providerId, contentType, null, fileCategory);

		String status = multimediaService.saveMultimediaFile(multimediaDTO, file);

		return new ResponseEntity<>(new Gson().toJson(status), HttpStatus.OK);
	}

	private Authentication authenticate(String userName, String password) {
		Authentication auth = new UsernamePasswordAuthenticationToken(userName, password);
		auth = provider.authenticate(auth);
		return auth;
	}

	private void downloadFile(File file, HttpServletResponse response) throws Exception {

		if (!file.exists()) {
			setErrorMessage(response);
			return;
		}

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			logger.info("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}

		logger.info("mimetype : " + mimeType);

		response.setContentType(mimeType);

		/* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser
		    while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

		/* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
		//response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

		response.setContentLength((int) file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		//Copy bytes from source to destination(outputstream in this example), closes both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	private void downloadFileFromS3(File imageFile, String awsAccessKeyId, String awsSecretAccessKey, String awsRegion,
	                                String awsBucket, String awsKeyFolder, HttpServletResponse response) throws Exception {
		//		client configuration optimization and socket timeout
		ClientConfiguration clientConfiguration = new ClientConfiguration().withMaxErrorRetry(MAX_ERROR_RETRY)
				.withMaxConnections(MAX_CONNECTIONS).withConnectionTimeout(MAX_CONNECTION_TIME_OUT)
				.withSocketTimeout(SOCKET_TIME_OUT);
		//		instantiate s3 client with configs
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(
				new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey)))
				.withClientConfiguration(clientConfiguration).withRegion(awsRegion).build();

		String mimeType = URLConnection.guessContentTypeFromName(imageFile.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + imageFile.getName() + "\""));
		response.setContentLength((int) imageFile.length());
		try {
			S3Object object = s3Client.getObject(new GetObjectRequest(awsBucket, awsKeyFolder + imageFile.getName()));
			S3ObjectInputStream objectContent = object.getObjectContent();
			//			copy input stream contents to file
			FileCopyUtils.copy(objectContent, response.getOutputStream());
			object.close();
			objectContent.close();
		}
		catch (AmazonServiceException aws) {
			aws.printStackTrace();
		}
		catch (AmazonClientException aws) {
			aws.printStackTrace();
		}
		catch (IOException io) {
			io.printStackTrace();
		}
	}

	private void setErrorMessage(HttpServletResponse response) {
		String errorMessage = "Sorry. The file you are looking for does not exist";
		try {
			logger.info(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
