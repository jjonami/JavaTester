package kr.co.sampler.crawling.crawling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CrawlingServiceByJsoup {
    private static final Logger log = LogManager.getLogger(CrawlingServiceByJsoup.class);

    private static final String url = "https://filfox.info/ko/address/f0984905";

    public static void crawling(){
        System.out.println("[WebCrawling - start] ==================");
        try {
            // HTML 가져오기
            Connection connect = Jsoup.connect(url);
            Thread.sleep(30000);
            Document doc = connect.get();


            // 테이블 선택
            Element table = doc.select("table").first();

            // 테이블 행 선택
            Elements rows = table.select("tr");
            for(Element row : rows){
                System.out.println("-------------");
                System.out.println(row);
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[WebCrawling - end] ==================");
    }

}
