package ink.ykb.configurer;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import ink.ykb.entity.User;

public class MyRedisChannelListener implements MessageListener{

	public void onMessage(Message message, byte[] pattern) {
		byte[] channel = message.getChannel();
		byte[] bs = message.getBody();
		String content = null;
		String p = null;
		try {
			content = new String(bs,"UTF-8");
			p = new String(channel,"UTF-8");
			
			if( "{".indexOf(content) >= 0 &&   "}".indexOf(content)  >= 0 && (countStr(content, "{") == countStr(content, "}") )
					){
				User user = new ObjectMapper().readValue(bs, User.class);
				content = new ObjectMapper().writeValueAsString(user);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.out.println("频道："+p+"，消息："+content);
		}
	}
	
	/**
	 * 计算一个字符串中包含另一个字符串的个数  
	 * @param one
	 * @param two
	 * @return
	 * @author: yang.kb@topcheer.com
	 * @date: 2018年1月11日 下午5:14:17
	 */
    private int countStr(String one, String two){  
        int counter=0;  
        if (one.indexOf(two) == -1) {  
            return 0;  
        }  
            while(one.indexOf(two)!=-1){  
                counter++;  
                one=one.substring(one.indexOf(two)+two.length());  
            }  
            return counter;  
    } 

}
