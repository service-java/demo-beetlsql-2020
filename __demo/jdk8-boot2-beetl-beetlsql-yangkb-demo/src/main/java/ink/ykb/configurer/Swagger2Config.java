package ink.ykb.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config{
	
	@Bean
	public Docket customDocket() {
		return createDocket("All", null);
	}
	
	@Bean
    public Docket customDocket1() {
        return createDocket("User", "/user/**");
        
    }
	@Bean
	public Docket customDocket2() {
		return createDocket("Redis", "/redis/**");
	}


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
        		//页面标题
                .title("Springboot测试系统API文档")
                //创建人
                .contact(new Contact("马丁的早晨", "http://ykb.ink", "y@kbblog.cn"))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }

	private Docket createDocket(String groupName ,String path){
		ApiSelectorBuilder asb = new Docket(DocumentationType.SWAGGER_2)
				.groupName(groupName)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("ink.ykb.controller"));
		if(path == null){
			return asb.build();
		}else{
			return asb.paths(PathSelectors.ant(path)).build();
		}
	}
    

    
}
