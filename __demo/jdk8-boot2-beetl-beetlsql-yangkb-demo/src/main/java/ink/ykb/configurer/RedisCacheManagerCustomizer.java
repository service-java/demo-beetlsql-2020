package ink.ykb.configurer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class RedisCacheManagerCustomizer{

//	 在自定义RedisCacheManager后此customize不会调用，
//	但不使用自定义的RedisCacheManager，会出现classLoad加载器不同导致的类型转换异常
// 所以此处只能自定义RedisCacheManager
//	@Bean
	public CacheManagerCustomizer<RedisCacheManager> cacheManagerCustomizer(){
		return new CacheManagerCustomizer<RedisCacheManager>() {
			
			@Override
			public void customize(RedisCacheManager cacheManager) {
				//设置缓存超时时间
				Map<String, RedisCacheConfiguration> cacheConfigurations = cacheManager.getCacheConfigurations();
//				此 cacheConfigurations为空，有bug？
//				cacheConfigurations.get("user").entryTtl(Duration.ofSeconds(10));
				
				
			}
		};
	}
	
	
//	@Bean
	public RedisCacheManager getRedisCacheManager(RedisTemplate<Object,Object>  redisTemplate){
		RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
		ClassLoader loader = this.getClass().getClassLoader();
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
		SerializationPair<Object> pair = SerializationPair.fromSerializer(jdkSerializer);
		
		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
		cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(3600));//设置所有的超时时间

		//设置单个缓存的超时时间
		Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
		initialCacheConfigurations.put("user",cacheConfig.entryTtl(Duration.ofSeconds(10)));
		
		
		RedisCacheManager cacheManager = new RedisCacheManager(cacheWriter, cacheConfig,initialCacheConfigurations);
		
		return cacheManager;
	}
	
}
