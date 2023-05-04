package kr.co.sampler.crawling.crawling;

import kr.co.sampler.crawling.common.model.enums.BrowserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrawlingService {
    private static final Logger log = LogManager.getLogger(CrawlingService.class);

    private static WebDriver driver;
    private static final String url = "https://mosdex.com/";

    private final CrawlingMapper mapper;

    @Value("${profile.activate}")
    private String profile;

    public CrawlingService(CrawlingMapper mapper) {
        this.mapper = mapper;
    }

    private void setFirefox(){
        String path = profile.equals("dev") ? "": "libs/geckodriver/geckodriver";
        System.setProperty("webdriver.gecko.driver", path);

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true); // GUI 없이 실행
        options.addArguments("--remote-allow-origins=*");
        log.info("[Firefox] 옵션 설정");

        // WebDriver 객체 생성
        driver = new FirefoxDriver(options);
        log.info("[Firefox] driver 생성");

        // 웹사이트 접속
        driver.get(url);
        try{
            Thread.sleep(3000); //브라우저 로딩될때까지
        }catch (Exception e){}
    }

    private void setChrome(){
        String path = profile.equals("dev") ? "/usr/bin/chromedriver": "libs/chromedriver/chromedriver";
        System.setProperty("webdriver.chrome.driver", path);


        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true); // GUI 없이 실행
        options.addArguments("--remote-allow-origins=*");
        log.info("[Chrome] 옵션 설정");

        // WebDriver 객체 생성
        driver = new ChromeDriver(options);
        log.info("[Chrome] driver 생성");

        // 웹사이트 접속
        driver.get(url);
        try{
            Thread.sleep(3000); //브라우저 로딩될때까지
        }catch (Exception e){}
    }

    public void crawling(BrowserType browser){
        System.out.println("[WebCrawling - start] ==================");
        if(driver == null){
            switch (browser){
                case CHROME: setChrome(); break;
                case FIREFOX: setFirefox(); break;
            }
        }

        // 원하는 요소 선택
        WebElement container = driver.findElement(By.className("term-container"));

        // 해당 엘리먼트가 나타날 때까지 대기하는 WebDriverWait 객체 생성
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // span 태그가 생성될 때까지 대기
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".term-container .tx")));

        // span 태그 찾기
        List<WebElement> spanElements = container.findElements(By.cssSelector(".tx"));
        for(WebElement e : spanElements){
            String timestamp = null;
            String symbol = null;
            String amount = null;
            String transaction = null;
            String from = null;
            String to = null;
            String profit = null;
            String mxid = null;

            try{
                timestamp = e.findElement(By.className("tx-timestamp")).getText();
                symbol = e.findElement(By.className("tx-symbol")).getText();
                amount = e.findElement(By.className("tx-amount")).getText();
                transaction = e.findElement(By.className("tx-transaction")).getText();
                from = e.findElement(By.className("tx-from")).findElement(By.className("tx-address")).getText();
                to = e.findElement(By.className("tx-to")).findElement(By.className("tx-address")).getText();
                profit = e.findElement(By.className("tx-profit")).getText();
                mxid = e.findElement(By.className("tx-mxid-val")).getText();

            } catch (Exception ex){}

            if(timestamp != null
                    && symbol != null
                    && amount != null
                    && transaction != null
                    && from != null
                    && to != null
                    && profit != null
                    && mxid != null
            ){
                System.out.println(timestamp);
                System.out.println(symbol);
                System.out.println(amount);
                System.out.println(transaction);
                System.out.println(from);
                System.out.println(to);
                System.out.println(profit);
                System.out.println(mxid);

                CrawlingData data = CrawlingData.builder()
                        .timestamp(timestamp)
                        .symbol(symbol)
                        .amount(amount)
                        .transaction(transaction)
                        .from(from)
                        .to(to)
                        .profit(profit)
                        .mxid(mxid)
                        .build();
//                QueueManager queue = QueueManager.getInstance();
//                queue.add(data);
                mapper.insert(data);
            }
        }

        System.out.println("[WebCrawling - end] ==================");

        // WebDriver 종료
//            driver.close();
//            driver.quit();
    }

    public void insertData(CrawlingData data){
        try {
            mapper.insert(data);
        }catch (Exception e){
            System.out.println(data);
        }

    }
}
