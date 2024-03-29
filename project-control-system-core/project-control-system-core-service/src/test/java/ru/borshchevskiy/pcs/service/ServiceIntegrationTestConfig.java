package ru.borshchevskiy.pcs.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import ru.borshchevskiy.pcs.common.config.PasswordEncoderConfig;
import ru.borshchevskiy.pcs.entities.EntityConfig;
import ru.borshchevskiy.pcs.repository.RepositoryConfig;
import ru.borshchevskiy.pcs.service.services.email.impl.EmailServiceImpl;

@SpringBootConfiguration
@EnableAutoConfiguration
@PropertySource("classpath:application-test.properties")
@Import({RepositoryConfig.class,
        EntityConfig.class,
        PasswordEncoderConfig.class,
        EmailServiceImpl.class,
        RabbitConfig.class
})

public class ServiceIntegrationTestConfig extends ServiceConfig {


}

