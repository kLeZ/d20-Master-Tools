package it.gecko.android.utils;

import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;

public class DiceExpressionKeyListener extends NumberKeyListener
{
	private static final char[] CHARACTERS = new char[] { 'd', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+', '-', '*', '/', '\\', '%' };
	private static DiceExpressionKeyListener instance;

	private char[] accepted;

	public DiceExpressionKeyListener()
	{
		super();
		accepted = CHARACTERS;
	}

	@Override
	public char[] getAcceptedChars()
	{
		return accepted;
	}

	/**
	 * @return the instance
	 */
	public static DiceExpressionKeyListener getInstance()
	{
		if (instance == null)
		{
			instance = new DiceExpressionKeyListener();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see android.text.method.TextKeyListener#getInputType()
	 */
	@Override
	public int getInputType()
	{
		return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
	}

	/* (non-Javadoc)
	 * @see android.text.method.NumberKeyListener#filter(java.lang.CharSequence, int, int, android.text.Spanned, int, int)
	 */
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
	{
		CharSequence ret = super.filter(source, start, end, dest, dstart, dend);
		//TODO: DO FILTER!!!
		return ret;
	}
}
