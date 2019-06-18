package ink.ykb.util;


/**
 * 自定义加解密异常
 * 注意：此异常为RuntimeException
 */
public class PlatformEncryptRuntimeException extends PlatformRuntimeException{

	private static final long serialVersionUID = 1L;
	protected Throwable cause = null;
	
	public PlatformEncryptRuntimeException() {
	}
	
	public PlatformEncryptRuntimeException(String msg){
		super(msg);
	}
	
	public PlatformEncryptRuntimeException( String message, Throwable cause )
	   {
	      super( message );
	      this.cause = cause;
	   }
	
	 public PlatformEncryptRuntimeException( Throwable cause )
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
