package com.zto.testcase.util;

import org.jpos.iso.ISOUtil;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DESCodec {

    public static String encode(String target) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        return ISOUtil.hexString(cipher.doFinal(target.getBytes()));
    }

    public static String decode(String target) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        return new String(cipher.doFinal(ISOUtil.hex2byte(target)));
    }

    private static Cipher getCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException {
        DESKeySpec keySpec = new DESKeySpec(getPassword());
        Cipher cipher = Cipher.getInstance("DES");
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        cipher.init(mode, keyFactory.generateSecret(keySpec));
        return cipher;
    }

    private static byte[] getPassword() throws UnsupportedEncodingException {
        return "passfordesnishiwode&0082".getBytes("UTF-8");
    }

    public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        System.out.println(encode("root"));
        System.out.println(decode("9C3A0625DAA8C0B08F2611BC14D24876"));
    }

}
