package ink.ykb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ink.ykb.es.entity.Book;
import ink.ykb.es.repository.BookRepository;
import ink.ykb.util.CommonResult;
import io.swagger.annotations.Api;

@Api(value="搜索")
@Controller
@RequestMapping("/es")
public class EsController {
	
	@Autowired
	private BookRepository bookRepository;
	
	@GetMapping("/book/{id}")
	@ResponseBody
	public CommonResult book(@PathVariable String id){
			Optional<Book> opt = bookRepository.findById(id);
			Book book = opt.get();
			return CommonResult.resultSuccess(book, 1);
	}
	@GetMapping("/book/search/{key}")
	@ResponseBody
	public CommonResult search(@PathVariable String key){
			 List<Book> list = bookRepository.getByMessage(key);
			return CommonResult.resultSuccess(list, list.size());
	}
	@GetMapping("/book/search/{key}/{page}")
	@ResponseBody
	public CommonResult search(@PathVariable int page,@PathVariable String key){
			int size = 5;
			PageRequest pageRequest = PageRequest.of(page, size);
			//翻页
			Page<Book> pages = bookRepository.getByMessage(key, pageRequest);
			return CommonResult.resultSuccess(pages, pages.getSize());
	}
	
	
}
