package kr.co.sampler.crawling.crawling;


import org.springframework.beans.factory.annotation.Autowired;

public class QueueWorker implements Runnable{

    @Autowired
    private CrawlingService service;

    @Override
    public void run() {
        while (true){
            CrawlingData data = QueueManager.dequeue();
            process(data);
        }
    }

    private void process(CrawlingData data){
        service.insertData(data);
    }
}
