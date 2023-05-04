package kr.co.sampler.crawling.crawling;

import kr.co.sampler.crawling.common.model.enums.BrowserType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingScheduler {
    private final CrawlingService service;

    @Scheduled(initialDelay = 180000, fixedRate = 3000)
    public void crawlingPerSec() { service.crawling(BrowserType.CHROME); }

//    @Scheduled(cron = "* * * * * *")
//    public void insertData(){
//        if(!QueueManager.isEmpty()){
//            service.insertData(QueueManager.poll());
//        }
//    }

}
