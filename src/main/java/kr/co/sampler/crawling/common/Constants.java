package kr.co.sampler.crawling.common;

public class Constants {
    // pw 정규식 (영문자, 숫자 8 ~ 15 // 영문자, 숫자, 대문자, 특수문자 최소 1개)
    public static final String PW_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[%^()_+\\[\\]@$!%*#?&<>'/])[A-Za-z\\d%^()_+\\[\\]@$!%*#?&<>'/]{8,15}$";
    // (3~15자 영문, 한글, 숫자)
    public static final String NICKNAME_PATTERN = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\\s]{3,15}$";

}