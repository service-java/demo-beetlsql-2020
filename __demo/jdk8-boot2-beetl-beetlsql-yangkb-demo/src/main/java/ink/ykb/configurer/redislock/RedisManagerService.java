package ink.ykb.configurer.redislock;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisManagerService {
	
	
	@Autowired(required = false)
	private StringRedisTemplate stringRedisTemplate;
	@Autowired(required = false)
	private RedisTemplate redisTemplate;

	public String getHashTime(String key) {
		return this.stringRedisTemplate.getExpire(key, TimeUnit.SECONDS).toString();
	}

	public Boolean setHashOne(String key, String word, Object value, int time) {
		log.debug("放入redis：{} {} {} {}", new Object[]{key, word, value, Integer.valueOf(time)});
		boolean result = this.stringRedisTemplate.opsForHash().putIfAbsent(key, word, value).booleanValue();
		log.info("放入redis结果：{} {} {}", new Object[]{key, word, Boolean.valueOf(result)});
		return this.stringRedisTemplate.expire(key, (long) time, TimeUnit.SECONDS);
	}

	public Boolean setHashOne(String key, String word, Object value) {
		return this.stringRedisTemplate.opsForHash().putIfAbsent(key, word, value);
	}

	public Boolean justHashOneIfExist(String key, String word) {
		return this.stringRedisTemplate.opsForHash().hasKey(key, word);
	}

	public Long deleteHashOne(String key, String word) {
		return this.stringRedisTemplate.opsForHash().delete(key, new Object[]{word});
	}

	public Boolean setHashMany(String key, Map<String, Object> map, int time) {
		this.stringRedisTemplate.boundHashOps(key).putAll(map);
		return this.stringRedisTemplate.expire(key, (long) time, TimeUnit.SECONDS);
	}

	public Object getHashOne(String key, String word) {
		return this.stringRedisTemplate.boundHashOps(key).get(word);
	}

	public Object getHashMany(String key) {
		return this.stringRedisTemplate.boundHashOps(key).entries();
	}

	public void setStr(String key, String value) {
		this.stringRedisTemplate.opsForValue().set(key, value);
	}

	public void setStr(String key, String value, long expire) {
		this.stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.MILLISECONDS);
	}

	public void setStr(String key, String value, long expire, TimeUnit timeUnit) {
		this.stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
	}

	public String getStr(String key) {
		return (String) this.stringRedisTemplate.boundValueOps(key).get();
	}

	public String listRightPop(String key) {
		String result = (String) this.stringRedisTemplate.opsForList().rightPop(key);
		return result;
	}

	public String listLast(String key) {
		List result = this.stringRedisTemplate.opsForList().range(key, -1L, -1L);
		return result != null && result.size() > 0 ? (String) result.get(0) : null;
	}

	public Long listRightPush(String key, String value) {
		Long result = this.stringRedisTemplate.opsForList().rightPushAll(key, new String[]{value});
		return result;
	}

	public boolean lock(String key, Long expire, TimeUnit timeUnit) {
		try {
			boolean e = this.stringRedisTemplate.boundValueOps(key).setIfAbsent("1").booleanValue();
			if (e) {
				this.stringRedisTemplate.boundValueOps(key).expire(expire.longValue(), timeUnit);
				log.debug("REDIS_LOCK 不存在，设置成功：{},{}", key, this.stringRedisTemplate.boundValueOps(key).get());
				return true;
			}

			Long expireOld = this.stringRedisTemplate.getExpire(key, timeUnit);
			log.debug("REDIS_LOCK 已存在，设置成功：{},{},{}",
					new Object[]{key, this.stringRedisTemplate.boundValueOps(key).get(), expireOld});
			String value = (String) this.stringRedisTemplate.boundValueOps(key).getAndSet("1");
			this.stringRedisTemplate.expire(key, expireOld.longValue(), timeUnit);
			if ("0".equals(value)) {
				this.stringRedisTemplate.expire(key, expire.longValue(), timeUnit);
				log.debug("REDIS_LOCK 设置成功：{},{},{}",
						new Object[]{key, this.stringRedisTemplate.boundValueOps(key).get(), expireOld});
				return true;
			}
		} catch (Exception arg6) {
			log.error("REDIS lock FAIL:{} ", key, arg6);
		}

		return false;
	}

	public boolean unlock(String key) {
		try {
			String e = (String) this.stringRedisTemplate.boundValueOps(key).get();
			log.debug("REDIS LOCK 非空：{}, value:{}", key, e);
			if (!StringUtils.isEmpty(e)) {
				Long expire = this.stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
				log.debug("REDIS LOCK 非空：{}, value:{},超时时间：{}", new Object[]{key, e, expire});
				String value = (String) this.stringRedisTemplate.boundValueOps(key).getAndSet("0");
				if (StringUtils.isEmpty(value)) {
					this.stringRedisTemplate.delete(key);
					log.debug("REDIS LOCK value为空：{}，删除该key", key);
					return false;
				}

				if ("1".equals(value)) {
					if (expire != null && expire.longValue() > 0L) {
						this.stringRedisTemplate.expire(key, expire.longValue() + 1L, TimeUnit.SECONDS);
					}

					log.debug("REDIS LOCK 解锁成功：{}", key);
					return true;
				}
			}

			log.debug("REDIS LOCK 为空：{}", key);
		} catch (Exception arg4) {
			log.error("REDIS unlock failed:{}", key, arg4);
		}

		return false;
	}

	public boolean unlockAndDel(String key) {
		this.stringRedisTemplate.delete(key);
		return true;
	}

	public List<String> listAll(String rediskey) {
		Long size = this.stringRedisTemplate.opsForList().size(rediskey);
		return this.stringRedisTemplate.opsForList().range(rediskey, 0L, size.longValue());
	}

	public void delete(String key) {
		this.stringRedisTemplate.delete(key);
	}

	public Long decr(String key) {
		Long result = Long.valueOf(0L);

		try {
			result = this.redisTemplate.boundValueOps(key).increment(-1L);
		} catch (Exception arg3) {
			log.error("redisTemplate decr error and key = " + key, arg3);
		}

		return result;
	}

	public Long incr(String key) {
		Long result = Long.valueOf(0L);

		try {
			result = this.redisTemplate.boundValueOps(key).increment(1L);
		} catch (Exception arg3) {
			log.error("redisTemplate incr error and key = " + key, arg3);
		}

		return result;
	}

	public Long decrBy(String key, long integer) {
		Long result = Long.valueOf(0L);

		try {
			result = this.redisTemplate.boundValueOps(key).increment(-integer);
		} catch (Exception arg5) {
			log.error("redisTemplate decrBy error and key = " + key, arg5);
		}

		return result;
	}

	public Long incrBy(String key, long integer) {
		Long result = Long.valueOf(0L);

		try {
			result = this.redisTemplate.boundValueOps(key).increment(integer);
		} catch (Exception arg5) {
			log.error("redisTemplate incrBy error and key = " + key, arg5);
		}

		return result;
	}

	public Double decrByFloat(String key, double db) {
		double result = 0.0D;

		try {
			result = this.redisTemplate.boundValueOps(key).increment(-db).doubleValue();
		} catch (Exception arg6) {
			log.error("redisTemplate decrByFloat error and key = " + key, arg6);
		}

		return Double.valueOf(result);
	}

	public Double incrByFloat(String key, double db) {
		double result = 0.0D;

		try {
			result = this.redisTemplate.boundValueOps(key).increment(db).doubleValue();
		} catch (Exception arg6) {
			log.error("redisTemplate incrByFloat error and key = " + key, arg6);
		}

		return Double.valueOf(result);
	}

	public Object get(String key) {
		return this.redisTemplate.boundValueOps(key).get(0L, -1L);
	}
}