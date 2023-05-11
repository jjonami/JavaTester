package kr.co.sampler.crawling.crawling;

import kr.co.sampler.crawling.common.model.enums.BrowserType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingScheduler {
    private final CrawlingServiceBySelenium service;

    @Scheduled(initialDelay = 5000, fixedRate = 300000)
//    @Scheduled(cron = "0 18,48 * * * *")
    public void jobCrawling(){
        service.crawling(BrowserType.CHROME);
    }

}
