package ink.ykb;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import ink.ykb.util.Constant;
import io.github.biezhi.excel.plus.ExcelPlus;
import io.github.biezhi.excel.plus.enums.ParseType;
import io.github.biezhi.excel.plus.exception.ExcelException;
import io.github.biezhi.excel.plus.reader.Reader;

public class ExcelTest {

	public static void main(String[] args) throws ExcelException, JsonProcessingException {
		ExcelPlus excelPlus = new ExcelPlus();

		Reader reader = Reader.create()
		                .parseType(ParseType.SAX)
		                .startRowIndex(2)
		                .sheetIndex(0)
		                .excelFile(new File("C:/Users/yangkebiao/Desktop/test.xlsx"));

		List<ExcelDemo> list = excelPlus.read(ExcelDemo.class, reader).asList();
		
		System.out.println(Constant.OBJECT_MAPPER.writeValueAsString(list));
		
	}
	
}
