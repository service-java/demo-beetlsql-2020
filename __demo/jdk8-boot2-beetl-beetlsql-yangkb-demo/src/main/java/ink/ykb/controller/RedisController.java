package ink.ykb.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ink.ykb.entity.User;
import ink.ykb.util.CommonResult;

@Controller
@RequestMapping("/redis")
public class RedisController {
	
	@Autowired
	private RedisTemplate<Object,Object> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	@Qualifier("jsonRedisTemplate")
	private RedisTemplate<Object,Object> jsonRedisTemplate;
	
	
	@GetMapping("/test")
	@ResponseBody
	public CommonResult test(String status ,String value, Integer type){
			Object result = null;
			if(status == null || status.equals("r")){
				if(type == null || type == 0){
					 result = stringRedisTemplate.opsForValue().get("test");
				}else if(type == 1){
					result = stringRedisTemplate.opsForList().range("name", 0,  stringRedisTemplate.opsForList().size("name") -1);
				}else if(type == 2){
					result = stringRedisTemplate.opsForHash().get("cache", "id");
				}else if(type == 3){
					result = stringRedisTemplate.opsForSet().members("age");
				}
			}else if("w".equals(status)){
				if(type == null || type == 0){
					stringRedisTemplate.opsForValue().set("test",value);
				}else if(type == 1){
					stringRedisTemplate.opsForList().rightPush("name", value);
				}else if(type == 2){
					stringRedisTemplate.opsForHash().put("cache", "id", value);
				}else if(type == 3){
					stringRedisTemplate.opsForSet().add("age",value);
				}
			}
			
			return CommonResult.resultSuccess(result, 1);
	}
	@GetMapping("/pub")
	@ResponseBody
	public CommonResult pub(int type,String pub,String message){
			if(type == 0){
				jsonRedisTemplate.convertAndSend(pub, message);
			}else if(type == 1){
				User user = new User();
				user.setCreateTime(new Date());
				user.setDepartmentId(1);
				user.setId(1);
				user.setName(message);
				jsonRedisTemplate.convertAndSend(pub, user);
			}
			return CommonResult.resultSuccess("发送成功", 1);
	}
	@GetMapping("/cache")
	@ResponseBody
	public CommonResult cache(String name){
			Object o = redisTemplate.opsForValue().get(name);
			
//			System.out.println("o:"+o.getClass().getClassLoader().toString());
//			System.out.println("user:"+Thread.currentThread().getContextClassLoader().toString());
			User user = (User)o;
			return CommonResult.resultSuccess(user, 1);
	}
	
}
