package org.opensrp;

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
import com.amazonaws.util.IOUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opensrp.domain.Multimedia;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.repository.MultimediaRepository;
import org.opensrp.service.ClientService;
import org.opensrp.service.MultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileNotFoundException;

import java.net.URL;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp.xml")
public class MultimediaServiceTest {

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

	@Mock
	private MultimediaService multimediaService;

	@Autowired
	private MultimediaRepository multimediaRepository;

	@Autowired
	private ClientService clientService;

	@Autowired
	@Value("#{opensrp['multimedia.directory.name']}")
	private String multimediaDirPath;

	private static final int MAX_ERROR_RETRY = 5;

	private static final int MAX_CONNECTIONS = 5000;

	private static final int MAX_CONNECTION_TIME_OUT = 5000;

	private static final int SOCKET_TIME_OUT = 5000;

	public static final int BUFFER_SIZE = 1024;

	private URL url;

	private File expectedImage;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		url = new URL("https://www.dropbox.com/s/cwazpyy2mipj865/gimage.png");
		expectedImage = new File("s3image.jpg");
		multimediaService = new MultimediaService(multimediaRepository, clientService);
	}

	@Ignore
	@Test
	public void shouldSaveMultimediaFile() throws FileNotFoundException {
		MultimediaDTO multimedia = new MultimediaDTO("1234567891", "opensrp", "image/jpeg", "", "nid");
		String baseDirPath = System.getProperty("user.home");
		FileInputStream fis = new FileInputStream(baseDirPath + File.separator + ".OpenSRP/multimedia/image.jpeg");

		MultipartFile multipartFile = null;

		try {
			multipartFile = new MockMultipartFile("file", fis);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String status = multimediaService.saveMultimediaFile(multimedia, multipartFile);

		Assert.assertEquals("success", status);

	}

	@Ignore
	@Test
	public void shouldGetMultimediaFiles() throws FileNotFoundException {
		MultimediaDTO multimediaDTO = new MultimediaDTO("1234567890", "opensrp", "image/jpeg", "", "profile");

		Multimedia expectedMultimedia = new Multimedia().withCaseId(multimediaDTO.getCaseId())
				.withProviderId(multimediaDTO.getProviderId()).withContentType(multimediaDTO.getContentType())
				.withFilePath(multimediaDTO.getFilePath()).withFileCategory(multimediaDTO.getFileCategory());

		String baseDirPath = System.getProperty("user.home");
		FileInputStream fis = new FileInputStream(baseDirPath + File.separator + ".OpenSRP/multimedia/image.jpeg");
		MultipartFile multipartFile = null;

		try {
			multipartFile = new MockMultipartFile("file", fis);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		boolean status = multimediaService.uploadFile(multimediaDTO, multipartFile);

		if (status)
			multimediaRepository.add(expectedMultimedia);

		List<Multimedia> multimediaFiles = multimediaService.getMultimediaFiles("opensrp");

		for (Multimedia actualMultimedia : multimediaFiles) {
			if (actualMultimedia.getCaseId().equals(multimediaDTO.getCaseId()))
				Assert.assertEquals(expectedMultimedia.getFilePath(), actualMultimedia.getFilePath());
		}
	}

	@Ignore
	@Test
	public void shouldSaveImageTos3() throws Exception {
		FileUtils.copyURLToFile(url, expectedImage);
		Assert.assertEquals("success", multimediaService
				.uploadImageToS3(expectedImage, awsAccessKeyId, awsSecretAccessKey, awsRegion, awsBucket, mediaKeyFolder));

	}

	@Ignore
	@Test
	public void shouldDownloadImageFroms3() throws Exception {
		FileUtils.copyURLToFile(url, expectedImage);
		ClientConfiguration clientConfiguration = new ClientConfiguration().withMaxErrorRetry(MAX_ERROR_RETRY)
				.withMaxConnections(MAX_CONNECTIONS).withConnectionTimeout(MAX_CONNECTION_TIME_OUT)
				.withSocketTimeout(SOCKET_TIME_OUT);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withCredentials(
				new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey)))
				.withClientConfiguration(clientConfiguration).withRegion(awsRegion).build();

		OutputStream fetchedOutputStream = null;
		OutputStream expectedOutputStream = null;
		try {
			S3Object object = s3Client.getObject(new GetObjectRequest(awsBucket, mediaKeyFolder + "gimage.png"));
			S3ObjectInputStream objectContent = object.getObjectContent();
			IOUtils.copy(objectContent, fetchedOutputStream);

			byte[] buffer = new byte[BUFFER_SIZE];
			int count;
			FileInputStream fileInputStream = new FileInputStream(expectedImage);
			while ((count = fileInputStream.read(buffer)) >= 0) {
				expectedOutputStream.write(buffer, 0, count);
			}
			Assert.assertEquals(expectedOutputStream, fetchedOutputStream);
			expectedOutputStream.flush();
			fetchedOutputStream.flush();
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
}
