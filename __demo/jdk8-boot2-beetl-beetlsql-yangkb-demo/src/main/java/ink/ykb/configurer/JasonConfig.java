package ink.ykb.configurer;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JasonConfig {
	
//	/**
//	 * 自定义ObjectMapper方式一
//	 * @return
//	 */
//	@Bean
//	@ConditionalOnMissingBean(ObjectMapper.class)
//	public ObjectMapper getObjectMapper( ) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);  
//		return objectMapper;
//	}
//	/**
//	 * 自定义ObjectMapper方式二
//	 * @return
//	 */
//		@Bean
//		public Jackson2ObjectMapperBuilderCustomizer  getJackson2ObjectMapperBuilderCustomizer( ) {
//			 return new Jackson2ObjectMapperBuilderCustomizer() {
//				
//				@Override
//				public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
//					jacksonObjectMapperBuilder.defaultViewInclusion(true);
//				}
//			};
//		}

}
