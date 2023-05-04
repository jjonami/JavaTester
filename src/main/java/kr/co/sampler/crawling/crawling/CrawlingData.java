package kr.co.sampler.crawling.crawling;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CrawlingData {
    private String timestamp;
    private String symbol;
    private String amount;
    private String transaction;
    private String from;
    private String to;
    private String profit;
    private String mxid;
}
