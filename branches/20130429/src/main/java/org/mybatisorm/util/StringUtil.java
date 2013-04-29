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
package org.mybatisorm.util;

import java.util.Collection;
import java.util.Iterator;

public class StringUtil {
	
	private static final char[] CHARACTERS_26_DIGIT =  {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
		'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	public static <T>String join(final Collection<T> objs, final String delimiter) {
		if (objs == null || objs.isEmpty())
			return "";
		Iterator<T> iter = objs.iterator();
		StringBuilder buffer = new StringBuilder(iter.next().toString());
		while (iter.hasNext())
			buffer.append(delimiter).append(iter.next().toString());
		return buffer.toString();
	}

	public static <T>String join(final Iterable<T> objs, final String format, final String delimiter) {
		if (objs == null)
			return "";
		Iterator<T> iter = objs.iterator();
		if (!iter.hasNext())
			return "";
		
		StringBuilder buffer = new StringBuilder(String.format(format, iter.next().toString()));
		while (iter.hasNext())
			buffer.append(delimiter).append(String.format(format, iter.next().toString()));
		return buffer.toString();
	}
	
	/**
	 * 주어진 integer값을 26진수 문자열로 변환합니다.
	 * @param i max 916,132,831 (9억)
	 * @return 26진수 문자열
	 */
	public static String convertTo26Digit(int i, int figure) {
		return convertTo26Digit((long)i,figure);
	}
	
	
	/**
	 * 주어진 integer값을 26진수 문자열로 변환합니다.
	 * @param i max 916,132,831 (9억)
	 * @return 26진수 문자열
	 */
	public static String convertTo26Digit(int i) {
		return convertTo26Digit((long)i,0);
	}
	
	/**
	 * 주어진 long 값을 26진수 문자열로 변환합니다.
	 * 
	 * @param l 값
	 * @return 26진수 문자열
	 */
	public static String convertTo26Digit(long l) {
		return convertTo26Digit(l,0);
	}
	
	/**
	 * 주어진 long 값을 26진수 문자열로 변환합니다. 자리수 남는 부분은 "0"으로 채웁니다.
	 * 
	 * @param l 값
	 * @param figure 자리수. figure 값이 0 이면 "0"으로 채우지 않습니다. 
	 * @return 26진수 문자열
	 */
	public static String convertTo26Digit(long l, int figure) {
		StringBuilder result = new StringBuilder();
		do {
			result.insert(0, CHARACTERS_26_DIGIT[(int)(l % 26)]);
			l = l / 26;
		} while (l > 0);
		return (figure > 0) ? lpad(result.toString(), figure, "0") : result.toString();
	}

	public static String lpad(String s, int len, String padc) {
		int slen = 0;
		if (s != null) {
			slen = s.length();
			if (slen >= len) {
				return s;
			}
		}
		StringBuilder sb = new StringBuilder(s);
		for (int i = slen; i < len; i++) {
			sb.insert(0, padc);
		}
		return sb.toString();
	}
}