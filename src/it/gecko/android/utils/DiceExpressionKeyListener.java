package it.gecko.android.utils;

import it.gecko.utils.Dice;
import it.gecko.utils.OperatorType;

import org.apache.commons.lang3.ArrayUtils;

import android.text.InputType;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class DiceExpressionKeyListener extends NumberKeyListener
{
	private static final char[] BASE_CHARS = new char[]
	{ Dice.DICE_TOKEN, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
	public static final char[] CHARACTERS = ArrayUtils.addAll(BASE_CHARS, OperatorType.getOperators());

	private final char[] accepted;

	private TextView textView;
	private CheckBox debugCheck;

	public DiceExpressionKeyListener(TextView textView, CheckBox debugCheck)
	{
		super();
		this.textView = textView;
		this.debugCheck = debugCheck;
		accepted = CHARACTERS;
	}

	@Override
	public char[] getAcceptedChars()
	{
		return accepted;
	}

	/* (non-Javadoc)
	 * @see android.text.method.TextKeyListener#getInputType()
	 */
	@Override
	public int getInputType()
	{
		return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
	}

	private boolean isDebug()
	{
		return debugCheck.isChecked();
	}

	/*
	 * Spiegazione:
	 * Il contenuto della TextEdit è descritto in questi 6 parametri.
	 * Andando in ordine, <source> contiene il NUOVO valore della TextEdit,
	 * compreso il nuovo carattere inserito.
	 * <start> e <end> sono riferiti rispettivamente alla posizione del
	 * testo <source> nella TextEdit.
	 * Analogamente <dest> è il contenuto CORRENTE della TextEdit, cioè ciò
	 * che era presente PRIMA del nuovo carattere che si sta processando.
	 * Infine <dstart> e <dend> sono i corrispettivi indici di inizio e
	 * fine della "vecchia" stringa del contenuto della TextEdit.
	 * Il metodo super.filter(...) filtra esattamente i caratteri che ho
	 * specificato sopra in getAcceptedChars()
	 * Ovviamente dato questo funzionamento, ottimale per la verità, è
	 * necessario solo attuare la logica per mantenere la struttura dei
	 * dati, in questo caso l'espressione del lancio di dadi.
	 * La logica per l'espressione è aiutata dalla classe Dice che
	 * permette di strutturare l'espressione in un oggetto.
	 * Ok, strutturiamo la logica per la struttura del testo:
	 * Intanto, devo sempre cercare la 'D' per delimitare il Dice.
	 * Se riesco a riconoscerne una devo sempre controllare che prima di
	 * questa ci sia un numero,
	 * in caso contrario lancio un toast in cui dico che prima della
	 * definizione del dado
	 * si deve inserire la quantità (Es.: 1d6, 6d4, etc.)
	 * Se non trovo una 'D' lascio correre il filtro, perché comunque sia i
	 * caratteri vengono già filtrati
	 * dal super metodo.
	 * Se il nuovo carattere non è una 'D' controllare che non sia un
	 * segno, a meno che dopo la 'D' non ci siano dei numeri, in quel caso
	 * è ammesso.
	 * 
	 * @see android.text.method.NumberKeyListener#filter(java.lang.CharSequence,
	 *      int, int, android.text.Spanned, int, int)
	 */
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
	{
		CharSequence ret = super.filter(source, start, end, dest, dstart, dend);
		if (isDebug())
		{
			textView.append(printFilter(source, start, end, dest, dstart, dend, ret));
		}

		if (source.length() > 0)
		{
			char c = source.charAt(0);
			String sdest = dest.toString();
			if (sdest.isEmpty() && !Character.isDigit(c))
			{
				deleteFilter(source, start, end, dest, dstart, dend, ret);
			}
			else if (c == Dice.DICE_TOKEN)
			{
				if (!Character.isDigit(last(sdest)) || sdest.contains(Dice.DICE_TOKEN_S))
				{
					deleteFilter(source, start, end, dest, dstart, dend, ret);
				}
			}
			else if (OperatorType.isOperator(c))
			{
				if (!Character.isDigit(last(sdest)) || OperatorType.containsOperator(sdest))
				{
					deleteFilter(source, start, end, dest, dstart, dend, ret);
				}
			}
		}
		return ret;
	}

	private char last(String sp)
	{
		return sp.charAt(sp.length() - 1);
	}

	private void deleteFilter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend, CharSequence ret)
	{
		ret = "";
		source = "";
		start = 0;
		end = 0;
		dstart = 0;
		dend = dest.length();
	}

	private String printFilter(final CharSequence source, final int start, final int end, final Spanned dest, final int dstart, final int dend, final CharSequence ret)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(" ").append("source: ").append(source).append(";");
		sb.append(" ").append("start: ").append(start).append(";");
		sb.append(" ").append("end: ").append(end).append(";");
		sb.append(" ").append("dest: ").append(dest).append(";");
		sb.append(" ").append("dstart: ").append(dstart).append(";");
		sb.append(" ").append("dend: ").append(dend).append(";");
		sb.append(" ").append("ret: ").append(ret).append(" ");
		sb.append("]");
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}
}
