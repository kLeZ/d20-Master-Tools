package it.gecko.utils;

import java.util.ArrayList;

public class Utils
{
	public static void swap(int op1, int op2)
	{
		int tmp = op2;
		op2 = op1;
		op1 = tmp;
	}

	public static boolean isInteger(String s)
	{
		boolean ret = true;
		try
		{
			Integer.valueOf(s);
		}
		catch (NumberFormatException e)
		{
			ret = false;
		}
		return ret;
	}

	public static ArrayList<String> splitEncolosed(String s, char open_tag, char close_tag)
	{
		ArrayList<String> ret = new ArrayList<String>();
		char[] chars = s.toCharArray();
		String contents = "";
		for (char c : chars)
		{
			if (c == open_tag)
			{
				if (!contents.isEmpty())
				{
					ret.add(contents);
				}
				contents = "";
			}
			else if (c == close_tag)
			{
				ret.add(contents);
				contents = "";
			}
			else
			{
				contents += c;
			}
		}
		if (!contents.isEmpty())
		{
			ret.add(contents);
			contents = "";
		}
		return ret;
	}
}
