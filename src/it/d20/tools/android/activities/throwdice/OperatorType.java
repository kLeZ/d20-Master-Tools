package it.d20.tools.android.activities.throwdice;

import it.gecko.utils.Utils;

public enum OperatorType
{
	Addition, Subtraction, Multiplication, Division;

	public static OperatorType parseOperator(char op)
	{
		OperatorType type = null;
		switch (op)
		{
			case '+':
				type = Addition;
				break;
			case '-':
				type = Subtraction;
				break;
			case '*':
				type = Multiplication;
				break;
			case '/':
				type = Division;
				break;
		}
		return type;
	}

	public int doOperation(int op1, int op2)
	{
		int ret = 0;
		switch (this)
		{
			case Addition:
				ret = add(op1, op2);
				break;
			case Division:
				ret = divide(op1, op2);
				break;
			case Multiplication:
				ret = multiply(op1, op2);
				break;
			case Subtraction:
				ret = subtract(op1, op2);
				break;
		}
		return ret;
	}

	private int add(int op1, int op2)
	{
		check(op1, op2);
		return op1 + op2;
	}

	private int subtract(int op1, int op2)
	{
		check(op1, op2);
		return op1 - op2;
	}

	private int divide(int op1, int op2)
	{
		check(op1, op2);
		return op1 / op2;
	}

	private int multiply(int op1, int op2)
	{
		check(op1, op2);
		return op1 * op2;
	}

	private void check(int op1, int op2)
	{
		/* RULES:
		 * - A number cannot be less than 0, if it is, it will become 0
		 * - A couple of numbers should be reversed if the operation is division and the divider is equal or less than 0.
		 * - The result of the operation cannot be negative, so if subtraction need to reverse operands order
		 */
		if (op1 < 0)
		{
			op1 = 0;
		}
		if (op2 < 0)
		{
			op2 = 0;
		}
		if ((this == Division) && (op2 <= 0))
		{
			Utils.swap(op1, op2);
		}
		if ((this == Subtraction) && ((op1 - op2) < 0))
		{
			Utils.swap(op1, op2);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		String ret = "";
		switch (this)
		{
			case Addition:
				ret = "+";
				break;
			case Division:
				ret = "/";
				break;
			case Multiplication:
				ret = "*";
				break;
			case Subtraction:
				ret = "-";
				break;
		}
		return ret.toString();
	}
}
