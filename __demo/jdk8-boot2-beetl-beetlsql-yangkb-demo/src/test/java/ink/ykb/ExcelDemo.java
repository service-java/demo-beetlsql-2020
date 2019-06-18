package ink.ykb;

import java.math.BigDecimal;

import io.github.biezhi.excel.plus.annotation.ExcelField;
import lombok.Data;

@Data
public class ExcelDemo {
				

	@ExcelField(order = 0, columnName = "序列" )
	private String seq;
	
	@ExcelField(order = 1, columnName = "*姓名" )
	private String name;
	
	@ExcelField(order = 2, columnName = "*手机号码" )
	private String hp;
	
	@ExcelField(order = 3, columnName = "*身份证号" )
	private String cardId;
	
	@ExcelField(order = 4, columnName = "*福利" )
	private BigDecimal amount;
	
}
