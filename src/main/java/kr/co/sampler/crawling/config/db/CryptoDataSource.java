package kr.co.sampler.crawling.config.db;

import kr.co.sampler.crawling.common.util.SecurityUtil;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class CryptoDataSource extends DriverManagerDataSource {

    private static final Logger log = LogManager.getLogger(CryptoDataSource.class);

    @SneakyThrows
    @Override
    public synchronized  void setUrl(String url) {
        try {
            super.setUrl(SecurityUtil.decryptSeedCEV(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void setUsername(String username) {
        try {
            super.setUsername(SecurityUtil.decryptSeedCEV(username));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Override
    public void setPassword(String password) {
        try {
            super.setPassword(SecurityUtil.decryptSeedCEV(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
