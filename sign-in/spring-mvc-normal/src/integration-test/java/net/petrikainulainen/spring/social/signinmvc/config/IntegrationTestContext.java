package net.petrikainulainen.spring.social.signinmvc.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Petri Kainulainen
 */
@Configuration
public class IntegrationTestContext {

    private static final String LIQUIBASE_CHANGELOG_FILE = "classpath:changelog.xml";
    private static final String LIQUIBASE_CONTEXT = "integrationtest";

    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(LIQUIBASE_CHANGELOG_FILE);
        liquibase.setContexts(LIQUIBASE_CONTEXT);

        return liquibase;
    }
}
