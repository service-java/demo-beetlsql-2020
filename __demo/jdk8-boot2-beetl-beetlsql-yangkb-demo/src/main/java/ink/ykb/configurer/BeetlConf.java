package ink.ykb.configurer;

import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

@Configuration
public class BeetlConf{

	//配置文件位置
	@Value("${beetl.propertiesPath}") 
	private String propertiesPath;
	
	//消息模板位置
	@Value("${beetl.templatesPath}") 
	private String templatesPath;
	
	//MVC视图模板配置路径
	@Value("${beetl.templatesViewPath}") 
	private String templatesViewPath;
	
	//MVC视图模板后缀路径
	@Value("${beetl.templatesViewSuffix}") 
	private String templatesViewSuffix;
	
	/**
	 * 邮件短信等模板消息配置
	 * @param beetlGroupUtilConfiguration
	 * @return
	 */
   @Bean(name = "groupTemplate")
    public GroupTemplate getGroupTemplate(@Qualifier("beetlGroupTemplateConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
	   return beetlGroupUtilConfiguration.getGroupTemplate();
   }
	
	/**
	 * 邮件短信等模板消息配置
	 * @return
	 */
   @Bean(name = "beetlGroupTemplateConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
            BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
            ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
            
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if(loader==null){
                loader = BeetlConf.class.getClassLoader();
            }
            beetlGroupUtilConfiguration.setConfigFileResource(patternResolver.getResource(propertiesPath));
            ClasspathResourceLoader cploder = new ClasspathResourceLoader(loader,templatesPath);
            beetlGroupUtilConfiguration.setResourceLoader(cploder);
            
            beetlGroupUtilConfiguration.init();
            
            return beetlGroupUtilConfiguration;
    }
   
	   
   /**
    * 视图模板配置
    * @return
    */
    @Bean(name = "beetlViewConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupViewConfiguration() {
            BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
            ResourcePatternResolver patternResolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
            
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if(loader==null){
                loader = BeetlConf.class.getClassLoader();
            }
            beetlGroupUtilConfiguration.setConfigFileResource(patternResolver.getResource(propertiesPath));
            ClasspathResourceLoader cploder = new ClasspathResourceLoader(loader,templatesViewPath);
            beetlGroupUtilConfiguration.setResourceLoader(cploder);
            
            beetlGroupUtilConfiguration.init();
            
            return beetlGroupUtilConfiguration;
    }
    /**
     * 视图模板配置
     * @return
     */
    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlViewConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
            BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
            beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
            beetlSpringViewResolver.setOrder(0);
            beetlSpringViewResolver.setSuffix(templatesViewSuffix);
            beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
            return beetlSpringViewResolver;
    }
}
