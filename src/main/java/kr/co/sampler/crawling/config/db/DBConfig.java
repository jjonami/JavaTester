package kr.co.sampler.crawling.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(value="kr.co.wifree.portal.rest.**.mapper", sqlSessionFactoryRef="sqlSessionFactoryMaster")
public class DBConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String datasourceDriverClassName;

    @Value("${spring.datasource.hikari.jdbc-url}")
    private String datasourceUrl;

    @Value("${spring.datasource.hikari.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.hikari.password}")
    private String datasourcePassword;


    @Value("${readonly.datasource.hikari.driver-class-name}")
    private String replicationDatasourceDriverClassName;

    @Value("${readonly.datasource.hikari.jdbc-url}")
    private String replicationDatasourceUrl;

    @Value("${readonly.datasource.hikari.username}")
    private String replicationDatasourceUsername;

    @Value("${readonly.datasource.hikari.password}")
    private String replicationDatasourcePassword;

    // hikariConfig.connection.testQuery=SELECT 1
    @Value("${hikariConfig.connection.testQuery}")
    private String testQuery;

    // hikariConfig.connection.cachePrepStmts=true
    @Value("${hikariConfig.connection.cachePrepStmts}")
    private String cachePrepStmts;

    // hikariConfig.connection.prepStmtCacheSize=250
    @Value("${hikariConfig.connection.prepStmtCacheSize}")
    private String prepStmtCacheSize;

    // hikariConfig.connection.prepStmtCacheSqlLimit=2048
    @Value("${hikariConfig.connection.prepStmtCacheSqlLimit}")
    private String prepStmtCacheSqlLimit;

    // hikariConfig.connection.useServerPrepStmts=true
    @Value("${hikariConfig.connection.useServerPrepStmts}")
    private String useServerPrepStmts;

    // [[
    // spring.datasource.hikari.leak-detection-threshold=60000
    @Value("${spring.datasource.hikari.leak-detection-threshold}")
    private Long hikariLeakDetectionThreshold;

    // spring.datasource.hikari.idle-timeout=10000
    @Value("${spring.datasource.hikari.idle-timeout}")
    private Long hikariIdleTimeout;

    // spring.datasource.hikari.max-lifetime=420000
    @Value("${spring.datasource.hikari.max-lifetime}")
    private Long hikariMaxLifetime;

    // spring.datasource.hikari.connection-timeout=10000
    @Value("${spring.datasource.hikari.connection-timeout}")
    private Long hikariConnectionTimeout;

    // spring.datasource.hikari.validation-timeout=10000
    @Value("${spring.datasource.hikari.validation-timeout}")
    private Long hikariValidationTimeout;

    // spring.datasource.hikari.maximum-pool-size=2
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int hikariMaximumPoolSize;
    // ]]

    public static final String MASTER_DATASOURCE = "masterDataSource";
    public static final String SLAVE_DATASOURCE = "slaveDataSource";
    public static final String ROUTING_DATASOURCE = "routingDataSource";

    public static final String MASTER_HIKARI = "masterHikariConfig";
    public static final String SLAVE_HIKARI = "slaveHikariConfig";

    public static final String CONFIG_XML_PATH = "classpath:config/sql-mapper-config.xml";
    public static final String SQL_XML_PATH = "classpath:sql/**/*_SQL.xml";

    @Bean(name = "masterHikariConfig")
    public HikariConfig masterHikariConfig(){
        HikariConfig hikariConfig = new HikariConfig();
        CryptoDataSource dataSource = new CryptoDataSource();

        dataSource.setDriverClassName(datasourceDriverClassName);
        dataSource.setUrl(datasourceUrl);
        dataSource.setUsername(datasourceUsername);
        dataSource.setPassword(datasourcePassword);
        hikariConfig.setDataSource(dataSource);

        hikariConfig.setDriverClassName(datasourceDriverClassName);
        hikariConfig.setJdbcUrl(dataSource.getUrl());
        hikariConfig.setUsername(dataSource.getUsername());
        hikariConfig.setPassword(dataSource.getPassword());

        System.out.println("master db url :: " + dataSource.getUrl());

        hikariConfig.setConnectionTestQuery(testQuery);
        hikariConfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", useServerPrepStmts);

        hikariConfig.setLeakDetectionThreshold(hikariLeakDetectionThreshold);
        hikariConfig.setMaxLifetime(hikariMaxLifetime);
        hikariConfig.setConnectionTimeout(hikariConnectionTimeout);
        hikariConfig.setValidationTimeout(hikariValidationTimeout);
        hikariConfig.setMaximumPoolSize(hikariMaximumPoolSize);
        return hikariConfig;
    }

    @Bean(name = "slaveHikariConfig")
    public HikariConfig slaveHikariConfig(){
        HikariConfig hikariConfig = new HikariConfig();
        CryptoDataSource dataSource = new CryptoDataSource();

        dataSource.setDriverClassName(replicationDatasourceDriverClassName);
        dataSource.setUrl(replicationDatasourceUrl);
        dataSource.setUsername(replicationDatasourceUsername);
        dataSource.setPassword(replicationDatasourcePassword);
        hikariConfig.setDataSource(dataSource);

        hikariConfig.setDriverClassName(replicationDatasourceDriverClassName);
        hikariConfig.setJdbcUrl(dataSource.getUrl());
        hikariConfig.setUsername(dataSource.getUsername());
        hikariConfig.setPassword(dataSource.getPassword());

        System.out.println("slave db url :: " + dataSource.getUrl());

        hikariConfig.setConnectionTestQuery(testQuery);
        hikariConfig.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        hikariConfig.addDataSourceProperty("useServerPrepStmts", useServerPrepStmts);
        hikariConfig.setReadOnly(true);

        hikariConfig.setLeakDetectionThreshold(hikariLeakDetectionThreshold);
        hikariConfig.setMaxLifetime(hikariMaxLifetime);
        hikariConfig.setConnectionTimeout(hikariConnectionTimeout);
        hikariConfig.setValidationTimeout(hikariValidationTimeout);
        hikariConfig.setMaximumPoolSize(hikariMaximumPoolSize);

        return hikariConfig;
    }

    //    @Primary
    @Bean(name = "masterDataSource")
    public DataSource dataSource(@Qualifier(MASTER_HIKARI) HikariConfig masterHikariConfig) { // 위에서 만든 설정 파일을 이용해서 디비와 연결하는 데이터 소스를 생성
        DataSource hikariDataSource = new HikariDataSource(masterHikariConfig);
        return hikariDataSource;
    }

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource(@Qualifier(SLAVE_HIKARI) HikariConfig slaveHikariConfig) {
        DataSource hikariDataSource = new HikariDataSource(slaveHikariConfig);
        return hikariDataSource;
    }

//    @Primary
    @Bean(name = "routingDataSource")
    @DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE})
    public DataSource routingDataSource(
            @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
            @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource
    ) {

        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> datasourceMap = new HashMap() {
            {
                put("master", masterDataSource);
                put("slave", slaveDataSource);
            }
        };
//        datasourceMap.put("leave", leaveDataSource);

        routingDataSource.setTargetDataSources(datasourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @Primary
    @Bean
    public DataSource routingLazyDataSource(@Qualifier(ROUTING_DATASOURCE) DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Primary
    @Bean(name = "sqlSessionFactoryMaster")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(ROUTING_DATASOURCE) DataSource routingDataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(routingLazyDataSource(routingDataSource));

        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(CONFIG_XML_PATH));
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(SQL_XML_PATH));

        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlSessionTemplateMaster")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryMaster") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "transactionManagerMaster")
    public PlatformTransactionManager transactionManagerMaster(@Qualifier(ROUTING_DATASOURCE) DataSource routingDataSource) {
        return new DataSourceTransactionManager(routingLazyDataSource(routingDataSource));
    }
}
