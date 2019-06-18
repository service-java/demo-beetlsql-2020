package ink.ykb.configurer.customcachemanager;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;

import ink.ykb.configurer.customcachemanager.CacheConfig.TwoLevelCacheManager;

public class RedisAndLocalCache implements Cache {

	private ConcurrentHashMap<Object, Object> local = new ConcurrentHashMap<>();
	
	private TwoLevelCacheManager cacheManager;
	
	private RedisCache redisCache;
	
	public RedisAndLocalCache(TwoLevelCacheManager twoLevelCacheManager, RedisCache redisCache) {
		super();
		this.cacheManager = twoLevelCacheManager;
		this.redisCache = redisCache;
	}
	

	@Override
	public String getName() {
		return redisCache.getName();
	}

	@Override
	public Object getNativeCache() {
		return redisCache.getNativeCache();
	}

	@Override
	public ValueWrapper get(Object key) {
		//一级缓存
		ValueWrapper wrapper = (ValueWrapper)local.get(key);
		if(wrapper != null){
			return wrapper;
		}else{
			//二级缓存
			wrapper = redisCache.get(key);
			if(wrapper != null){
				local.put(key, wrapper);
			}
			return wrapper;
		}
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		ValueWrapper valueWrapper = this.get(key);
		if(valueWrapper == null){
			return (T)null;
		}
		
		return (T)valueWrapper.get();
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		ValueWrapper valueWrapper = this.get(key);
		if(valueWrapper != null){
			return (T)valueWrapper.get();
		}
		
		try {
			T value = valueLoader.call();
			put(key, value);
			return value;
		} catch (Exception e) {
			throw new ValueRetrievalException(key, valueLoader, e);
		}
		
	}

	@Override
	public void put(Object key, Object value) {
		redisCache.put(key, value);
		//通知其他节点缓存更新
		clearOtherJVM();
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		redisCache.put(key, value);
		
		//通知其他节点缓存更新
		clearOtherJVM();
		
		return redisCache.get(key);
	}

	@Override
	public void evict(Object key) {
		redisCache.evict(key);
		//通知其他节点缓存更新
		clearOtherJVM();

	}

	@Override
	public void clear() {
		this.redisCache.clear();
		this.local.clear();
	}

	//提供给CacheManage清空一级缓存
	public void clearLocal() {
		this.local.clear();
		
	}

	protected void clearOtherJVM() {
		cacheManager.publishMeaasge(redisCache.getName());
	}

}
