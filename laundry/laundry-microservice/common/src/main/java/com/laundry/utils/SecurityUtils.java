package com.laundry.utils;



import com.laundry.constant.Algorithm;
import com.laundry.dto.request.UserProfile;
import com.laundry.enums.CustomHeader;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@NoArgsConstructor
public class SecurityUtils {

    //    public static Long getUserId() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        //kiểm tra xem có đúng dạng JwtAuthenticationToken
//        //đồng thời ép kiểu sang jwtAuth
//        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
//            Jwt jwt = jwtAuth.getToken();
//            return Long.parseLong(jwt.getSubject());
//        }
//        return null;
//    }
    public static PrivateKey readPrivateKey(String privateKey) {
        try {
            privateKey = privateKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Base64.getDecoder().decode(privateKey);
            Security.addProvider(new BouncyCastleProvider());
            //yêu cầu sử dụng RSA do BoucyCastle cung cấp
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA_KEYPAIR, "BC");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            log.error("Error reading private key: {}", e.getMessage());
            return null;
        }
    }

    public static PublicKey readPublicKey(String publicKey) {
        try {
            publicKey = publicKey
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            Security.addProvider(new BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance(Algorithm.RSA_KEYPAIR, "BC");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            log.error("Error reading public key: {}", e.getMessage());
            return null;
        }
    }

    public static UserProfile decodeHeader(ServerHttpRequest serverHttpRequest) {
        String encoded = serverHttpRequest.getHeaders().getFirst("X-User-Info");
        String userJson = null;
        if (StringUtils.isNotEmpty(encoded)) {
            userJson = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        }
        return Utils.convertJsonToObject(userJson, UserProfile.class);
    }

    public static String getHeaderValue(String header) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request.getHeader(header);
    }

    public static Long getIdFromHeader() {
        String userIdString = getHeaderValue(CustomHeader.USER_ID.getHeaderName());
        if (StringUtils.isNotEmpty(userIdString)) {
            try {
                return Long.parseLong(userIdString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static String encodeBase64(String request) {
        byte[] encodeBytes = Base64.getEncoder().encode(request.getBytes(StandardCharsets.UTF_8));
        return new String(encodeBytes, StandardCharsets.UTF_8);
    }

    public static String createOtp(int length) {
        if (length <= 0 || length > 10) {
            throw new IllegalArgumentException("Length must be between 1 and 9");
        }
        String input = Long.toString(System.currentTimeMillis() / 1000l);
        String hash = hashMac(input);
        int offset = hash.length() - 8;
        String hexSegment = hash.substring(Math.max(0, offset));
        Long otpNum = Long.parseUnsignedLong(hexSegment, 16);
        Long otpMode = (long) Math.pow(10, length);
        Long otp = otpNum % otpMode;
        return String.format("%0" + length + "d", otp);
    }

    public static String hashMac(String val) {
        try {
            StringBuilder str = new StringBuilder();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(val.getBytes(StandardCharsets.UTF_8));
            for (byte b : hashBytes) {
                str.append(String.format("%02x", b));
            }
            return str.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(String data, String signNature, PublicKey publicKey) {
        Signature signature = null;
        try {
            signature = Signature.getInstance(Algorithm.SHA256withRSA);
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] digitalSignature = Base64.getDecoder().decode(signNature);
            return signature.verify(digitalSignature);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static String signData(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(Algorithm.SHA256withRSA);
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] digitalSignature = signature.sign();
            return Base64.getEncoder().encodeToString(digitalSignature);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
