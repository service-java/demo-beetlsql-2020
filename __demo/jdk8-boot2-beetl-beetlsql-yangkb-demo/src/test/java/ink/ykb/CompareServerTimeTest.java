package ink.ykb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ink.ykb.util.PwdUtil;

/**
 * 测试允许发送服务器时间和接收服务器时间相差+-10秒
 * 匹配规则：（密码 + 时间戳） MD5
 */
public class CompareServerTimeTest {
	
	
	public static void main(String[] args) throws Exception {
		boolean compareDateTime = true;
		for (int i = 0; i < 100000; i++) {
			//明文密码
			String signKey = "2dr3434";
			//发送服务器时间戳
			Date now = new Date();
			
			//随机生成几秒，模拟接收服务器当前的时间
			Integer ran = getRandom();
			Date now2 = new Date(now.getTime() + (ran * 1000));
			
			compareDateTime = compareDateTime(signKey, now, now2);
			if(!compareDateTime) {
				break;
			}
		}
		if(!compareDateTime) {
			System.out.println("不匹配！！！！！");
		}
		
		
		
	}
	
	/**
	 * 比较时间
	 * @param signKey
	 * @param date
	 * @param date2
	 * @return
	 * @throws Exception
	 */
	private static boolean compareDateTime(String signKey,Date date,Date date2) throws Exception {
		System.out.println("发送端==================>");
		String passMd5 = getPassMd5(signKey, date);
		
		System.out.println("接收端==================>");
		List<Date> serverConverTime = getServerConverTime(date2);
		for (int j = 0; j < serverConverTime.size(); j++) {
			
			String passMd5_2 = getPassMd5(signKey, serverConverTime.get(j));
			
			if(passMd5_2.equals(passMd5)) {
				System.out.println("第"+(j+1)+"次密码匹配成功");
				return true;
			}else {
				if(j == serverConverTime.size() - 1) {
					System.out.println("密码匹配失败");
				}
			}
		}
		return false;
	}
	
	
	private static String getPassMd5(String signKey,Date date) throws Exception {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//格式化时间
		Date converTime = getConverTime(date);
		//时间签名
		String dateSignkey = dateFormat.format(converTime);
		
		String passMd5 = PwdUtil.md5(signKey + dateSignkey);
		
		System.out.println("signKey："+signKey);
		System.out.println("now："+dateFormat.format(date));
		System.out.println("dateSignkey："+dateSignkey);
		System.out.println("passMd5："+passMd5);
		System.out.println("");
		
		return passMd5;
	}
	
	
	/**
	 * 获取服务器对应的3个时间
	 * @param now
	 * @return
	 * @throws Exception
	 */
	private static List<Date> getServerConverTime(Date date) throws Exception {
		List<Date> list = new ArrayList<>();
		
		list.add(date);
		list.add(new Date(date.getTime() + ((10)*1000)));
		list.add(new Date(date.getTime() - ((10)*1000)));
		
		return list;
	}
	
	/**
	 * 格式化时间
	 * @param now
	 * @return
	 * @throws Exception
	 */
	private static Date getConverTime(Date date) throws Exception {
		Date d = null; 
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		int lastNum = Integer.parseInt(dateFormat.format(date).substring(13));
		
		if(lastNum >= 5) {
			d = new Date(date.getTime() + (  (10 - lastNum) * 1000));
		}else {
			d = new Date(date.getTime() - ( lastNum * 1000));
		}
		
		return dateFormat.parse(dateFormat.format(d));
	}
	
	
	/**
	 *随机数范围： -10 - 10
	 * @return
	 */
	private static Integer getRandom() {
		Random random1 = new Random();
		int nextInt = random1.nextInt(11);
		
		Random random2 = new Random();
		int fh = random2.nextInt(2);
		if(fh == 0) {
			nextInt = Integer.parseInt("-"+nextInt);
		}
		
		return nextInt;
	}


}
