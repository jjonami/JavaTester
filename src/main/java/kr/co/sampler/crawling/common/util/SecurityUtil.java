package kr.co.sampler.crawling.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 보안 관련 Util
 *
 */

@Component
public class SecurityUtil {
    final static String key = "6evX3Te57WRI0WEI";
    final static IvParameterSpec iv = new IvParameterSpec(key.substring(0, 16).getBytes());
    private static final String BASE64_PATTERN_STR = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";

    private static final Logger log = LogManager.getLogger(SecurityUtil.class);

    /**
     * Sha-256 암호화
     * @param str
     * @return
     */
    public static String encryptSHA256(String str){
        String sha = "";

        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            sha = sb.toString();

        }catch(NoSuchAlgorithmException e){
            //e.printStackTrace();
            log.debug("Encrypt Error - NoSuchAlgorithmException");
            sha = null;
        }

        return sha;
    }

    /**
     * Sha-256 암호화 검증
     * @param inStr
     * @param encStr
     * @return
     */
    public boolean checkSHA256(String inStr, String encStr) {
        if (StringUtil.isEmpty(inStr)) {
            log.error("inStr is Null");
            return false;
        }

        if (StringUtil.isEmpty(encStr)) {
            log.error("encStr is Null");
            return false;
        }

        if (this.encryptSHA256(inStr).equals(encStr))
            return true;
        else {
            log.error("No Match");
            return false;
        }
    }

    /**
     * 비밀번호 최소 8자리 25자리 이하 숫자, 대문자, 소문자, 특수문자 각각 1개 이상 포함 Validation 체크
     * @param pw
     * @return
     */
    public static boolean validationPasswd(String pw){
        log.debug("pw "+pw);
        //TODO : 다른 특수 기호 예시 `, ~ 확인필요
        //http://highcode.tistory.com/6
        /**
         ^ 으로 우선 패턴의 시작을 알립니다.
         [0-9] 괄호사이에 두 숫자를 넣어 범위를 지정해줄 수 있습니다.
         * 를 넣으면 글자 수를 상관하지 않고 검사합니다.
         $ 으로 패턴의 종료를 알립니다.
         1) 숫자만 : ^[0-9]*$
         2) 영문자만 : ^[a-zA-Z]*$
         3) 한글만 : ^[가-힣]*$
         4) 영어 & 숫자만 : ^[a-zA-Z0-9]*$
         5) E-Mail : ^[a-zA-Z0-9]+@[a-zA-Z0-9]+$
         6) 휴대폰 : ^01(?:0|1|[6-9]) - (?:\d{3}|\d{4}) - \d{4}$
         7) 일반전화 : ^\d{2,3} - \d{3,4} - \d{4}$
         8) 주민등록번호 : \d{6} \- [1-4]\d{6}
         9) IP 주소 : ([0-9]{1,3}) \. ([0-9]{1,3}) \. ([0-9]{1,3}) \. ([0-9]{1,3})
         * */
        Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#?!@$%\\^&*-~])[A-Za-z\\d#?!@$%\\^&*-~]{8,25}$");

        Matcher m = p.matcher(pw);
        if(m.matches()){
            return true;
        }
        return false;
    }

    /**
     * 문자열에 공백여부 체크 있을경우 true 없을경우 false
     * @param
     * @return
     */
    public static boolean validationSpace(String spaceCheck){
        for(int i = 0 ; i < spaceCheck.length() ; i++)
        {
            if(spaceCheck.charAt(i) == ' ')
                return true;
        }
        return false;
    }

    /**
     * 문자열이 null이거나 비어있는 경우 true 아닌경우 false
     * @param emptyCheck
     * @return
     */
    public static boolean validationEmpty(String emptyCheck){
        if(StringUtils.isEmpty(emptyCheck) == true){
            return true;

        } else {
            return false;

        }
    }

    /**
     * 아이디 (특수기호는 $ ! % * # _ - 만 사용) 외 포함되어있으면 false 없을경우 true
     * @param id
     * @return
     */
    public static boolean validationId(String id) {
        Pattern p = Pattern.compile("(?=.*[\\\\{\\\\}\\\\[\\\\]\\\\,;:&|\\\\)~`^+<>\\\\\\\\\\\\\\\\=\\\\(\\\\'\\\\\\\"\\\\])");
        Matcher m = p.matcher(id);

        if(m.find()){
            return false;
        }
        return true;
    }

    public static Key getAESKey() throws Exception {

        Key keySpec;

        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");

        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);
        keySpec = new SecretKeySpec(keyBytes, "AES");

        return keySpec;
    }

    // 암호화
    public static String encryptSeedCEV(String value) {
        try {
            if(NullCheckUtil.isEmpty(value)){
                return 	value;
            }
            Key keySpec = getAESKey();
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = c.doFinal(value.getBytes("UTF-8"));
            String enStr = new String(Base64.encodeBase64(encrypted));

            return enStr;
        }catch (Exception e){
            e.printStackTrace();
            return  value;
        }
    }

    // 복호화
    public static String decryptSeedCEV(String enStr) {
        try {
            if(NullCheckUtil.isEmpty(enStr)){
                return 	enStr;
            }
            if (isBase64Pattern(enStr)) {
                Key keySpec = getAESKey();
                Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
                c.init(Cipher.DECRYPT_MODE, keySpec, iv);
                byte[] byteStr = Base64.decodeBase64(enStr.getBytes("UTF-8"));
                String decStr = new String(c.doFinal(byteStr), "UTF-8");
                return decStr;
            }
            return enStr;
        }catch (Exception e){
            e.printStackTrace();
            return enStr;
        }

    }

    private static boolean isBase64Pattern(String str) {
        Pattern pattern = Pattern.compile(BASE64_PATTERN_STR);
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()) {
            return true;
        }
        return false;
    }

    public static String generateSalt() throws Exception {
        Random rnd = new Random();

        byte[] bt = new byte[0x10];
        rnd.nextBytes(bt);

        String encodeStr = new String(Base64.encodeBase64(bt));

        return encodeStr;
    }

    public static String makePasswordHash(String password, String salt) throws Exception {
        String passwordStr = String.format("%s%s", salt, password);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(passwordStr.getBytes(StandardCharsets.UTF_8));

        String encodeStr = new String(Base64.encodeBase64(md.digest()));

        return encodeStr;
    }

    @Test
    public static void main(String[] args) throws Exception {
        System.out.println("============== START ==============");
        // TODO :: 암호화|복호화 테스트
        String encCEV = "UP!2jBMrn9wXDL5dwwk";
        String encSHA256 = "";
        String decCEV = "eLwRkKfCkL3yyuQxHa3GCjkhuxLRTlnueH19yTqdbHzT+Ww5Ypr+uVru6t8fXC04A0tG34hLZvoyXYNMsH/xzg==";

        System.out.println("encCEV :: " + SecurityUtil.encryptSeedCEV(encCEV));
//        System.out.println("encSHA256 :: " + SecurityUtil.encryptSHA256(encSHA256));
//        System.out.println("decCEV :: " + SecurityUtil.decryptSeedCEV(decCEV));
        System.out.println("============== END ==============");
    }
}
