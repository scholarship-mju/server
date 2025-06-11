package mju.scholarship.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile; // Profile 임포트 추가
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration; // RedisStandaloneConfiguration 임포트 추가
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
@EnableCaching // 캐싱 활성화
public class RedisConfig {

    // 로컬 개발 환경을 위한 RedisConnectionFactory (기본 프로파일 또는 'dev' 프로파일)
    @Bean
    @Profile("!ec2") // 'ec2' 프로파일이 아닐 때 이 빈을 사용
    public RedisConnectionFactory standaloneRedisConnectionFactory(
            @Value("${spring.data.redis.host}") String host,
            @Value("${spring.data.redis.port}") int port,
            @Value("${spring.data.redis.password:}") String password // 비밀번호가 없을 수도 있으므로 기본값 설정
    ) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        if (!password.isEmpty()) { // 비밀번호가 설정되어 있다면 설정
            configuration.setPassword(password);
        }
        return new LettuceConnectionFactory(configuration);
    }

    // EC2 서버 (Sentinel) 환경을 위한 RedisConnectionFactory
    @Bean
    @Profile("ec2") // 'ec2' 프로파일일 때 이 빈을 사용
    public RedisConnectionFactory sentinelRedisConnectionFactory(
            @Value("${spring.data.redis.sentinel.master}") String master,
            @Value("${spring.data.redis.sentinel.nodes}") String sentinelNodes, // "host1:port1,host2:port2,..."
            @Value("${spring.data.redis.password:}") String password // 비밀번호가 없을 수도 있으므로 기본값 설정
    ) {
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        configuration.setMaster(master);

        Set<RedisNode> sentinels = Stream.of(sentinelNodes.split(","))
                .map(node -> {
                    String[] parts = node.split(":");
                    return new RedisNode(parts[0], Integer.parseInt(parts[1]));
                })
                .collect(Collectors.toSet());

        configuration.setSentinels(sentinels);
        if (!password.isEmpty()) { // 비밀번호가 설정되어 있다면 설정
            configuration.setPassword(password);
        }
        return new LettuceConnectionFactory(configuration);
    }

    // CacheManager는 RedisConnectionFactory만 주입받으므로,
    // 위에서 어떤 ConnectionFactory가 활성화되든 동일하게 동작합니다.
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }

    // RedisMessageListenerContainer 또한 ConnectionFactory만 주입받으므로 동일하게 동작
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }

    // RedisTemplate 역시 ConnectionFactory만 주입받으므로 동일하게 동작
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}