/*
 *    Copyright 2012 The MyBatisORM Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatisorm.exception;

public class InvalidParameterObjectException extends MyBatisOrmException {

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
