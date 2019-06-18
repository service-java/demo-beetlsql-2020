package ink.ykb;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ink.ykb.es.entity.Book;
import ink.ykb.util.CommonResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SimpleApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HttpTest {

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * elasticsearch
	 * @author: yang.kb@topcheer.com
	 * @throws IOException 
	 * @date: 2018年1月12日 下午3:07:29
	 */
	@Test
	public void test_02() throws IOException{
		Map<String, Object> params = new HashMap<>();
		params.put("id", 1);
		
		String result = restTemplate.getForObject("http://127.0.0.1:9200/product/book/{id}", String.class,params);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		JsonParser parser = factory.createParser(result);
		
		TreeNode root = mapper.readTree(parser);
		TreeNode sourceNode = root.get("_source");
		
		Book book = mapper.convertValue(sourceNode, Book.class);
		System.out.println(book.getMessage());
		
	}
	
	
	@Test
	public void test_01() throws JsonProcessingException{
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);//表单提交
		
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
	    params.add("id", 1);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(params,headers);
		
		CommonResult commonResult = restTemplate.postForObject("http://127.0.0.1:9090/user/info", httpEntity, CommonResult.class);
		System.out.println(new ObjectMapper().writeValueAsString(commonResult));
		
	}
	@Test
	public void test_00() throws JsonProcessingException{
		
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
		headers.setContentType(MediaType.APPLICATION_JSON);//json data提交
		
		Map<String, Object> params = new HashMap<>();
		params.put("id", 1);
		HttpEntity<String> httpEntity = new HttpEntity<String>(new ObjectMapper().writeValueAsString(params),headers);
		
		CommonResult commonResult = restTemplate.postForObject("http://127.0.0.1:9090/user/info", httpEntity, CommonResult.class);
		System.out.println(new ObjectMapper().writeValueAsString(commonResult));
		
	}
	

	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String result = Request.Get("https://www.baidu.com").execute().returnContent().asString(Charset.forName("utf-8"));
		System.out.println(result);
		
	}
	
}
