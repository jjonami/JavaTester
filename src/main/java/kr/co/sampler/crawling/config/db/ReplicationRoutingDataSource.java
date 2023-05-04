package kr.co.sampler.crawling.config.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger log = LogManager.getLogger(ReplicationRoutingDataSource.class);

    @Value("${profile.activate}")
    private String profileActivated;

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if(isReadOnly) {
            if(!isProd()) {
                log.info("slave");
            }
            return "slave";
        } else {
            if(!isProd()) {
                log.info("master");
            }
            return "master";
        }
    }

    private boolean isProd() {
        if(profileActivated.equals("prod")) {
            return true;
        }
        return false;
    }

}
