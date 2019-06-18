package ink.ykb;

import org.beetl.sql.core.SQLManager;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.GenFilter;
import org.beetl.sql.ext.gen.MapperCodeGen;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SimpleApplication.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GenPojoCodeTest {

	@Autowired
	private SQLManager sqlMnager;
	
	/**
	 * 生成指定的代码
	 * @throws Exception
	 * @author: yang.kb@topcheer.com
	 * @date: 2017年12月21日 下午5:10:45
	 */
//	@Test
	public void genPojoCode2() throws Exception {
		GenConfig config = new GenConfig();
		String entityPkg = "ink.ykb.entity";
		String daoPkg = "ink.ykb.dao";
	   	MapperCodeGen mapper = new MapperCodeGen(daoPkg);
	   	config.codeGens.add(mapper);
		
	   	String table = "user_test";
	   	
	   	sqlMnager.genPojoCode(table, entityPkg, config);
	   	sqlMnager.genSQLFile(table);
	}
	
	/**
	 * 初始化生成所有实体类和dao类
	 * 注意：只用于初始化，开发阶段请勿使用
	 * @throws Exception 
	 */
//	@Test
	public void genPojoCode() throws Exception {
		GenConfig config = new GenConfig();
		MyGenFilter filter = new MyGenFilter();
		String entityPkg = "ink.ykb.entity";
		String daoPkg = "ink.ykb.dao";
	   	MapperCodeGen mapper = new MapperCodeGen(daoPkg);
	   	config.codeGens.add(mapper);
		
	   	sqlMnager.genALL(entityPkg, config, filter);
	}
	
	
	
	private class MyGenFilter implements GenFilter{

		@Override
		public boolean accept(String tableName) {
			return true;
		}
		
	}
}
