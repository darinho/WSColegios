/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Karlita
 */
public class CommonEncripta {

    private static final char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String key = "#gbAmyC01$*7qxm@&dic10$*";
    private static final byte[] initializationVector = {18, 12, 45, 37, 7, 98, 68, 79};

    public static String get_md5(String sTexto) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(sTexto.getBytes());
            StringBuilder sb = new StringBuilder(2 * bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                int low = (int) (bytes[i] & 0x0f);
                int high = (int) ((bytes[i] & 0xf0) >> 4);
                sb.append(HEXADECIMAL[high]);
                sb.append(HEXADECIMAL[low]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getHashSHA256(String ClearText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            String OriginalText = ClearText;

            byte byteData[] = md.digest(OriginalText.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
        }

        return "";
    }

    public static String encryptTripleDes(String plainText) {
        try {
            //---- Use specified 3DES key and IV from other source --------------
            byte[] plaintext = plainText.getBytes();
            byte[] tdesKeyData = key.getBytes();
// byte[] myIV = initializationVector.getBytes();
            Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
            IvParameterSpec ivspec = new IvParameterSpec(initializationVector);
            c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
            byte[] cipherText = c3des.doFinal(plaintext);
            return new BASE64Encoder().encode(cipherText);
        } catch (Exception e) {
            return "";
        }
    }

    public static String decryptTripleDes(String cipherText) {
        try {
            byte[] encData = new BASE64Decoder().decodeBuffer(cipherText);
            Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            byte[] tdesKeyData = key.getBytes();
            SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
            IvParameterSpec ivspec = new IvParameterSpec(initializationVector);
            decipher.init(Cipher.DECRYPT_MODE, myKey, ivspec);
            byte[] plainText = decipher.doFinal(encData);
            return new String(plainText);
        } catch (Exception e) {
            return "";
        }
    }

}
