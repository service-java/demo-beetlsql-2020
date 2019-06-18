package ink.ykb.configurer;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	//定义一个Redis的频道，默认叫cache,用于pub/sub
	@Value("${springext.cache.redis.topic:cache}")
	private String topicName;
	
	
	
	@Bean("redisTemplate")
	public RedisTemplate<Object,Object> getRedisTemplate(RedisConnectionFactory connectionFactory) throws UnknownHostException{
		
		RedisTemplate<Object,Object> template = new RedisTemplate<Object,Object>();
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		//解决应类加载器不同（RestartClassLoader）导致的转换类型异常
		ClassLoader loader = this.getClass().getClassLoader();
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
		
		template.setConnectionFactory(connectionFactory);
		template.setDefaultSerializer(jdkSerializer);
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(jdkSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(jdkSerializer);
		
		return template;
	}
	
	@Bean("jsonRedisTemplate")
	public RedisTemplate<Object,Object> getJsonRedisTemplate(RedisConnectionFactory connectionFactory) throws UnknownHostException{
		
		RedisTemplate<Object,Object> template = new RedisTemplate<Object,Object>();
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//		Jackson2JsonRedisSerializer<Object> jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
		
		template.setConnectionFactory(connectionFactory);
		
		template.setDefaultSerializer(jsonSerializer);
		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(jsonSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(jsonSerializer);
		
		return template;
	}
	
	@Bean
	@Qualifier("myRedisChannelListener")
	public MessageListenerAdapter getMessageListenerAdapter(){
		
		MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(new MyRedisChannelListener());
		
		listenerAdapter.setSerializer(new Jackson2JsonRedisSerializer<>(Object.class) );
		
		return listenerAdapter;
	}
	
	
	
	@Bean
	public RedisMessageListenerContainer getRedisMessageListenerContainer(
			RedisConnectionFactory connectionFactory,
			@Qualifier("twoLevelCacheMessageListener") MessageListenerAdapter twoLevelCacheMessageListener,
			@Qualifier("myRedisChannelListener") MessageListenerAdapter myRedisChannelListener
			){
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(myRedisChannelListener,new PatternTopic("news*"));//news消息通知
		container.addMessageListener(twoLevelCacheMessageListener,new PatternTopic(topicName));//cache消息通知
		
		return container;
		
	}
	
}
