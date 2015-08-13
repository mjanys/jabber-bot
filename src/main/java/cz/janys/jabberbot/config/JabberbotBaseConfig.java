package cz.janys.jabberbot.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Martin Janys
 */
@Configuration
@ComponentScan(basePackages = "cz.janys.jabberbot.**")
//@PropertySource("classpath:jabberbot-default.properties")
@ImportResource("classpath:jabberbot-xmpp-context.xml")
public abstract class JabberbotBaseConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
