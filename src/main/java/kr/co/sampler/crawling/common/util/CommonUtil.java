package kr.co.sampler.crawling.common.util;

import kr.co.sampler.crawling.common.Constants;
import kr.co.sampler.crawling.common.model.exception.CustomException;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonUtil {
    public static DateTimeFormatter dtf_dash_with_time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.KOREA);
    public static DateTimeFormatter dtf_dot = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREA);
    public static DateTimeFormatter dtf_only_time = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREA);

    public static void printError(CustomException e, Logger log){
        log.error("Exception ============ "+e.getCode());
    }

    // 이메일 형식 확인
    public static boolean isValidEmail(String email){
        String emailRegx = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegx);
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    // 휴대폰 형식 확인
    public static boolean isValidPhoneNo(String phoneNo){
        // 000 000[0] 0000
        Pattern pattern = Pattern.compile("^01(?:0|1|[6-9])([0-9]{3,4})([0-9]{4})$");
        Matcher phoneNoMatcher = pattern.matcher(phoneNo);
        return phoneNoMatcher.find();
    }

    // 패스워드 형식 확인
    public static boolean isValidPw(String pw){
        // pw 정규식 확인 :: 영문자, 숫자 8 ~ 15 // 영문자, 숫자, 대문자, 특수문자 최소 1개
        String mbPwRegx = Constants.PW_PATTERN;
        Pattern mbPwPattern = Pattern.compile(mbPwRegx);
        Matcher mbPwMatcher = mbPwPattern.matcher(pw);
        return mbPwMatcher.matches();
    }

    // 닉네임 형식 확인
    public static boolean isValidNickName(String nickName){
        String nickNameRegx = Constants.NICKNAME_PATTERN;
        Pattern nickNamePattern = Pattern.compile(nickNameRegx);
        Matcher nickNameMatcher = nickNamePattern.matcher(nickName);
        return nickNameMatcher.matches();
    }

    // 휴대폰번호 앞부분 0 제거
    public static String removeZeroFromPhoneNo(String phoneNo){
        String regx = "^0+";
        return phoneNo.replaceAll(regx, "");
    }

    public static boolean checkYn(String value) {
        return value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("N");
    }

    public static String generateTempPw(){

        List<String> enS = Arrays.asList("a","b","c","d","e","f","c","h","j","k","l","m","n","p","q","r","s","t","u","v","w","x","y","z");
        List<String> num = Arrays.asList("0","1","2","3","4","5","6","7","8","9");
        List<String> enL = Arrays.asList("A","B","C","D","E","F","C","H","J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z");
        List<String> sp = Arrays.asList("!","@","#","%","^","*","-","~");

        Collections.shuffle(enS);
        Collections.shuffle(num);
        Collections.shuffle(enL);
        Collections.shuffle(sp);

        return enS.get(0)+num.get(1)+sp.get(2)+enL.get(3)+enS.get(4)+enS.get(5)+sp.get(6)+sp.get(7)+num.get(8)+enL.get(9)+enS.get(10);
    }

    public static boolean isDateCheck (String date,String format){
        /*
            SimpleDateFormat 종류
            대소문자에 따라 의미하는게 다르니 잘 보고 써주세요.
           y : 연
           M : 월
           d : 일
           E : 요일명
           u : 요일(1=월요일,2=화요일....7=일요일)
           a : am pm
           H : 24시간 기준 (0~23)
           k : 24시간 기준 (1~24)
           K : 오전 오후 구분없는 시간 (0~11)
           h : 오전 오후 구분없는 시간 (1~12)
           m : 분
           s : 초
           S : 1/1000초
           z : 타임존
         */
        try {
            SimpleDateFormat dateFormatParser = new SimpleDateFormat(format); //검증할 날짜 포맷 설정
            dateFormatParser.setLenient(false); //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
            dateFormatParser.parse(date); //대상 값 포맷에 적용되는지 확인
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean startEndCompareTo (String startDate,String endDate,String format){
        /*
            SimpleDateFormat 종류
            대소문자에 따라 의미하는게 다르니 잘 보고 써주세요.
           y : 연
           M : 월
           d : 일
           E : 요일명
           u : 요일(1=월요일,2=화요일....7=일요일)
           a : am pm
           H : 24시간 기준 (0~23)
           k : 24시간 기준 (1~24)
           K : 오전 오후 구분없는 시간 (0~11)
           h : 오전 오후 구분없는 시간 (1~12)
           m : 분
           s : 초
           S : 1/1000초
           z : 타임존
         */
        try {
            SimpleDateFormat dateFormatParser = new SimpleDateFormat(format); //검증할 날짜 포맷 설정
            Date start = new Date(dateFormatParser.parse(startDate).getTime());
            Date end = new Date(dateFormatParser.parse(endDate).getTime());
            int compare = end.compareTo(start);

            //start date 가 end date 보다 작을 경우 false
            if(compare < 0){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
