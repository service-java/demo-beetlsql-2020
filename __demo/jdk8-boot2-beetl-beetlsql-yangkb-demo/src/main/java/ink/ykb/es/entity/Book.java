package ink.ykb.es.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="product",type="book")
public class Book implements Serializable{

	private static final long serialVersionUID = 4410530331185831047L;
	
	@Id
	String id;
	String name;
	String type;
	Date postDate;
	String message;
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Book(String name, String type, Date postDate, String message) {
		super();
		this.name = name;
		this.type = type;
		this.postDate = postDate;
		this.message = message;
	}
	public Book() {
		super();
	}
	
	
	
	
	
}
