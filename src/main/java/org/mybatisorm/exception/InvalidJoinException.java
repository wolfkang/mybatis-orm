package org.mybatisorm.exception;

public class InvalidJoinException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidJoinException(String message) {
		super(message);
	}

	public InvalidJoinException(Throwable cause) {
		super(cause);
	}
}
