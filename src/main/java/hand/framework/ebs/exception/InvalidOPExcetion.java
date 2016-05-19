package hand.framework.ebs.exception;

public class InvalidOPExcetion extends RuntimeException{

	private String errorMsg;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidOPExcetion(String msg){
		super();
		this.errorMsg = msg;
	}

	@Override
	public String getMessage() {
		return getErrorMsg();
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
