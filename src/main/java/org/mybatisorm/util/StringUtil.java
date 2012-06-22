package org.mybatisorm.util;

import java.util.Collection;
import java.util.Iterator;

public class StringUtil {
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
}
