package ink.ykb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ink.ykb.configurer.zookeeper.ClusterLock;
import ink.ykb.dao.UserDao;
import ink.ykb.entity.User;
import ink.ykb.service.UserService;
import ink.ykb.util.PlatformException;
import ink.ykb.util.PlatformRuntimeException;

@Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class, RuntimeException.class})
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private SQLManager sqlManager;
	@Autowired
	private UserDao userDao;
	
	String lockPath = "/lock/order";
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);  
	@Autowired
	private CuratorFramework zkClient;
	
	
	@Cacheable(cacheNames = "user",key = "#id")
	@Override
	public User getUser(Integer id) {
		User user = sqlManager.single(User.class,id);
		return user;
	}

	@Override
	public List<User> getUserList(String keyword) {
		Query<User> query = sqlManager.query(User.class);
		List<User> list = query.andLike("name", "%"+keyword+"%").select();
		return list;
	}

	@Override
	public void addUser(User user){
		
		if(user == null){throw new PlatformRuntimeException("用户不能为空");}
		if(user.getName() == null){throw new PlatformRuntimeException("姓名不能为空");}
		user.setCreateTime(new Date());
		user.setDepartmentId(1);
		userDao.insert(user);
	}
	@CachePut(cacheNames = "user",key = "#user.id")
	@Override
	public User updateUser(User user) {
		if(user == null){throw new PlatformRuntimeException("用户不能为空");}
		if(user.getName() == null){throw new PlatformRuntimeException("姓名不能为空");}
		if(user.getId() == null){throw new PlatformRuntimeException("ID不能为空");}
		
		User dbUser = userDao.single(user.getId());
		if(dbUser == null){
			throw new PlatformRuntimeException("查询不到该用户");
		}
		dbUser.setName(user.getName());
		
		userDao.updateById(dbUser);
		return dbUser;
	}

	@CacheEvict(cacheNames = "user",key = "#id")
	@Override
	public void deleteUser(Integer id){
		if(id == null){throw new PlatformRuntimeException("ID不能为空");}
		
		User dbUser = userDao.single(id);
		if(dbUser == null){
			throw new PlatformRuntimeException("查询不到该用户");
		}
		userDao.deleteById(id);
	}
	
	public void autoTask(Integer id){
		if(id == null){throw new PlatformRuntimeException("ID不能为空");}
		
		User dbUser = userDao.single(id);
		if(dbUser == null){
			throw new PlatformRuntimeException("查询不到该用户");
		}
		userDao.deleteById(id);
	}

	@Override
	public void makeOrderType(String type) {
		String path = lockPath+"/"+type;
		logger.info("try do job for "+type);
		
		try {
			InterProcessMutex lock = new InterProcessMutex(zkClient, path);
			if(lock.acquire(10, TimeUnit.HOURS)){
				try {
					Thread.sleep(5000);//耗时操作
					
					logger.info("do job "+type +" done");
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					lock.release();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@ClusterLock(path="/lock/order",key="type")
	@Override
	public void makeOrderType2(String type) throws Exception{
		//即使进入了锁，也要判断业务是否被处理，如果处理了，就立即返回
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}//耗时操作
				
				int num = (int)(Math.random()*5);
				if(num == 1){
					throw new PlatformRuntimeException("假装抛PlatformRuntimeException异常");
				}else if(num == 2){
					throw new RuntimeException("假装抛RuntimeException异常");
				}else if(num == 3){
					throw new Exception("假装抛Exception异常");
				}else if(num == 4){
					throw new PlatformException("假装抛PlatformException异常");
				}
	}

	
}
