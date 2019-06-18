package ink.ykb.util;


/**
 * 全局自定义异常
 * 注意：此异常为RuntimeException
 */
public class PlatformRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	protected Throwable cause = null;
	
	private Integer code;
	
	private Object data;
	
	public PlatformRuntimeException() {
	}
	
	public PlatformRuntimeException(String msg){
		super(msg);
	}
	
	public PlatformRuntimeException(String msg,Integer code) {
		super(msg);
		this.code = code;
	}
	public PlatformRuntimeException(String msg,Integer code, Object data) {
		super(msg);
		this.code = code;
		this.data = data;
	}
	public PlatformRuntimeException(String msg,Integer code, Throwable cause) {
		super(msg);
		this.code = code;
		this.cause = cause;
	}
	public PlatformRuntimeException(String msg,Integer code, Object data, Throwable cause) {
		super(msg);
		this.code = code;
		this.data = data;
		this.cause = cause;
	}

	public PlatformRuntimeException( String message, Throwable cause )
	   {
	      super( message );
	      this.cause = cause;
	   }
	
	 public PlatformRuntimeException( Throwable cause )
	   {
	      super( cause.getMessage() );
	      this.cause = cause;
	   }
	 
	 @Override
	public Throwable getCause()
	   {
	      return this.cause;
	   }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
