package org.opensrp.web.utils;

import org.junit.Test;

import java.io.*;

import static java.util.Arrays.asList;

public class PdfUtilTest {

	@Test
	public void test() throws IOException {
		ByteArrayOutputStream pdf = PdfUtil.generatePdf(asList("data", "dataaaa", "ddddddddddddddddddddd"), 10,10, 2, 8);
		System.out.println(pdf.size());
		/*FileOutputStream fileOutputStream = new FileOutputStream( File.separator + "samplePdf");
		fileOutputStream.write(pdf.toByteArray());
		fileOutputStream.close();*/
	}

}
