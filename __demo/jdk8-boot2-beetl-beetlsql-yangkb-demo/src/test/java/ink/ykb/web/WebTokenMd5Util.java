package ink.ykb.web;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* MD5 摘要算法工具类
*
*/
public class WebTokenMd5Util {

	
	 public static String getMd5(String s) {
		 String result = null;
		    MessageDigest md5 = null;
		    try{
		        md5 = MessageDigest.getInstance("MD5");
		        md5.update((s).getBytes("UTF-8"));
		    }catch (NoSuchAlgorithmException error){
		        error.printStackTrace();
		    }catch (UnsupportedEncodingException e){
		        e.printStackTrace();
		    }
		    byte b[] = md5.digest();
		    int i;
		    StringBuffer buf = new StringBuffer("");

		    for(int offset=0; offset<b.length; offset++){
		        i = b[offset];
		        if(i<0){
		            i+=256;
		        }
		        if(i<16){
		            buf.append("0");
		        }
		        buf.append(Integer.toHexString(i));
		    }

		    result = buf.toString();
		    return result;
	 }
}
