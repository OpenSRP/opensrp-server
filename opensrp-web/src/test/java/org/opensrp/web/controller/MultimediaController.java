package org.opensrp.web.controller;

import static org.mockito.MockitoAnnotations.initMocks;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.opensrp.domain.Multimedia;
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
public class MultimediaController {
	
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
	
	@Test
	public void shouldSaveMultimediaFile() throws FileNotFoundException, ParseException
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse("2016-11-08");
		DateTime dateTime=new DateTime(date);
		Multimedia multimedia = new Multimedia(null, "421141472", "92","png", null, "Image", null, dateTime, "This is An Image of Polio form", null, null, null);
		System.out.println(System.getProperty("user.dir"));
		String status = multimediaService.saveMultimediaFile("D:\\openSRP_server_workspace\\opensrp-server\\opensrp-web\\src\\test\\resources\\images\\sample.png", multimedia);
		Assert.assertEquals("success", status);
		System.out.println(System.getProperty("user.dir"));
	}
	
	@Test
	public void shouldGetPagedData() throws FileNotFoundException, JSONException
	{	
		Date date = new Date();
		Assert.assertNotNull( multimediaService.getMultimediaDataByDate(10, new DateTime(date), new DateTime(date)));
	}
	
	@Test
	public void shouldSaveFrames() throws org.bytedeco.javacv.FrameGrabber.Exception, IOException, JCodecException
	{
    	File file=new File(System.getProperty("user.dir")+"\\video\\video.mp4");
    	BufferedImage frame = getFrame(file, 1.0);
    	ImageIO.write(frame, "png", new File("frame_1.png"));
	}
	
	BufferedImage getFrame(File file, double sec) throws IOException, JCodecException {
        FileChannelWrapper ch = null;
        try {
            ch = NIOUtils.readableFileChannel(file);
            return ((FrameGrab) new FrameGrab(ch).seekToSecondPrecise(sec)).getFrame();
        } finally {
            NIOUtils.closeQuietly(ch);
        }
    }
	
	@Test
	public void shouldSavePdf() throws IOException
	{
    PDDocument document = PDDocument.load(new File(System.getProperty("user.dir")+"\\pdf\\pdf_guide.pdf"));
	PDFRenderer pdfRenderer = new PDFRenderer(document);
	for (int page = 0; page < document.getNumberOfPages(); ++page)
	{ 
	    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

	    // suffix in filename will be used as the file format
	    ImageIO.write(bim, "png", new File("pdfImage_1.png"));
	}
	document.close();
	}
	
	
}
