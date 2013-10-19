package ebs.tools;

import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * Вспомогательный класс для зашифровки и расшифровки данных.
 *
 * Created by Dubov Aleksei
 * Date: Jun 30, 2007
 * Time: 10:04:42 PM
 * Company: EBS (c) 2007
 */
public class CryptTools {
	public static final String[] CRYPTERS =
			{"DSA", "MD5", "SHA", "DES", "DESede", "PBEWithMD5AndDES", "HmacMD5", "HmacSHA1"};
	
	public static final SecureRandom SECURE_RANDOM = new SecureRandom();

	public static final BASE64Encoder ENCODER = new BASE64Encoder();
	public static final BASE64Decoder DECODER = new BASE64Decoder();

	public static String makeBase64StringKey(int len) {
		byte[] keyBytes = new byte[len];
		SECURE_RANDOM.nextBytes(keyBytes);
		StringBuilder encoded = new StringBuilder();
		for (int i = 0; i < keyBytes.length; i += 3) {
			encoded.append(encodeBase64Block(keyBytes, i));
			if(encoded.length() > len) break;
		}
		return encoded.toString().substring(0, len);
    }

	private static char[] encodeBase64Block(byte[] raw, int offset) {
		int block = 0;
		int slack = raw.length - offset - 1;
		int end = (slack >= 2) ? 2 : slack;
		for (int i = 0; i <= end; i++) {
			byte b = raw[offset + i];
			int neuter = (b < 0) ? b + 256 : b;
			block += neuter << (8 * (2 - i));
		}
		char[] base64 = new char[4];
		for (int i = 0; i < 4; i++) {
			int sixbit = (block >>> (6 * (3 - i))) & 0x3f;
			base64[i] = getBase64Char(sixbit);
		}
		return base64;
	}

	private static char getBase64Char(int sixBit) {
		if (sixBit >= 0 && sixBit <= 25) return (char)('A' + sixBit);
		if (sixBit >= 26 && sixBit <= 51) return (char)('a' + (sixBit - 26));
		if (sixBit >= 52 && sixBit <= 61) return (char)('0' + (sixBit - 52));
		return '0';
	}

	/**
	 * По ключу <code>key</code> создаёт ключ формата <a href="http://java.sun.com/javase/6/docs/api/javax/crypto/SecretKey.html">SecretKey</a>.
	 *
	 * @param key Исходный ключ.
	 * @return Ключ готовый к использыванию.
	 * @throws UnsupportedEncodingException если не поддерживается кодировка.
	 * @throws InvalidKeyException если заданный ключ не соответстует требованиям.
	 * @throws NoSuchAlgorithmException если отсутствует используемый алгоритм шифрации.
	 * @throws InvalidKeySpecException если неправильный <code>DESKeySpec</code>.
	 */
	private static SecretKey makeKey(String key)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		StringBuilder buf = new StringBuilder(key);
		if (key.length() < 8) {
			for (int i = key.length(); i <= 8; i++) {
				buf.append('0');
			}
		}
		return SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(buf.toString().getBytes("UTF8")));
	}

	/**
	 * Зашифровывает строку по ключу.
	 *
	 * @param data строка для зашифровки.
	 * @param key ключ шифрования.
	 * @return зашифрованная строка.
	 */
	public static String encrypt(String data, String key) {
		try {
			Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			desCipher.init(Cipher.ENCRYPT_MODE, makeKey(key));
			return ENCODER.encodeBuffer(desCipher.doFinal(data.getBytes("UTF8")))
					.replaceAll("\n", "--")
					.replaceAll("/", "slash");
		} catch (Exception e) { LoggerFactory.getLogger(CryptTools.class).error("Exception", e); }
		return null;
	}

	/**
	 * Расшифровывает строку по ключу.
	 *
	 * @param data строка для расшифровки.
	 * @param key ключ шифрования.
	 * @return расшифрованная строка.
	 */
	public static String decrypt(String data, String key) {
		try {
			Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			desCipher.init(Cipher.DECRYPT_MODE, makeKey(key));
			data = data.replaceAll("::", "\n")
								.replaceAll("--", "\n")
								.replaceAll("slash", "/")
								.replaceAll(" ", "+");
			if(data.length() < desCipher.getBlockSize()) {
				return null;
			} else {
				return new String(desCipher.doFinal(DECODER.decodeBuffer(data)));
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(CryptTools.class).warn(new StringBuilder("Cannot decrypt data:").append(data).toString(), e);
			return null;
		}
	}
}
