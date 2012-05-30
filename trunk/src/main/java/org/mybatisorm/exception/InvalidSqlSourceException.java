package org.mybatisorm.exception;

public class InvalidSqlSourceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidSqlSourceException(String message) {
		super(message);
	}

	public InvalidSqlSourceException(Throwable cause) {
		super(cause);
	}
}
