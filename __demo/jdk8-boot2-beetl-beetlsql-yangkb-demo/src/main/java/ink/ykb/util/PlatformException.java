package ink.ykb.util;


/**
 * 自定义异常<br>
 * 注意：此异常为check Exception类型
 */
public class PlatformException extends Exception{

	private static final long serialVersionUID = 1L;
	protected Throwable cause = null;
	
	public PlatformException() {
	}
	
	public PlatformException(String msg){
		super(msg);
	}
	
	public PlatformException( String message, Throwable cause )
	   {
	      super( message );
	      this.cause = cause;
	   }
	
	 public PlatformException( Throwable cause )
	   {
	      super( cause.getMessage() );
	      this.cause = cause;
	   }
	 
	 @Override
	public Throwable getCause()
	   {
	      return this.cause;
	   }

}
