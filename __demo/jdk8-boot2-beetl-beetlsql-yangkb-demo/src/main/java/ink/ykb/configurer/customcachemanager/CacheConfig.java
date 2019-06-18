package ink.ykb.configurer.customcachemanager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class CacheConfig {

	@Value("${springext.cache.redis.topic:cache}")
	private String topicName;
	
	@Bean
	public TwoLevelCacheManager getTwoLevelCacheManager(StringRedisTemplate redisTemplate){
		RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
		
		ClassLoader loader = this.getClass().getClassLoader();
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
		SerializationPair<Object> pair = SerializationPair.fromSerializer(jdkSerializer);
		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
		
		
		TwoLevelCacheManager cacheManager = new TwoLevelCacheManager(redisTemplate, writer, cacheConfig);
		
		return cacheManager;
	}
	
	
	
	
	
	@Bean
	@Qualifier("twoLevelCacheMessageListener")
	public MessageListenerAdapter listenerAdapter(final TwoLevelCacheManager cacheManager){
		
		MessageListenerAdapter listenerAdapter =  new MessageListenerAdapter(new MessageListener() {
			
			@Override
			public void onMessage(Message message, byte[] pattern) {
				byte[] channel = message.getChannel();
				byte[] bs = message.getBody();
				
				String topic = null;
				String cacheName = null;
				try {
					topic = new String(channel,"UTF-8");
					
					if(topic.equals(topicName)){
						//JDK反序列化
//						ClassLoader loader = this.getClass().getClassLoader();
//						JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
//						cacheName = (String)jdkSerializer.deserialize(bs);
						
						cacheName = new String(bs,"UTF-8");
						
						cacheManager.receiver(cacheName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					System.out.println("频道："+topic+"，消息："+cacheName);
				}
				
			}
		});
		
		return listenerAdapter ;
	}
	
	class TwoLevelCacheManager extends RedisCacheManager{
		
		private StringRedisTemplate redisTemplate;
		
		public TwoLevelCacheManager(StringRedisTemplate redisTemplate,RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
			super(cacheWriter, defaultCacheConfiguration);
			
			this.redisTemplate = redisTemplate;
		}

		@Override
		protected Cache decorateCache(Cache cache) {
			return new RedisAndLocalCache(this,(RedisCache)cache);
		}

		public void publishMeaasge(String cacheName){
			this.redisTemplate.convertAndSend(topicName, cacheName);
		}

		
		public void receiver(String name){
			RedisAndLocalCache cache = (RedisAndLocalCache)this.getCache(name);
			if(cache != null){
				cache.clearLocal();
			}
		}
	
	}
	
}
