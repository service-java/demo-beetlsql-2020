package ink.ykb.web;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;



public class Test {

	
    public static void main(String[] args) {
    	
    	System.out.println(WebTokenMd5Util.getMd5("123测试").equals("efa89292f135b914db20de5f8b077ea7"));
    	
        String context = "i1obp+V0Q61SJD5FRxkhJInU09dHxZNT10GQD3HlGBwWEnM2WYfGkWtOyPAXAFLrozEAX6ZFd22E7tTOIKoKaA==";
        String key =  WebTokenAESUtils.decrypt(context);
        JSONObject json = JSONObject.parseObject(key);
		System.out.println(WebTokenAESUtils.IV.equals(json.getString("s")));
		
		long currentTime = new Date().getTime();
		System.out.println( json.getLong("c") > new Date( currentTime - (30 * 60 * 1000)).getTime() && json.getLong("c")  < new Date(currentTime+ (30 * 60 * 1000)).getTime());
    }

}
