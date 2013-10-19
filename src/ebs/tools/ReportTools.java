package ebs.tools;

import com.carbonfive.sstemplates.SsTemplateException;
import com.carbonfive.sstemplates.SsTemplateProcessor;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.digester.Digester;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.xmlpull.mxp1_serializer.MXSerializer;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Dubov Aleksei
 * Date: Aug 26, 2008
 * Time: 4:04:02 PM
 * Company: EBS (c) 2008
 */

public class ReportTools {
	private static XmlSerializer serializer = new MXSerializer();

	public static String generateXLS(String name, HashMap<String, String> subData,
									 String[] columns, Integer[] toSumm, List<String[]> data,
									 String baseDir, String fileName)
			throws SsTemplateException, IOException
	{
		generateXLS(name, subData, columns, toSumm, data, FileTools.getFile(baseDir, fileName));
		return fileName;
	}

	public synchronized static void generateXLS(String name, HashMap<String, String> subData,
								   String[] columns, Integer[] toSumm, List<String[]> data,
								   File file)
			throws IOException, SsTemplateException
	{
		String tempFileName = new StringBuilder(CryptTools.makeBase64StringKey(10)).append(".xml").toString();
		File template = new File(file.getParent(), tempFileName);

		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(template), "UTF-8");
		try {
			serializer.setOutput(writer);
			serializer.startTag(null, "workbook");
			serializer.startTag(null, "sheet").attribute(null, "name", "xls");

			Integer delta = 1;

			if(name != null) {
				startRow();
				wrapCell(name);
				endRow();
				delta++;
			}

			if(subData != null) {
				Set<String> keySet = subData.keySet();
				for(String key : keySet) {
					if(key == null) continue;
					String kdata = subData.get(key);
					if(kdata == null) kdata = "";

					startRow();
					wrapCell(new StringBuilder(key).append(':').toString());
					wrapCell(kdata);
					endRow();
					delta++;
				}

				startRow();
				wrapCell("");
				endRow();
				delta++;
			}

			startRow();
			for(String column : columns) {
				if(column == null) column = "";
				wrapCell(column);
			}
			endRow();

			Integer colcounter = delta;
			for(String[] d : data) {
				colcounter += 1;
				startRow();
				for(int i = 0; i < columns.length; i++) {
					serializer.startTag(null, "cell");
					if(toSumm != null) {
						for(Integer checknum : toSumm)  {
							if(i == checknum) serializer.attribute(null, "type", "numeric");
						}
					}
					if(d[i] == null) {
						d[i] = "";
					}
					serializer.text(d[i]).endTag(null, "cell");
				}
				endRow();
			}

			if(toSumm != null) {
				startRow();
				for(int i = 0; i <= columns.length; i++) {
					serializer.startTag(null, "cell");
					for(Integer checknum : toSumm) {
						if(i == checknum) {
							serializer.attribute(null, "type", "formula")
									.text(new StringBuilder("sum(")
											.append((char) ('A' + i)).append(delta + 1).append(':')
											.append((char) ('A' + i)).append(colcounter)
											.append(')').toString());
						}
					}
					serializer.endTag(null, "cell");
				}
				endRow();
			}

			serializer.endTag(null, "sheet").endTag(null, "workbook").endDocument();
		} finally {
			writer.close();
		}

		HSSFWorkbook workbook = null;
		while (workbook == null) {
			try {
				SsTemplateProcessor processor = SsTemplateProcessor.getInstance();
				workbook = processor.process(template, new HashMap(0));
			} catch (NullPointerException e) {
				LoggerFactory.getLogger(ReportTools.class).info("Error getting SAX instance. Retrying...");
				Thread.yield();
			}
		}


		OutputStream out = new FileOutputStream(file);
		try {
			workbook.write(out);
		} finally {
			out.close();
		}

		template.delete();
	}

	private static void startRow() throws IOException {
		serializer.startTag(null, "row");
	}

	private static void endRow() throws IOException {
		serializer.endTag(null, "row");
	}

	private static void wrapCell(String text) throws IOException {
		serializer.startTag(null, "cell").text(text).endTag(null, "cell");
	}

	public static String generatePDF(String[] columns, List<String[]> data, String baseDir,
									 String fileName) throws DocumentException, FileNotFoundException
	{	
		generatePDF(columns, data, FileTools.getFile(baseDir, fileName));
		return fileName;
	}

	public synchronized static void generatePDF(String[] columns, List<String[]> data, File file)
			throws DocumentException, FileNotFoundException
	{
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

//        Font font = new Font(BaseFont.createFont(FONT_LOCATION, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED), 8,
//				Font.NORMAL);

		PdfPTable table = new PdfPTable(columns.length);
		for(String column : columns) table.addCell(new Phrase(column));
		for(String[] d : data)  {
			for(int i = 1; i <= columns.length; i++) table.addCell(new Phrase(d[i]));
		}

		document.add(table);
		document.close();
	}
}
