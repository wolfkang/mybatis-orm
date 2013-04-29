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
package org.mybatisorm.annotation;

public enum NamingRule {
	NONE(0x00),
	UPPERCASE(0x01),
	LOWERCASE(0x02),
	UNDERSCORE(0x04),
	UPPERCASE_UNDERSCORE(0x01 | 0x04),
	LOWERCASE_UNDERSCORE(0x02 | 0x04);
	
	private int value;
	
	NamingRule(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean has(NamingRule naming) {
		return (this.value & naming.value) != NamingRule.NONE.value;
	}
	
	public boolean equals(NamingRule naming) {
		return value == naming.value;
	}
}
