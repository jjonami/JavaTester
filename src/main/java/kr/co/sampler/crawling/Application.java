package kr.co.sampler.crawling;

import kr.co.sampler.crawling.crawling.QueueManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan(value = {"kr.co.sampler.crawling"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        QueueManager queueManager = new QueueManager();
        queueManager.startQueueWorker();
    }


}
