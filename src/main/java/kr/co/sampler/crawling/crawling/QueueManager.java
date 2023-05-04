package kr.co.sampler.crawling.crawling;

import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

@AllArgsConstructor
public class QueueManager {
    private static QueueManager instance = null;
    private static Queue<CrawlingData> queue = new LinkedList<>();

    public static QueueManager getInstance(){
        if(instance == null){
            instance = new QueueManager();
        }
        return instance;
    }

    public void add(CrawlingData data){
        queue.add(data);
    }

    public static CrawlingData dequeue(){
        synchronized (queue){
            while (queue.isEmpty()){
                try {
                    queue.wait();
                }catch (InterruptedException e){}
            }
        }
        return queue.poll();
    }

    public void startQueueWorker(){
        QueueWorker worker = new QueueWorker();
        new Thread(worker).start();
    }
}
