package it.gecko.text;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class StringUtils
{
	public static String replace(String source, String replaced, String replace)
	{
		StringTokenizer st = new StringTokenizer(source, replaced);
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens())
		{
			sb.append(st.nextToken()).append(replace);
		}
		return trimEnd(sb.toString());
	}

	public static String trimStart(String str)
	{
		str = str.trim();
		int idx = str.lastIndexOf(' ') + 1;
		if ((str != null) && !str.equalsIgnoreCase(""))
		{
			str = str.substring(idx, str.length());
		}
		return str;
	}

	public static String trimEnd(String str)
	{
		str = str.trim();
		int idx = str.lastIndexOf(' ');
		if ((str != null) && !str.equalsIgnoreCase(""))
		{
			str = str.substring(0, idx);
		}
		return str;
	}

	public static String throwableToString(Throwable t)
	{
		String msg = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		t.printStackTrace(new PrintStream(baos));
		msg = baos.toString();
		return msg;
	}
}
