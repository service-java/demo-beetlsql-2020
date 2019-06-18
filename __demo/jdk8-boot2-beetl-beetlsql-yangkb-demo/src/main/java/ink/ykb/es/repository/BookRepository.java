package ink.ykb.es.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import ink.ykb.es.entity.Book;

public interface BookRepository extends CrudRepository<Book, String>{

	List<Book> getByMessage(String key);
	
	Page<Book> getByMessage(String key,Pageable pageable);
	
}
