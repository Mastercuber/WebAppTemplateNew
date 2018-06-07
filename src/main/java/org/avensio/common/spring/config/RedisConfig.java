package org.avensio.common.spring.config;

import lombok.extern.java.Log;

import org.avensio.common.message.MultiCastPostInfoToFollower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@Profile({"redis"})
@PropertySource({ "classpath:application.properties" })
@Log
public class RedisConfig {
    @Value("${spring.redis.host:localhost}")
    String host;
    @Value("${spring.redis.password:secret}")
    String password;
    @Value("${spring.redis.port:6379}")
    int port;

    @Bean
    @Primary
    public RedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    @DependsOn("connectionFactory")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setEnableDefaultSerializer(true);
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();

        return template;
    }

    @Bean
    @DependsOn("connectionFactory")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory());
        stringRedisTemplate.setEnableDefaultSerializer(true);
        stringRedisTemplate.afterPropertiesSet();

        return stringRedisTemplate;
    }

    @Bean
    @DependsOn("connectionFactory")
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.addMessageListener(listenerAdapter, new PatternTopic("newPostsQueue"));
        container.afterPropertiesSet();

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MultiCastPostInfoToFollower receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    MultiCastPostInfoToFollower receiver() {
        return new MultiCastPostInfoToFollower();
    }
}
