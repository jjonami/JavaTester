package kr.co.sampler.crawling.crawling;

import kr.co.sampler.crawling.common.model.enums.BrowserType;
import kr.co.sampler.directSend.DirectSendDTO;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CrawlingServiceBySelenium {
    private static final Logger log = LogManager.getLogger(CrawlingServiceBySelenium.class);

    private WebDriver driver;
    private final String url = "https://filfox.info/ko/address/f0984905";
    private final String[] tels = {"010-7522-0013", "010-9161-3169"};

    private final CrawlingMapper mapper;

    @Value("${profile.activate}")
    private String profile;

    public CrawlingServiceBySelenium(CrawlingMapper mapper) {
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
        log.info("[Chrome Driver Path] {}", path);
        System.setProperty("webdriver.chrome.driver", path);

        ChromeOptions options = new ChromeOptions();
//        options.setHeadless(true); // GUI 없이 실행
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

        //해당 엘리먼트가 나타날 때까지 대기하는 WebDriverWait 객체 생성
        WebDriverWait wait = new WebDriverWait(driver, 60);

        // 생성될 때까지 대기
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("table")));

        // 원하는 요소 선택
        WebElement table = driver.findElement(By.tagName("table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        log.info("rows0 : {}", rows.get(0).getText());
        log.info("rows : {}", rows.get(1).getText());


//        WebElement row = rows.get(1);
//        List<WebElement> cells = row.findElements(By.tagName("td"));
//        WebElement date = cells.get(2);
//        WebElement state = cells.get(cells.size() - 1);
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime targetTime = LocalDateTime.parse( date.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        log.info("targetTime :: {}", targetTime);
//        log.info("now :: {}", now);
//
//        long duration = Duration.between(targetTime, now).toMinutes();
//        log.info("Duration :: {}", duration);
//
//        String msg = null;
//        if(duration > 18) msg = "[filfox] 18분 초과됐습니다.";
//        else if (duration > 10) msg = "[filfox] 10분 초과됐습니다.";
//        else if(!state.equals("OK")) msg = "[filfox] 상태가 OK가 아닙니다.";
//        if(msg != null){
//            HashMap<String, Object> parameters = new HashMap<>();
//            parameters.put("content", msg);
//            DirectSendDTO sms = DirectSendDTO.builder()
//                    .alarmCode("1")
//                    .alarmType("sms")
//                    .parameters(parameters)
//                    .build();
//
//            for(String tel : tels){
//                // TODO :: send SMS
//                sms.setAlarmUrl(tel);
//                System.out.println("문자 전송 ==> "+tel);
//                System.out.println("내용 ==> "+sms);
//            }
//        }
        System.out.println("[WebCrawling - end] ==================");

        // WebDriver 종료
        driver.close();
        driver.quit();
    }

    public void insertData(CrawlingData data){
        try {
            mapper.insert(data);
        }catch (Exception e){
            System.out.println(data);
        }

    }
}
