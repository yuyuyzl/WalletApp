package buaa.jj.accountservice;

import java.security.MessageDigest;

public class Encrypt {

    public static String SHA256(String s) {
        String encode = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(s.getBytes("UTF-8"));
            encode = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
