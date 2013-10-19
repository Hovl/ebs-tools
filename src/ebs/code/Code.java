package ebs.code;

/**
 * Created by Dubov Aleksei
 * Date: Jun 20, 2008
 * Time: 9:39:46 PM
 * Company: EBS (c) 2008
 */

public interface Code {
	String getShortInfo(String locale);
	String getInfo();
	String getToolTip();
	String getColor();
	Boolean isOk();
	Boolean isWarn();
	Boolean isError();
}
