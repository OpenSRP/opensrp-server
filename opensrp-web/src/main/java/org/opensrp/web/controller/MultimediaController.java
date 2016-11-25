package org.opensrp.web.controller;

import static ch.lambdaj.collection.LambdaCollections.with;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;

import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.jcodec.api.JCodecException;
import org.jcodec.common.NIOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.opensrp.service.MultimediaService;
import org.opensrp.domain.*;
import org.opensrp.repository.*;

import ch.lambdaj.function.convert.Converter;

import com.google.gson.Gson;

@Controller
public class MultimediaController {

	private MultimediaService multimediaService;

	private MultimediaRepository multimediaRepository;

	private String basePath;

	@Autowired
	public MultimediaController(MultimediaService multimediaService,
			MultimediaRepository multimediaRepository,
			@Value("#{opensrp['multimedia.datastore.directory']}") String path) {
		this.multimediaRepository = multimediaRepository;
		this.multimediaService = multimediaService;
		this.basePath=path;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/multimediaData", produces = "application/json")
	@ResponseBody
	public List<Multimedia> getMultimediaData(HttpServletRequest request,
			@RequestParam("searchByName") String searchByName,
			@RequestParam("searchByContent") String searchByContent,
			@RequestParam("searchByStartDate") String searchByStartDate,
			@RequestParam("searchByEndDate") String searchByEndDate,
			@RequestParam("length") Integer length) throws Exception {
		List<Multimedia> temp;
		if (!searchByName.isEmpty()) {
			temp = multimediaService.getMultimediaDataByName(length,
					searchByName);
		} else if (!searchByContent.isEmpty()) {
			temp = multimediaService.getMultimediaDataByContent(length,
					searchByContent);
		} else if (!searchByStartDate.isEmpty() && !searchByEndDate.isEmpty()) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startdate = format.parse(searchByStartDate);
			Date enddate = format.parse(searchByEndDate);
			DateTime startdateTime = new DateTime(startdate);
			DateTime enddateTime = new DateTime(enddate);

			temp = multimediaService.getMultimediaDataByDate(length,
					startdateTime, enddateTime);
		} else {
			temp = multimediaService.getMultimediaData(length);
		}
		System.out.print(temp);
		return temp;
	}

	@RequestMapping(value = "/image", method = RequestMethod.GET)
	@ResponseBody
	public void multimediaImagePreview(@RequestParam("path") String data,
			Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			org.bytedeco.javacv.FrameGrabber.Exception, JCodecException {

		String filePath = basePath + File.separator + data;
		String SendfilePath="";
		System.out.println();
		System.out.println(filePath);
		if (data.contains("video")) {
			System.out.println(data);
			File file = new File(filePath);
			BufferedImage frame = getFrame(file, 1.0);
			ImageIO.write(frame, "png", new File("frame_1.png"));
			System.out.println("\n data:" + data);
			filePath="frame_1.png";
		} else if (data.contains("documents")) {
			PDDocument document = PDDocument
					.load(new File(filePath));
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			int page = 0;
			BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300,
					ImageType.RGB);
			ImageIO.write(bim, "png", new File("pdfImage_1.png"));

			document.close();
			filePath="pdfImage_1.png";
		} else if (data.contains("images")) {
			System.out.println(filePath);
		}
		int fileSize = (int) new File(filePath).length();
		response.setContentLength(fileSize);
		response.setContentType("image/png");
		FileInputStream inputStream = new FileInputStream(filePath);
		ServletOutputStream outputStream = response.getOutputStream();
		int value = IOUtils.copy(inputStream, outputStream);
		System.out.println("File Size :: " + fileSize);
		System.out.println("Copied Bytes :: " + value);
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		response.setStatus(HttpServletResponse.SC_OK);

	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	public void multimediaDownload(@RequestParam("path") String data,
			Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		getMultimedia(data, locale, model, request, response);
	}

	@RequestMapping(value = "/preview", method = RequestMethod.GET)
	@ResponseBody
	public void multimediaPreview(@RequestParam("path") String data,
			Locale locale, Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		getMultimedia(data, locale, model, request, response);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/page")
	@ResponseBody
	public ModelAndView showPage() throws JSONException {
		return new ModelAndView("multimedia_page");
	}

	BufferedImage getFrame(File file, double sec) throws IOException,
			JCodecException {
		FileChannelWrapper ch = null;
		try {
			ch = NIOUtils.readableFileChannel(file);
			return ((FrameGrab) new FrameGrab(ch).seekToSecondPrecise(sec))
					.getFrame();
		} finally {
			NIOUtils.closeQuietly(ch);
		}
	}

	public void getMultimedia(String data, Locale locale, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String filePath = basePath + File.separator + data;
		int fileSize = (int) new File(filePath).length();
		response.setContentLength(fileSize);
		if (data.contains("video"))
			response.setContentType("video/mp4");
		else if (data.contains("image"))
			response.setContentType("image/png");
		else if (data.contains("document"))
			response.setContentType("application/pdf");
		FileInputStream inputStream = new FileInputStream(filePath);
		ServletOutputStream outputStream = response.getOutputStream();
		int value = IOUtils.copy(inputStream, outputStream);
		System.out.println("File Size :: " + fileSize);
		System.out.println("Copied Bytes :: " + value);
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(headers = { "Accept=application/json" }, method = GET, value = "/getmultimedia")
	@ResponseBody
	public List<Multimedia> getMultimediaFiles(@RequestParam("providerId") String providerId) {

		List<Multimedia> allMultimedias = multimediaService
				.getMultimediaFiles(providerId);

		return with(allMultimedias).convert(
				new Converter<Multimedia, Multimedia>() {
					@Override
					public Multimedia convert(Multimedia md) {
						return new Multimedia(md.getBaseEntityId(), md
								.getProviderId(), md.getContentType(), md
								.getFilePath(), md.getFileCategory());
					}
				});
	}

	@RequestMapping(headers = { "Accept=multipart/form-data" }, method = POST, value = "/setmultimedia")
	public ResponseEntity<String> uploadMultimediaFiles(
			@RequestParam("providerId") String providerId,
			@RequestParam("entityId") String entityId,
			@RequestParam("contentType") String contentType,
			@RequestParam("fileCategory") String fileCategory,
			@RequestParam("file") MultipartFile file) {

		Multimedia Multimedia = new Multimedia(entityId, providerId,
				contentType, null, fileCategory);

		String status = multimediaService.saveMultimediaFile(Multimedia, file);

		return new ResponseEntity<>(new Gson().toJson(status), HttpStatus.OK);
	}
}
