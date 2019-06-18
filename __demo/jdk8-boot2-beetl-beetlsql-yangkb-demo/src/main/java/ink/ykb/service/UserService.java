package ink.ykb.service;

import java.util.List;

import ink.ykb.entity.User;

public interface UserService{

	User getUser(Integer id);
	
	List<User> getUserList(String keyword);
	
	void addUser(User user);
	
	User updateUser(User user);
	
	void deleteUser(Integer id);
	
	void makeOrderType(String type);
	
	void makeOrderType2(String type) throws Exception;
	
}
