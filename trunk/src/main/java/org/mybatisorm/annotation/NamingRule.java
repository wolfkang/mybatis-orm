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
