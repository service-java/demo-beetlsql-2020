package ink.ykb;

import com.fasterxml.jackson.annotation.JsonView;


public class TestPOJO extends BaseEntity{
	
    private String a;  
    
    private String b;  
    
    @JsonView(value = {FilterView.OutputC.class,FilterView.OutputD.class})  
    private String c;  
    
    private String d;  
    
    
    @JsonView(FilterView.OutputA.class)  
    private String f;
    @JsonView(FilterView.OutputB.class)  
    private String e;  
    
    public static class FilterView {  
        static class OutputA {}  
        static class OutputB {}  
        static class OutputC {}  
        static class OutputD {}  
    }

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}  
    
    
}

