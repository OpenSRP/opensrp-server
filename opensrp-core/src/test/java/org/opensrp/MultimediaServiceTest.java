package org.opensrp;

import static org.mockito.MockitoAnnotations.initMocks;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.opensrp.domain.Multimedia;
import org.opensrp.dto.form.MultimediaDTO;
import org.opensrp.repository.MultimediaRepository;
import org.opensrp.service.MultimediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-opensrp.xml")
public class MultimediaServiceTest {
	
	@Mock
	private MultimediaService multimediaService;

	@Autowired
	private MultimediaRepository multimediaRepository;
	
	@Autowired
	@Value("#{opensrp['multimedia.directory.name']}") 
	private String multimediaDirPath;
	
	@Before
	public void setUp() throws Exception
	{
		initMocks(this);
		multimediaService = new MultimediaService(multimediaRepository, multimediaDirPath);
	}
	
	
}
