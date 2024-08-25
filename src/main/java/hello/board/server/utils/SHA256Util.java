package hello.board.server.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SHA256Util {
    public static final String ENCRYPTION_KEY = "SHA-256";

    private SHA256Util() {
    }

    public static String encrptySHA256(String text) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(ENCRYPTION_KEY);
            messageDigest.update(text.getBytes());
            byte[] digested = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte byteData : digested) {
                sb.append(Integer.toString((byteData & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("encrypt SHA256 ERROR : {}", e.getMessage());
        }

        return null;
    }

}
