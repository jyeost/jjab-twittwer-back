package jjabtwitter.member.domain;

import jjabtwitter.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static jjabtwitter.global.exception.ExceptionInformation.PASSWORD_ENCRYPT_FAIL;

@RequiredArgsConstructor
@Component
public class PasswordEncoder {
    private static final String SHA_256 = "SHA-256";

    @Value("${salt.en1}")
    private String salt1;

    @Value("${salt.en2}")
    private String salt2;

    public String encode(final String password) {
        try {
            final String salted = saltAndEncrypt(password, salt1);
            return saltAndEncrypt(salted, salt2);
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessLogicException(PASSWORD_ENCRYPT_FAIL);
        }
    }

    private String saltAndEncrypt(final String password, final String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SHA_256);
        final String salted = password + salt;
        md.update(salted.getBytes());

        final StringBuilder sb = new StringBuilder();
        for (byte b : md.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
