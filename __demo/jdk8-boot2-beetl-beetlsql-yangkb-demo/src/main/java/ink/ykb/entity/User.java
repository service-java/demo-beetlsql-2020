package ink.ykb.entity;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonView;

import ink.ykb.util.filterDto.FilterView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/* 
* 
* gen by beetlsql 2017-12-20
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(value = "User",description = "用户")
public class User implements Serializable {
	
	private static final long serialVersionUID = -8548722610287071619L;
	
	@ApiModelProperty(value="主键")
	Integer id ;
	@ApiModelProperty(value="部门主键")
	Integer departmentId ;
	@ApiModelProperty(value="姓名")
	String name ;
	@ApiModelProperty(value="创建时间")
	Date createTime ;
	
	 @JsonView(FilterView.OutputA.class)  
	@ApiModelProperty(value="密码")
	String password ;
	
	

}
