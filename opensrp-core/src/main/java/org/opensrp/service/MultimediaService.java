package org.opensrp.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.opensrp.domain.Client;
import org.opensrp.domain.Multimedia;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.repository.MultimediaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@Service
public class MultimediaService {

	private static Logger logger = LoggerFactory.getLogger(MultimediaService.class.toString());

	public static final String IMAGES_DIR = "patient_images";

	private static final String VIDEOS_DIR = "videos";

	private final MultimediaRepository multimediaRepository;

	private final ClientService clientService;

	private String multimediaDirPath;

	@Value("#{opensrp['multimedia.directory.name']}")
	private String baseMultimediaDirPath;

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

	private String successResponse = "success";

	private String failedResponse = "fail";

	private String imageAttribute = "Patient Image";

	@Autowired
	public MultimediaService(MultimediaRepository multimediaRepository, ClientService clientService) {
		this.multimediaRepository = multimediaRepository;
		this.clientService = clientService;
	}

	public String saveMultimediaFile(MultimediaDTO multimediaDTO, MultipartFile file) {

		String fileExtension = makeFileExtension(multimediaDTO);

		if (multimediaDirectoryLocation.equalsIgnoreCase("s3")) {
				File imageToSave = new File(multimediaDTO.getCaseId() + fileExtension);
			try {
				file.transferTo(imageToSave);
				uploadImageToS3(imageToSave, awsAccessKeyId, awsSecretAccessKey, awsRegion, awsBucket, mediaKeyFolder);
				updateClientWithImage(multimediaDTO, fileExtension);
				return successResponse;
			} catch (IOException e) {
				e.printStackTrace();
				return failedResponse;
			}
		}

		if (uploadFile(multimediaDTO, file)) {
			try {
				logger.info("Image path : " + multimediaDirPath);
				Multimedia multimediaFile = new Multimedia().withCaseId(multimediaDTO.getCaseId())
						.withProviderId(multimediaDTO.getProviderId()).withContentType(multimediaDTO.getContentType())
						.withFilePath(multimediaDTO.getFilePath()).withFileCategory(multimediaDTO.getFileCategory());
				multimediaRepository.add(multimediaFile);
				updateClientWithImage(multimediaDTO, fileExtension);
				return successResponse;
			}
			catch (Exception e) {
				e.getMessage();
				return failedResponse;
			}
		}
		return failedResponse;
	}

	public boolean uploadFile(MultimediaDTO multimediaDTO, MultipartFile multimediaFile) {

		if (!multimediaFile.isEmpty()) {
			try {

				multimediaDirPath = baseMultimediaDirPath + File.separator;
				new File(multimediaDirPath).mkdir();
				String fileName =
						multimediaDirPath + File.separator + multimediaDTO.getCaseId() + makeFileExtension(multimediaDTO);
				multimediaDTO.withFilePath(fileName);
				File multimediaDir = new File(fileName);
				multimediaFile.transferTo(multimediaDir);

				return true;
			}
			catch (Exception e) {
				logger.error("", e);
				return false;
			}
		} else {
			return false;
		}
	}

	public List<Multimedia> getMultimediaFiles(String providerId) {
		return multimediaRepository.all(providerId);
	}

	public Multimedia findByCaseId(String entityId) {
		return multimediaRepository.findByCaseId(entityId);
	}

	public String uploadImageToS3(File imageFile, String awsAccessKeyId, String awsSecretAccessKey, String awsRegion,
	                              String awsBucket, String awsKeyFolder) {

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(
				new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey)))
				.withRegion(awsRegion).build();
		InputStream inputStream;
		try {
			byte[] md5 = DigestUtils.md5(new FileInputStream(imageFile));
			inputStream = new FileInputStream(imageFile);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(imageFile.length());
			metadata.setContentMD5(new String(Base64.encodeBase64(md5)));
			PutObjectRequest request = new PutObjectRequest(awsBucket, awsKeyFolder + imageFile.getName(), inputStream,
					metadata);
			s3Client.putObject(request);
			return successResponse;
		}
		catch (AmazonServiceException e) {
			logger.error("", e);
			return failedResponse;
		}
		catch (SdkClientException e) {
			logger.error("", e);
			return failedResponse;
		}
		catch (FileNotFoundException e) {
			logger.error("", e);
			return failedResponse;
		}
		catch (IOException e) {
			logger.error("", e);
			return failedResponse;
		}
	}

	public String makeFileExtension(MultimediaDTO multimediaDTO) {
		switch (multimediaDTO.getContentType()) {
			case "application/octet-stream":
				multimediaDirPath += VIDEOS_DIR;
				return ".mp4";

			case "image/gif":
				multimediaDirPath += IMAGES_DIR;
				return ".gif";

			case "image/png":
				multimediaDirPath += IMAGES_DIR;
				return ".png";
			default:
				return ".jpg";
		}
	}

	public void updateClientWithImage(MultimediaDTO multimediaDTO, String fileExtension) {
		Client client = clientService.getByBaseEntityId(multimediaDTO.getCaseId());
		if (client != null) {
			try {
				if (client.getAttribute(imageAttribute) != null) {
					client.removeAttribute(imageAttribute);
				}
				client.addAttribute(imageAttribute, multimediaDTO.getCaseId() + fileExtension);
				client.setServerVersion(null);
				clientService.updateClient(client);
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
