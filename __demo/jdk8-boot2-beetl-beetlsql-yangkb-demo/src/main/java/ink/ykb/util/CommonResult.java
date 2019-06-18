package ink.ykb.util;

/**
* ajax数据返回对象
*/
public class CommonResult {

	public enum ResultStatusType{
		/** 服务器返回状态码：200 正常，用于提交成功 */
		SUCCESS(200,"操作成功"),
		/**  服务器返回状态码：503 警告，用于数据校验失败  */
		WARN(503,"操作失败"),
		/**  服务器返回状态码：500 异常，用于捕获异常  */
		ERROR(500,"操作异常"),
		/**  服务器返回状态码：1000 重定向  */
		REDIRECT(1000,"请重新登录");
		
		public Integer  status;
		public String  msg;
		
		private ResultStatusType(Integer status,String msg) {
			this.status = status;
			this.msg = msg;
		}
		
		public ResultStatusType getResultStatusType(Integer status) {
			if(status.equals(SUCCESS.status)) {
				return SUCCESS;
			}
			if(status.equals(WARN.status)) {
				return WARN;
			}
			if(status.equals(ERROR.status)) {
				return ERROR;
			}
			if(status.equals(REDIRECT.status)) {
				return REDIRECT;
			}
			throw new PlatformRuntimeException("未找到匹配的消息状态码");
		}
	}
	
    private Integer code;
    private String message;
    private Object data;
    private Integer total;

    public static CommonResult build(Integer code, String message, Object Data,Integer total) {
        return new CommonResult(code, message, Data,total);
    }
    public static CommonResult build(Integer code, String message, Object Data) {
    	return new CommonResult(code, message, Data);
    }

    public static CommonResult resultSuccess(Object data,Integer total) {
    	CommonResult result = createCommonResult(null, ResultStatusType.SUCCESS);
    	result.data = data;
    	result.total=total;
    	return result;
    }

    public static CommonResult resultSuccess() {
    	CommonResult result = createCommonResult(null, ResultStatusType.SUCCESS);
    	return result;
    }
    public static CommonResult resultSuccess(String message) {
    	CommonResult result = createCommonResult(null, ResultStatusType.SUCCESS);
    	result.message = message;
    	return result;
    }
    
    public static CommonResult resultFail() {
    	CommonResult result = createCommonResult(null, ResultStatusType.WARN);
    	return result;
    }
    
    public static CommonResult resultFail(String message) {
    	CommonResult result = createCommonResult(null, ResultStatusType.WARN);
    	result.message = message;
    	return result;
    }
    public static CommonResult resultFail(Exception e) {
    	CommonResult result = createCommonResult(e, ResultStatusType.WARN);
    	return result;
    }

    public static CommonResult resultError() {
    	CommonResult result = createCommonResult(null, ResultStatusType.ERROR);
    	return result;
    }
    
    public static CommonResult resultError(String message) {
    	CommonResult result = createCommonResult(null, ResultStatusType.ERROR);
    	result.message = message;
    	return result;
    }
    
    public static CommonResult resultError(Exception e) {
    	CommonResult result = createCommonResult(e, ResultStatusType.ERROR);
    	return result;
    }
    
    public CommonResult() {
    		
    }

    public static CommonResult build(Integer code, String message,Integer total) {
        return new CommonResult(code, message, null,total);
    }

    public CommonResult(Integer code, String message, Object data) {
    	this.code = code;
    	this.message = message;
    	this.data = data;
    }
    public CommonResult(Integer code, String message, Object data,Integer total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total=total;
    }



    public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

    private static CommonResult createCommonResult(Throwable e,ResultStatusType statusType) {
    	CommonResult result = new CommonResult();
    	result.code = statusType.status;
    	result.message = statusType.msg;
    	if(e != null) {
    		if(e instanceof PlatformRuntimeException){
        		PlatformRuntimeException exception = (PlatformRuntimeException)e;
        		if(exception.getCode() != null ) {
        			result.code = exception.getCode() ;
        		}
        		if(exception.getData() != null ) {
        			result.data = exception.getData() ;
        		}
        		
        		result.message = e.getMessage();
        	}
    	}
    	
    	return result;
    	
    }
    
    
}
