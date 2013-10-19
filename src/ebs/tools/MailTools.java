package ebs.tools;

import org.apache.commons.mail.*;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Dubov Aleksey
 * Date: Aug 5, 2008
 * Time: 3:36:24 PM
 * Company: EBS (c) 2008-2011
 */

public class MailTools {
	public static void sendSmtpMail(HashMap<String, String> recipients,
								String subject, String message,
								String fromName,
								String fromAddr,
								HashMap<String, String> filepaths,
								String login,
								String password,
								String host,
								Integer port,
								String encoding)
			throws EmailException
	{
		System.setProperty("javax.net.ssl.keyStorePassword", "changeit"); //glassfish default password for keystore

		HtmlEmail email = new HtmlEmail();

		email.setHostName(host);
		email.setSmtpPort(port);
		email.setTLS(true);
		email.setCharset(encoding);
		email.setAuthentication(login, password);
		email.setDebug(true);

		email.setFrom(fromAddr, fromName);

		if(recipients.isEmpty()) {
			return;
		}

		Set<String> toSet = recipients.keySet();
		for(String to : toSet) {
			String name = recipients.get(to);
			if(name != null) {
				email.addTo(to, name);
			} else {
				email.addTo(to);
			}
		}

		email.setSubject(subject);
		email.setHtmlMsg(message);

		if(filepaths != null) {
			Set<String> pathSet = filepaths.keySet();
			for(String path : pathSet) {
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(path);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);

				String name = filepaths.get(path);
				if(name != null) {
					attachment.setName(name);
				}

				email.attach(attachment);
			}
		}

		email.send();
	}
}
