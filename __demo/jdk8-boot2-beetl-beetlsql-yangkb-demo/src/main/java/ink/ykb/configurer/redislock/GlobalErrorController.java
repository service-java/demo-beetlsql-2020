package ink.ykb.configurer.redislock;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局处理异常
 */
@Slf4j
//@ControllerAdvice
public class GlobalErrorController {

//    @Autowired
//    private MessageUtil messageUtil;
//    @Autowired
//    private Constant constant;
//    
//	@ExceptionHandler(value = {BindException.class,MethodArgumentNotValidException.class})
//	@ResponseBody
//    public  ResponseMsg<String> bindExceptionHandler(Exception e) {
//		
//		String code = null;
//		String field = null;
//		if(e instanceof BindException){
//			BindException e1 =  (BindException)e;
//	    	code = e1.getBindingResult().getFieldError().getDefaultMessage();
//	    	field = e1.getBindingResult().getFieldError().getField();
//		}else if(e instanceof MethodArgumentNotValidException){
//			MethodArgumentNotValidException e1 = (MethodArgumentNotValidException)e;
//			field = e1.getBindingResult().getFieldError().getField();
//	    	code = e1.getBindingResult().getFieldError().getDefaultMessage();
//		}
//		
//		log.error("参数校验失败：{}" ,field);
//		if(StringUtils.isBlank(code)){
//    		code = ResponseMsg.CODE_FAIL;
//    	}
//    	String msg = messageUtil.getMsg(code);
//    	return new ResponseMsg<>(code,msg);
//    	
//    }
//
//	@ExceptionHandler(ServiceException.class)
//    public @ResponseBody ResponseMsg<Object> serviceException(ServiceException e) {
//        log.error("异常信息： \n编号：{}\n消息：{}\n数据：{}\n位置：{}",
//        		e.getCode(),
//        		e.getMessage(),
//        		e.getData()!= null?constant.formatJson(e.getData()):"",
//        		(e.getStackTrace()!=null && e.getStackTrace().length > 0)?e.getStackTrace()[0].toString():e.getStackTrace());
//        
//        return new ResponseMsg<>(e.getCode(),e.getMessage(),e.getData());
//    }
//
//	@ExceptionHandler(Exception.class)
//    public @ResponseBody ResponseMsg<String> exception(Exception e) {
//        log.error("未知错误",e);
//        return new ResponseMsg<>(CodeConstant.MMA_999,messageUtil.getMsg(CodeConstant.MMA_999),e.getMessage());
//    }
}
