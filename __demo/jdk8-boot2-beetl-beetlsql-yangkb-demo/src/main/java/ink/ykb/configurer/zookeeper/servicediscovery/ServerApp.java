package ink.ykb.configurer.zookeeper.servicediscovery;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import ink.ykb.util.PropertiesUtil;

/**
 * 模拟服务提供者
 *
 * @author Ricky Fung
 * @create 2016-12-08 19:24
 */
public class ServerApp {

	public static final String BASE_PATH = "services";
    public static final String SERVICE_NAME = "ink.ykb.service.UserService";
	private static String zkServiceAddress = PropertiesUtil.getProps("zk.service.address", CuratorUtils.applicationProperties);
	private static Integer zkServicePort = Integer.valueOf(PropertiesUtil.getProps("zk.service.port", CuratorUtils.applicationProperties));
    
    
    public static void main(String[] args) {

        CuratorFramework client = null;
        ServiceRegistry serviceRegistry = null;
        try{
            client = CuratorUtils.getCuratorClient();
            client.start();

            //服务注册中心
            serviceRegistry = new ServiceRegistry(client, BASE_PATH);
            serviceRegistry.start();

            //注册两个service 实例
            ServiceInstance<ServerPayload> host1 = createServiceInstance("host1", new ServerPayload("HZ", 5));
            ServiceInstance<ServerPayload> host2 = createServiceInstance("host2", new ServerPayload("QD", 3));

            serviceRegistry.registerService(host1);
            serviceRegistry.registerService(host2);

            System.out.println("register service success...");

            TimeUnit.MINUTES.sleep(1);

            //获取服务
            Collection<ServiceInstance<ServerPayload>> list = serviceRegistry.queryForInstances(SERVICE_NAME);
            if(list!=null && list.size()>0){
                System.out.println("service:"+SERVICE_NAME+" provider list:"+ new ObjectMapper().writeValueAsString(list));
            } else {
                System.out.println("service:"+SERVICE_NAME+" provider is empty...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(serviceRegistry!=null){
                try {
                    serviceRegistry.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            client.close();
        }

    }
    
    private static  ServiceInstance<ServerPayload>  createServiceInstance(String id,ServerPayload payload) throws Exception{
    	return ServiceInstance.<ServerPayload>builder()
			        .id(id)
			        .name(SERVICE_NAME)
			        .address(zkServiceAddress)
			        .port(zkServicePort)
			        .payload(payload)
			        .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
			        .build();
    }
    private static  ServiceInstance<ServerPayload>  createServiceInstance(String id,String name,String address,int port,ServerPayload payload) throws Exception{
    	return ServiceInstance.<ServerPayload>builder()
    			.id(id)
    			.name(name)
    			.address(address)
    			.port(port)
    			.payload(payload)
    			.uriSpec(new UriSpec("{scheme}://{address}:{port}"))
    			.build();
    }
}
