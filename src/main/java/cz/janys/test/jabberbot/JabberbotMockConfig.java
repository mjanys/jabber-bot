package cz.janys.test.jabberbot;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Martin Janys
 */
@Configuration
@ComponentScan(basePackages = "cz.janys.jabberbot.**")
public class JabberbotMockConfig {

    @Bean
    public ThreadPoolTaskScheduler jabberbotScheduler() {
        return Mockito.mock(ThreadPoolTaskScheduler.class);
    }
    @Bean
    public SubscribableChannel inboundChannel() {
        return Mockito.mock(SubscribableChannel.class);
    }
    @Bean
    public MessageChannel outboundChannel() {
        return Mockito.mock(MessageChannel.class);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
