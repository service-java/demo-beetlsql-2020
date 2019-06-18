package ink.ykb.configurer.zookeeper.servicediscovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import ink.ykb.util.PropertiesUtil;

public class CuratorUtils {

	public static final String applicationProperties = "/application.properties";
	
	public static CuratorFramework getCuratorClient(){
        String address = PropertiesUtil.getProps("zk.url", applicationProperties);
        System.out.println("create curator client:"+address);
        return CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(1000, 3));
    }
}
