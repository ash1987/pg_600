package com.company.pg.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * 版权：融贯资讯 <br/>
 * 作者：shaohua.ai@rogrand.com <br/>
 * 生成日期：2013-11-6 <br/>
 * 描述：AES加解密
 */
public class Encryption {
	
	/**
	 * 
	 * TODO 对明文加密
	 * 
	 * @param seed
	 *            明钥
	 * @param plaintext
	 *            明文
	 * @return 密文
	 * @throws Exception
	 *             String
	 */
	public static String encrypt(String seed, String plaintext) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] result = encrypt(rawKey, plaintext.getBytes());
		return toHex(result);
	}
	
	/**
	 * 
	 * TODO 将密文解密为明文
	 * 
	 * @param seed
	 *            明钥
	 * @param encrypted
	 *            密文
	 * @return 明文
	 * @throws Exception
	 *             String
	 */
	public static String decrypt(String seed, String encrypted) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}
	
	/**
	 * 
	 * TODO 使用AES加密算法加密明钥
	 * 
	 * @param seed
	 *            明钥
	 * @return 密钥
	 * @throws Exception
	 *             byte[]
	 */
	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");// 定义AES算法的（对称）密钥生成器
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");// 定义伪随机数生成器
																// (RNG)
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}
	
	/**
	 * 
	 * TODO 使用密钥加密明文
	 * 
	 * @param raw
	 *            密钥
	 * @param clear
	 *            明文
	 * @return 密文
	 * @throws Exception
	 *             byte[]
	 */
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}
	
	/**
	 * 
	 * TODO 使用AES解密密文
	 * 
	 * @param raw
	 *            密钥
	 * @param encrypted
	 *            密文
	 * @return 明文
	 * @throws Exception
	 *             byte[]
	 */
	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
	
	/**
	 * 
	 * TODO 将16进制字符串转换为字节数组
	 * 
	 * @param hexString
	 *            16进制字符串
	 * @return byte[]
	 */
	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}
	
	/**
	 * 
	 * TODO 将字节数组转换为16进制字符串
	 * 
	 * @param buf
	 *            字节数组
	 * @return String
	 */
	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}
	
	private final static String HEX = "0123456789ABCDEF";
	
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
	
}
