package lynx.product_service.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class LiquibaseConfig {

    private final Environment env;

    @Bean
    @Primary
    @LiquibaseDataSource
    public DataSource liquibaseDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        String driver = env.getProperty("spring.datasource.driver-class-name");
        if (driver == null) {
            throw new IllegalStateException("Driver class name is not set");
        }
        dataSource.setDriverClassName(driver);
        return dataSource;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @Primary
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(liquibaseDataSource());
        liquibase.setChangeLog("classpath:db/changelog/master.xml");
        liquibase.setContexts("development,test,production");
        liquibase.setDefaultSchema("public");
        return liquibase;
    }
}