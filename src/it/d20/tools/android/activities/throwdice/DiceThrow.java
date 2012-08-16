package it.d20.tools.android.activities.throwdice;

import it.gecko.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DiceThrow
{
	public static final char DICE_TOKEN = 'D';
	public static final char OPENED_DICE = '(';
	public static final char CLOSED_DICE = ')';

	private final int nThrows;
	private final int nFaces;
	private final OperatorType modifierOperator;
	private final int modifier;

	public DiceThrow(int nThrows, int nFaces)
	{
		this.nThrows = nThrows;
		this.nFaces = nFaces;
		modifierOperator = null;
		modifier = Integer.MIN_VALUE;
	}

	public DiceThrow(int nThrows, int nFaces, OperatorType modifierOperator, int modifier)
	{
		this.nThrows = nThrows;
		this.nFaces = nFaces;
		this.modifierOperator = modifierOperator;
		this.modifier = modifier;
	}

	/**
	 * @return the nThrows
	 */
	public int getnThrows()
	{
		return nThrows;
	}

	/**
	 * @return the nFaces
	 */
	public int getnFaces()
	{
		return nFaces;
	}

	/**
	 * @return the modifierOperator
	 */
	public OperatorType getModifierOperator()
	{
		return modifierOperator;
	}

	/**
	 * @return the modifier
	 */
	public int getModifier()
	{
		return modifier;
	}

	public static boolean isDiceThrow(String dt)
	{
		return parse(dt) != null;
	}

	public static LinkedHashMap<DiceThrow, OperatorType> parseMany(String dts)
	{
		LinkedHashMap<DiceThrow, OperatorType> ret = new LinkedHashMap<DiceThrow, OperatorType>();
		ArrayList<String> parts = Utils.splitEncolosed(dts, OPENED_DICE, CLOSED_DICE);
		DiceThrow dt = null;
		OperatorType ot = null;

		for (int i = 0; (i + 1) <= parts.size(); i += 2)
		{
			String part1 = parts.get(i);
			String part2 = (i + 1) < parts.size() ? parts.get(i + 1) : "";

			if (isDiceThrow(part1))
			{
				if (dt == null)
				{
					dt = DiceThrow.parse(part1);
				}
				else
				{
					ret.put(dt, OperatorType.Addition);
					dt = DiceThrow.parse(part1);
					ot = null;
				}
			}

			if (isDiceThrow(part2))
			{
				ret.put(dt, OperatorType.Addition);
				dt = DiceThrow.parse(part2);
			}
			else
			{
				if (OperatorType.isOperator(part2))
				{
					ot = OperatorType.getOperator(part2);
				}
				ret.put(dt, ot);
				dt = null;
			}
			ot = null;
		}
		return ret;
	}

	public static DiceThrow parse(String dt)
	{
		DiceThrow ret = null;
		dt = dt.toUpperCase();
		String[] diceparts = dt.split(String.valueOf(DICE_TOKEN));
		if (diceparts.length == 2)
		{
			int nThrows = Integer.valueOf(diceparts[0]), nFaces = 0, modifier = 0;
			String nFacesBld = "";
			OperatorType opType = null;
			char[] faceParts = diceparts[1].toCharArray();
			for (char c : faceParts)
			{
				String current = String.valueOf(c);
				if (Utils.isInteger(current))
				{
					nFacesBld = nFacesBld.concat(current);
				}
				else if ((opType = OperatorType.parseOperator(c)) != null)
				{
					break;
				}
				else
				{
					break;
				}
			}
			nFaces = Integer.valueOf(nFacesBld);

			if (opType != null)
			{
				String[] bonusParts = diceparts[1].split(opType.toEscapedString());
				if ((bonusParts.length == 2) && Utils.isInteger(bonusParts[1]))
				{
					modifier = Integer.valueOf(bonusParts[1]);
				}
				ret = new DiceThrow(nThrows, nFaces, opType, modifier);
			}
			else
			{
				ret = new DiceThrow(nThrows, nFaces);
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		boolean ret = false;
		if (o instanceof DiceThrow)
		{
			ret = getnThrows() == ((DiceThrow) o).getnThrows();
			ret &= getnFaces() == ((DiceThrow) o).getnFaces();
			ret &= getModifierOperator() == ((DiceThrow) o).getModifierOperator();
			ret &= getModifier() == ((DiceThrow) o).getModifier();
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(nThrows).append(DICE_TOKEN).append(nFaces);
		if (modifierOperator != null)
		{
			sb.append(modifierOperator).append(modifier);
		}
		return sb.toString();
	}
}
