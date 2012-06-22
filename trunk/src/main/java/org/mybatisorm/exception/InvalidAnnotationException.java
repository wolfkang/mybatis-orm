package org.mybatisorm.exception;

public class InvalidAnnotationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAnnotationException(String message) {
		super(message);
	}

	public InvalidAnnotationException(Throwable cause) {
		super(cause);
	}
}
