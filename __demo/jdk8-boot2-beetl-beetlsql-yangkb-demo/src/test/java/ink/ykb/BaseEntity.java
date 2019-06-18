package ink.ykb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import ink.ykb.TestPOJO.FilterView;

@JsonIgnoreProperties(value = {"autoMark"})
public class BaseEntity {
	
	@JsonView(FilterView.OutputD.class)  
    private String autoMark;

	public String getAutoMark() {
		return autoMark;
	}

	public void setAutoMark(String autoMark) {
		this.autoMark = autoMark;
	}

	
    
	
}
