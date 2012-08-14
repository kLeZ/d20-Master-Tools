package it.d20.tools.android.activities.throwdice;

import it.gecko.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

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

	public static HashMap<DiceThrow, OperatorType> parseMany(String dts)
	{
		HashMap<DiceThrow, OperatorType> ret = new HashMap<DiceThrow, OperatorType>();
		ArrayList<String> parts = Utils.splitEncolosed(dts, OPENED_DICE, CLOSED_DICE);
		DiceThrow dt = null;
		OperatorType ot = null;
		for (String part : parts)
		{
			if ((dt == null) && isDiceThrow(part))
			{
				dt = parse(part);
			}
			else if ((dt != null) && !isDiceThrow(part))
			{
				char[] charParts = part.toCharArray();
				for (char c : charParts)
				{
					if ((ot = OperatorType.parseOperator(c)) != null)
					{
						break;
					}
				}
			}
			else if ((dt != null) && isDiceThrow(part) && (ot == null))
			{
				ret.put(dt, OperatorType.Addition);
				dt = parse(part);
			}
			else if ((dt != null) && isDiceThrow(part) && (ot != null))
			{
				ret.put(dt, ot);
				dt = parse(part);
				ot = null;
			}
		}
		return ret;
	}

	public static boolean isDiceThrow(String dt)
	{
		return parse(dt) != null;
	}

	public static DiceThrow parse(String dt)
	{
		DiceThrow ret = null;
		String[] diceparts = dt.split(String.valueOf(DICE_TOKEN));
		if (diceparts.length == 2)
		{
			int nThrows = Integer.valueOf(diceparts[0]), nFaces = 0, power = 0, modifier = 0;
			OperatorType opType = null;
			char[] faceParts = diceparts[1].toCharArray();
			for (int i = faceParts.length - 1; i >= 0; i--)
			{
				String current = String.valueOf(faceParts[i]);
				if (Utils.isInteger(current))
				{
					nFaces += (int) (Integer.valueOf(current) * Math.pow(10, power++));
				}
				else if ((opType = OperatorType.parseOperator(faceParts[i])) != null)
				{
					break;
				}
				else
				{
					break;
				}
			}

			if (opType != null)
			{
				String[] bonusParts = diceparts[1].split(opType.toString());
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
}
