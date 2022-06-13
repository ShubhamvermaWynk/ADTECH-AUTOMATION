package com.airtel.utils;

import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;
import io.restassured.response.Response;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.testng.Assert;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Random;
import java.util.StringTokenizer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.X509EncodedKeySpec;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import static com.airtel.adtech.constants.AdtechApiConstants.GET_PUBLIC_KEY;

public class Utilities extends ReportHelper {

    /*
    *   DES encryption/decryption
    */
    public static final String CARRIAGE_RETURN = "\r";
    public static final String CARRIAGE_RETURN_REPLACEMENT = "__CRGRTRN__";
    public static final String PLUS = "\\+";
    public static final String PLUS_REPLACEMENT = "__PLS__";
    public static final String SLASH = "/";
    public static final String SLASH_REPLACEMENT = "__SLSH__";
    public static final String NEWLINE = "\n";
    public static final String NEWLINE_REPLACEMENT = "__NWLIN__";
    private static String KEYGEN_STR = "l@l@ruru#@";
    private static Cipher ecipher = null;
    private static Cipher dcipher = null;

    /*
    *   RSA encryption/decryption
    */
    public Cipher eCipher_RSA = null;
    public byte[] publicKey;
    CommonApi commonApi = new CommonApi();

    /*
    *   key setup for RSA encryption/decryption
    */
    public void setUp()
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec ospec = new X509EncodedKeySpec(publicKey);
        this.eCipher_RSA = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        eCipher_RSA.init(Cipher.ENCRYPT_MODE, kf.generatePublic(ospec));
    }

    public String encrypt_RSA(String stringToBeEncrypted, String serverInitials, String uniqueIdentifer) {
        try {
            Response response = commonApi.getDevicesResponseWithHeadersIgnoreSecurity(serverInitials, null,
                    GET_PUBLIC_KEY, true, false, null, null, uniqueIdentifer);
            JSONObject jsonObject = new JSONObject(response.asString());
            publicKey = Base64.decodeBase64(jsonObject.get("key").toString().getBytes());
            setUp();
            return Base64.encodeBase64String(eCipher_RSA.doFinal(stringToBeEncrypted.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            ReportHelper.reporterLogging(false,e.getMessage() + e);
            e.printStackTrace();
            Assert.assertTrue(false);
            return stringToBeEncrypted;
        }
    }

    /*
    *   Get Key for DES encrypt/decrypt
    */
    private static Key getKey() {
        try {
            byte[] bytes = getbytes(KEYGEN_STR);
            DESKeySpec pass = new DESKeySpec(bytes);
            SecretKeyFactory sKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey sKey = sKeyFactory.generateSecret(pass);
            return sKey;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /*
    *   Get bytes
    */
    private static byte[] getbytes(String str) {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        StringTokenizer sTokenizer = new StringTokenizer(str, "-", false);
        while (sTokenizer.hasMoreTokens()) {
            try {
                byteOutputStream.write(sTokenizer.nextToken().getBytes());
            } catch (IOException ex) {}
        }
        byteOutputStream.toByteArray();
        return byteOutputStream.toByteArray();
    }

    /*
    *   DES decrypt
    */
    public static String decrypt(String sourceStr) {
        if (sourceStr == null || "".equals(sourceStr)) {
            return null;
        }
        try {
            sourceStr = sourceStr.replaceAll(NEWLINE_REPLACEMENT, NEWLINE);
            sourceStr = sourceStr.replaceAll(SLASH_REPLACEMENT, SLASH);
            sourceStr = sourceStr.replaceAll(PLUS_REPLACEMENT, PLUS);
            sourceStr = sourceStr.replaceAll(CARRIAGE_RETURN_REPLACEMENT, CARRIAGE_RETURN);
            // Get secret key
            Key key = getKey();
            dcipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            dcipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dec = Base64.decodeBase64(sourceStr.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /*
    *   DES encrypt
    */
    public static String encrypt(String sourceStr) {
        if (sourceStr == null || "".equals(sourceStr))
            return null;
        try {
            // Get secret key
            Key key = getKey();
            ecipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] enc = ecipher.doFinal((new String(sourceStr)).getBytes("UTF-8"));
            String encryptedResult = new String(Base64.encodeBase64(enc));
            if (encryptedResult != null) {
                encryptedResult = encryptedResult.replaceAll(CARRIAGE_RETURN, CARRIAGE_RETURN_REPLACEMENT);
                encryptedResult = encryptedResult.replaceAll(PLUS, PLUS_REPLACEMENT);
                encryptedResult = encryptedResult.replaceAll(SLASH, SLASH_REPLACEMENT);
                encryptedResult = encryptedResult.replaceAll(NEWLINE, NEWLINE_REPLACEMENT);
            }
            return encryptedResult;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /*
    *   function to mask characters of a string with a special character passed
    */
    public String maskString(String string, int startIndex, int endIndex, char maskChar) {

        if (string == null || string.equals("")) {
            ReportHelper.logValidationFailure("string", "string.length()>=1", string,"string to be masked can't be null or blank");
            Assert.assertTrue(false);
        }
        if (startIndex > endIndex) {
            ReportHelper.logValidationFailure("End Index","<="+startIndex, String.valueOf(startIndex),"End index cannot be greater than the start index");
            Assert.assertTrue(false);
        }
        if (startIndex < 0)
            startIndex = 0;
        if (endIndex > string.length())
            endIndex = string.length();

        int maskLength = endIndex - startIndex;
        if (maskLength == 0)
            return string;

        StringBuilder sbMaskString = new StringBuilder(maskLength);
        for (int i = 0; i < maskLength; i++)
            sbMaskString.append(maskChar);

        return string.substring(0, startIndex) + sbMaskString.toString() + string.substring(startIndex + maskLength);
    }

    public static String getRandomString(int n)
    {

        String AlphaNumericString = "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));

        }

        return sb.toString();
    }

    public static String getRandomNumber(int n) {

        String AlphaNumericString = "0123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();

    }

}
