package org.mybatisorm.exception;

public class InvalidParameterObjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1725205274084467593L;

	public InvalidParameterObjectException(String message) {
		super(message);
	}

	public InvalidParameterObjectException(Throwable cause) {
		super(cause);
	}
}
