package ebs.tools;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import java.io.Writer;
import java.io.IOException;

/**
 * Created by Dubov Aleksei
 * Date: Nov 7, 2007
 * Time: 7:08:26 PM
 * Company: EBS (c) 2007
 */
public class XMLTools {
	public static String getXMLText(Document doc) {
		return new XMLOutputter().outputString(doc);
	}

	public static void saveXML(Document doc, Writer writer) throws IOException {
		new XMLOutputter().output(doc, writer);
	}
}
