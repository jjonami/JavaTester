package kr.co.sampler.crawling.common;

import kr.co.sampler.crawling.common.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private static final Logger log = LogManager.getLogger(DataLoader.class);

    @Value("${profile.activate}")
    private String profileActivated;

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("profileActivated :: " + profileActivated);
        System.out.println("start time :: " + DateUtil.getToday("yyyy-MM-dd HH:mm:ss"));
        log.info("-------------------- end init --------------------");
    }

    private boolean isLocal(){
        return profileActivated.equalsIgnoreCase("local");
    }
}
