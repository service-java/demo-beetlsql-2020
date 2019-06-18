package ink.ykb.configurer.redislock;

import lombok.Data;

/**
 * 业务异常
 */
@Data
public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String code;

    private Object data;

    public ServiceException(String code,String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String code,String message,Object data){
        super(message);
        this.code = code;
        this.data = data;
    }
}
