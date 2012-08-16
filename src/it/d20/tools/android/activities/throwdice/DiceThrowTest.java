/**
 * 
 */
package it.d20.tools.android.activities.throwdice;

import it.gecko.utils.CollectionsAssert;

import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author kLeZ-hAcK
 */
public class DiceThrowTest
{
	/**
	 * Test method for
	 * {@link it.d20.tools.android.activities.throwdice.DiceThrow#parseMany(java.lang.String)}
	 * .
	 */
	@Test
	public void testParseMany()
	{
		String input1 = "(1d6)+(2d6+4)+(10d8+43)-(4d12)+(12d10)+(3d4-2)+(1d10*2)+(10d6/2)";
		String input2 = "(1d6)(2d6+4)(10d8+43)-(4d12)(12d10)(3d4-2)(1d10*2)(10d6/2)";

		LinkedHashMap<DiceThrow, OperatorType> map = new LinkedHashMap<DiceThrow, OperatorType>();
		map.put(new DiceThrow(1, 6), OperatorType.Addition);
		map.put(new DiceThrow(2, 6, OperatorType.Addition, 4), OperatorType.Addition);
		map.put(new DiceThrow(10, 8, OperatorType.Addition, 43), OperatorType.Subtraction);
		map.put(new DiceThrow(4, 12), OperatorType.Addition);
		map.put(new DiceThrow(12, 10), OperatorType.Addition);
		map.put(new DiceThrow(3, 4, OperatorType.Subtraction, 2), OperatorType.Addition);
		map.put(new DiceThrow(1, 10, OperatorType.Multiplication, 2), OperatorType.Addition);
		map.put(new DiceThrow(10, 6, OperatorType.Division, 2), null);

		CollectionsAssert.assertMapsEquals(map, DiceThrow.parseMany(input1));
		CollectionsAssert.assertMapsEquals(map, DiceThrow.parseMany(input2));
	}

	/**
	 * Test method for
	 * {@link it.d20.tools.android.activities.throwdice.DiceThrow#parse(java.lang.String)}
	 * .
	 */
	@Test
	public void testParse()
	{
		String input1 = "1d6";
		String input2 = "2d6+4";
		String input3 = "10d8+43";
		String input4 = "4d12";
		String input5 = "12d10";
		String input6 = "3d4-2";
		String input7 = "1d10*2";
		String input8 = "10d6/2";

		DiceThrow dt1 = new DiceThrow(1, 6);
		DiceThrow dt2 = new DiceThrow(2, 6, OperatorType.Addition, 4);
		DiceThrow dt3 = new DiceThrow(10, 8, OperatorType.Addition, 43);
		DiceThrow dt4 = new DiceThrow(4, 12);
		DiceThrow dt5 = new DiceThrow(12, 10);
		DiceThrow dt6 = new DiceThrow(3, 4, OperatorType.Subtraction, 2);
		DiceThrow dt7 = new DiceThrow(1, 10, OperatorType.Multiplication, 2);
		DiceThrow dt8 = new DiceThrow(10, 6, OperatorType.Division, 2);

		Assert.assertEquals(dt1, DiceThrow.parse(input1));
		Assert.assertEquals(dt2, DiceThrow.parse(input2));
		Assert.assertEquals(dt3, DiceThrow.parse(input3));
		Assert.assertEquals(dt4, DiceThrow.parse(input4));
		Assert.assertEquals(dt5, DiceThrow.parse(input5));
		Assert.assertEquals(dt6, DiceThrow.parse(input6));
		Assert.assertEquals(dt7, DiceThrow.parse(input7));
		Assert.assertEquals(dt8, DiceThrow.parse(input8));
	}
}
