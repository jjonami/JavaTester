package kr.co.sampler.crawling.common.model.exception;

import kr.co.sampler.crawling.common.ResultCode;
import lombok.Getter;

@Getter
public class CustomException extends Exception{
    private final ResultCode code;
    public CustomException(ResultCode code) {
        super();
        this.code = code;
    }
}
