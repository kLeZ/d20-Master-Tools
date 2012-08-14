package it.gecko.utils;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

public class UtilsTest extends TestCase
{
	public void testSplitEncolosed()
	{
		char open = '(', close = ')';
		String input1 = "()()()()()()()()";
		String input2 = "(aaa)(bbb)(ccc)(ddd)(eee)(fff)(ggg)(hhh)";
		String input3 = "(aaa)()(ccc)()(eee)()(ggg)()";
		String input4 = "(aa)a(b)b(bc)c(c)c(c)d(d)d(e)e(e)ffgg";
		String input5 = "(a)aaa(b)b(b)b()f()r()c(c)e(e)g";

		String[] output1 =
		{ "", "", "", "", "", "", "", "" };
		String[] output2 =
		{ "aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg", "hhh" };
		String[] output3 =
		{ "aaa", "", "ccc", "", "eee", "", "ggg", "" };
		String[] output4 =
		{ "aa", "a", "b", "b", "bc", "c", "c", "c", "c", "d", "d", "d", "e", "e", "e", "ffgg" };
		String[] output5 =
		{ "a", "aaa", "b", "b", "b", "b", "", "f", "", "r", "", "c", "c", "e", "e", "g" };

		Assert.assertEquals(Arrays.asList(output1), Utils.splitEncolosed(input1, open, close));
		Assert.assertEquals(Arrays.asList(output2), Utils.splitEncolosed(input2, open, close));
		Assert.assertEquals(Arrays.asList(output3), Utils.splitEncolosed(input3, open, close));
		Assert.assertEquals(Arrays.asList(output4), Utils.splitEncolosed(input4, open, close));
		Assert.assertEquals(Arrays.asList(output5), Utils.splitEncolosed(input5, open, close));
	}
}
