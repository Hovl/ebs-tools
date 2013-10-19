package ebs.tools;

/**
 * Created with IntelliJ IDEA.
 * User: murik
 * Date: 15.02.12
 * Time: 19:09
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashMaker {

    public static String makeSHA256Hash(String s) {
   		return makeHash("SHA-256", s);
   	}

	public static String makeMD5Hash(String s) {
		return makeHash("MD5", s);
	}

    public static String makeHash(String algorithm, String s) {
   		String result;
   		try {
   			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
   			byte[] digest = messageDigest.digest(s.getBytes());
               result = toHex(digest);
   		} catch (NoSuchAlgorithmException e) {
   			return null;
   		}
   		return result;
   	}

	private static String toHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for(byte anA : a) {
			sb.append(Character.forDigit((anA & 0xf0) >> 4, 16));
			sb.append(Character.forDigit(anA & 0x0f, 16));
		}
		return sb.toString();
	}

}