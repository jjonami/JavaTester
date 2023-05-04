package kr.co.sampler.crawling.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResultCode {
    /*
     *  HTTP STATUS CODE
     *
     *      400 = 잘못된 요청 - 공통
     *      401 = 유효성 요청 - JWT, Request Header
     *      403 = 권한
     *      404 = Not Found
     *      409 = 내부 검증 로직 ( if/else )
     *      500 = 통신
     */

    CODE_SUCCESS(200, 200, "success")
    ,CODE_500(500, 500, "확인되지 않은 오류입니다. 잠시 후 다시 시도해주세요.")
    ,CODE_401( 401,401, "인증되지 않은 키 입니다.")
    ,CODE_403( 403,403, "허용되지 않은 요청입니다.")

    ;
    private Integer httpStatus;
    private Integer code;
    private String msg;

    ResultCode( Integer httpStatus, Integer code, String msg){
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = msg;
    }

    public Integer getHttpStatus(){
        return httpStatus;
    }
    public Integer getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

    public static ResultCode getErrorCodeByCode(Integer code) {
        return Arrays.stream(ResultCode.values()).filter(x -> x.getCode().equals(code)).findFirst().get();
    }
}
