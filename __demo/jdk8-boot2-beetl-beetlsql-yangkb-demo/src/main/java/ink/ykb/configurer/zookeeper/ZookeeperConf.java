package ink.ykb.configurer.zookeeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Aspect
@Order(101)
public class ZookeeperConf {
	
	
		@Value("${zk.url}")
		private String zkUrl;
		
		@Value("${zk.service.address}")
		private String zkServiceAddress;
		@Value("${zk.service.port}")
		private Integer zkServicePort;
		
		private Logger logger = LoggerFactory.getLogger(ZookeeperConf.class);
		
		@Bean
		public CuratorFramework getCuratorFramework(){
			
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			CuratorFramework client = CuratorFrameworkFactory.newClient(zkUrl, retryPolicy);
			client.start();
			
			/*
			client.getCuratorListenable().addListener(new CuratorListener() {
				
				@Override
				public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
					CuratorEventType type = event.getType();
					if(type == CuratorEventType.WATCHED){
						WatchedEvent we = event.getWatchedEvent();
						EventType et = we.getType();
						logger.info(et + ":" + we.getPath());
						
						client.checkExists().watched().forPath("/head");
					}
				}
			});
			*/
			
			
			//领导选取
			LeaderSelectorListenerAdapter listenerAdapter = new LeaderSelectorListenerAdapter() {
				
				@Override
				public void takeLeadership(CuratorFramework client) throws Exception {
					// TODO Auto-generated method stub
					
				}
			};
			LeaderSelector selector = new LeaderSelector(client, "/schedule", listenerAdapter);
			selector.autoRequeue();
			selector.start();
			
			
			/*
			try {
				
				//测试。。。。。
				//注册服务
				registerService(client);
				
				//查询服务
				ServiceInstance<Map> service = fnidService(client, "book");
				System.out.println(new ObjectMapper().writeValueAsString(service));
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			
			
			
			return client;
		}

		protected void registerService(CuratorFramework client) throws Exception{
			
			//构造一个服务描述
			ServiceInstanceBuilder<Map> service = ServiceInstance.builder();
			
			service.address(zkServiceAddress);
			service.port(zkServicePort);
			service.name("book");
			
			Map config = new HashMap<>();
			config.put("url", "/api/v3/book");
			service.payload(config);
			
			ServiceInstance<Map> instance = service.build();
			
			ServiceDiscovery<Map> serviceDiscovery = ServiceDiscoveryBuilder.builder(Map.class)
					.client(client).serializer(new JsonInstanceSerializer<>(Map.class))
					.basePath("/service").build();
			
			//服务注册
			serviceDiscovery.registerService(instance);
			serviceDiscovery.start();
			
		}
		
		protected ServiceInstance<Map> fnidService(CuratorFramework client,String serviceName) throws Exception{
			
			ServiceDiscovery<Map> serviceDiscovery = ServiceDiscoveryBuilder.builder(Map.class)
					.client(client).serializer(new JsonInstanceSerializer<>(Map.class))
					.basePath("/service").build();
			
			serviceDiscovery.start();
			
			Collection<ServiceInstance<Map>> serviceList = serviceDiscovery.queryForInstances(serviceName);
			if(serviceList.size() == 0){
				return null;
			}
			ServiceInstance<Map> service = new ArrayList<ServiceInstance<Map>>(serviceList).get(0);
			
			logger.info("Address",service.getAddress());
			logger.info("Payload",service.getPayload());
			
			return service;
		}
		
		
		
		
		
		
}
