package ink.ykb.configurer.redislock;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseMsg<T> implements Serializable {
		@JsonIgnore
		public static final String CODE_SUCCESS = "000000";
		@JsonIgnore
		public static final String CODE_FAIL = "000001";
		@JsonIgnore
		public static final String CODE_UNKNOWN_ERROR = "999999";
		private String code = "000000";
		private Long timestamp = Long.valueOf(System.currentTimeMillis());
		@JsonInclude(Include.NON_NULL)
		private String msg;
		@JsonInclude(Include.NON_NULL)
		private T data;

		@JsonIgnore
		public boolean isOk() {
			return "000000".equals(this.code);
		}

		public ResponseMsg(String code, String message, T data) {
			this.code = code;
			this.msg = message;
			this.data = data;
		}

		public ResponseMsg(String code, String message) {
			this.code = code;
			this.msg = message;
		}

		public ResponseMsg(T data) {
			this.data = data;
		}

		public ResponseMsg(String message, T data) {
			this.msg = message;
			this.data = data;
		}

		public ResponseMsg() {
		}

		public String getCode() {
			return this.code;
		}

		public Long getTimestamp() {
			return this.timestamp;
		}

		public String getMsg() {
			return this.msg;
		}

		public T getData() {
			return this.data;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setTimestamp(Long timestamp) {
			this.timestamp = timestamp;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public void setData(T data) {
			this.data = data;
		}

		public boolean equals(Object o) {
			if (o == this) {
				return true;
			} else if (!(o instanceof ResponseMsg)) {
				return false;
			} else {
				ResponseMsg other = (ResponseMsg) o;
				if (!other.canEqual(this)) {
					return false;
				} else {
					label59 : {
						String this$code = this.getCode();
						String other$code = other.getCode();
						if (this$code == null) {
							if (other$code == null) {
								break label59;
							}
						} else if (this$code.equals(other$code)) {
							break label59;
						}

						return false;
					}

					Long this$timestamp = this.getTimestamp();
					Long other$timestamp = other.getTimestamp();
					if (this$timestamp == null) {
						if (other$timestamp != null) {
							return false;
						}
					} else if (!this$timestamp.equals(other$timestamp)) {
						return false;
					}

					String this$msg = this.getMsg();
					String other$msg = other.getMsg();
					if (this$msg == null) {
						if (other$msg != null) {
							return false;
						}
					} else if (!this$msg.equals(other$msg)) {
						return false;
					}

					Object this$data = this.getData();
					Object other$data = other.getData();
					if (this$data == null) {
						if (other$data != null) {
							return false;
						}
					} else if (!this$data.equals(other$data)) {
						return false;
					}

					return true;
				}
			}
		}

		protected boolean canEqual(Object other) {
			return other instanceof ResponseMsg;
		}

		public int hashCode() {
			boolean PRIME = true;
			byte result = 1;
			String $code = this.getCode();
			int result1 = result * 59 + ($code == null ? 43 : $code.hashCode());
			Long $timestamp = this.getTimestamp();
			result1 = result1 * 59 + ($timestamp == null ? 43 : $timestamp.hashCode());
			String $msg = this.getMsg();
			result1 = result1 * 59 + ($msg == null ? 43 : $msg.hashCode());
			Object $data = this.getData();
			result1 = result1 * 59 + ($data == null ? 43 : $data.hashCode());
			return result1;
		}

		public String toString() {
			return "ResponseMsg(code=" + this.getCode() + ", timestamp=" + this.getTimestamp() + ", msg=" + this.getMsg()
					+ ", data=" + this.getData() + ")";
		}
}
