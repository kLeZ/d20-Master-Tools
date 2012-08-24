package it.gecko.android.utils;

import it.gecko.utils.Dice;
import it.gecko.utils.OperatorType;
import android.text.*;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class DiceExpressionKeyListener extends BaseKeyListener implements InputFilter
{
	private static final char[] BASE_CHARS = new char[]
	{ Dice.DICE_TOKEN, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
	private static final String TAG = "it.gecko.android.utils.DiceExpressionKeyListener";

	private final char[] accepted;

	public DiceExpressionKeyListener()
	{
		accepted = new char[BASE_CHARS.length + OperatorType.getOperators().length];
		System.arraycopy(BASE_CHARS, 0, accepted, 0, BASE_CHARS.length);
		System.arraycopy(OperatorType.getOperators(), 0, accepted, BASE_CHARS.length, OperatorType.getOperators().length);
	}

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
	 * -------------------------------------------------------------------------
	 * 
	 * Allora il metodo filter viene richiamato dal metodo setText() della TextView
	 * (ricordo che la EditText è una TextView che implementa l'interfaccia Editable)
	 * con questo SPECIFICO codice:
	 * 
	 * |||| Pseudo codice per spiegare da dove viene mFilters. ||||
	 * 
	 * 
	 * InputFilter[] mFilters = <-- setKeyListener(KeyListener);
	 * 
	 * 
	 * |||| Pseudo codice per spiegare da dove viene mFilters. ||||
	 * 
	 * 
	    int n = mFilters.length;
	    for (int i = 0; i < n; i++) {
	        CharSequence out = mFilters[i].filter(text, 0, text.length(),
	                                              EMPTY_SPANNED, 0, 0);
	        if (out != null) {
	            text = out;
	        }
	    }
	 * 
	 * 
	 * Ne consegue che in realtà quello che conta è il CharSequence di return,
	 * fondamentale perché sovrascrive ogni volta il testo.
	 * 
	 * 
	 * @see android.text.method.NumberKeyListener#filter(java.lang.CharSequence,
	 *      int, int, android.text.Spanned, int, int)
	 */
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
	{
		CharSequence ret = null;
		boolean filter = end > 1;
		int trueend = end;
		int i;
		for (i = start; i < end; i++)
		{
			if (!ok(getAcceptedChars(), source.charAt(i)))
			{
				break;
			}
		}

		if (i == end)
		{
			// It was all OK.
			ret = null;
		}

		if ((end - start) == 1)
		{
			// It was not OK, and there is only one char, so nothing remains.
			ret = "";
		}

		SpannableStringBuilder filtered = new SpannableStringBuilder(source, start, end);
		i -= start;
		end -= start;

		// Only count down to i because the chars before that were all OK.
		for (int j = end - 1; j >= i; j--)
		{
			if (!ok(getAcceptedChars(), source.charAt(j)))
			{
				filtered.delete(j, j + 1);
			}
		}

		/*
		 * Quando filtro un carattere rimane nella variabile source, appeso,
		 * Ricordarsi di toglierlo da li controllando end, in modo da non
		 * trovarselo alla chiamata successiva.
		 */
		if (filter)
		{
			source = source.subSequence(0, trueend - 1);
		}

		ret = filtered;

		log(printFilter(source, start, end, dest, dstart, dend, filtered));

		if (ret.length() > 0)
		{
			// Entro solo se source è valorizzato con qualcosa
			log("I'm in -> source.length() > 0");
			char c = last(ret);
			String sdest = dest.toString();
			if (sdest.isEmpty() && !Character.isDigit(c))
			{
				/*
				 * La regola specifica che l'espressione deve cominciare con un numero.
				 * In questo caso la destinazione (il contenuto "corrente" della TextView) è vuota
				 * e controllo se il primo carattere che viene inserito è un numero.
				 * Non lo è, di conseguenza non inserisco nulla.
				 */
				log("I'm in -> sdest.isEmpty() && !Character.isDigit(c)");
				ret = "";
			}
			else if (c == Dice.DICE_TOKEN)
			{
				/*
				 * La regola specifica che ci deve essere un "Dice Token" in questo caso 'D'
				 * Se sono in presenza di un Dice Token posso andare avanti.
				 */
				log("I'm in -> c == Dice.DICE_TOKEN");
				if (!Character.isDigit(last(sdest)) || sdest.contains(Dice.DICE_TOKEN_S))
				{
					/*
					 * Il Dice Token in questo caso non è al posto giusto perché
					 * prima di esso deve comparire un numero, che non compare.
					 */
					log("I'm in -> !Character.isDigit(last(sdest)) || sdest.contains(Dice.DICE_TOKEN_S)");
					ret = ret.subSequence(0, ret.length() - 1); // Ho messo -1 perché il subSequence tratta l'end come esclusivo.
				}
			}
			else if (OperatorType.isOperator(c))
			{
				/*
				 * La regola definisce che opzionalmente può esserci un operatore aritmetico.
				 * Se sono in presenza di un operatore aritmetico posso proseguire.
				 */
				log("I'm in -> OperatorType.isOperator(c)");
				if (!Character.isDigit(last(sdest)) || OperatorType.containsOperator(sdest) || !sdest.contains(Dice.DICE_TOKEN_S))
				{
					/*
					 * L'operatore non può essere ammesso perché:
					 * - ne è già presente uno;
					 * - l'ultimo carattere non è un numero, quindi l'espressione di base non è completa
					 * - l'espressione non contiene un Dice Token, quindi non è ancora completa
					 */
					log("I'm in -> !Character.isDigit(last(sdest)) || OperatorType.containsOperator(sdest) || !sdest.contains(Dice.DICE_TOKEN_S)");
					ret = ret.subSequence(0, ret.length() - 1); // Ho messo -1 perché il subSequence tratta l'end come esclusivo.
				}
			}
		}
		return ret;
	}

	protected static boolean ok(char[] accept, char c)
	{
		for (int i = accept.length - 1; i >= 0; i--)
		{
			if (accept[i] == c) { return true; }
		}

		return false;
	}

	private char last(CharSequence s)
	{
		return s.charAt(s.length() - 1);
	}

	private char last(String s)
	{
		return s.charAt(s.length() - 1);
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

	private void log(String text)
	{
		Log.d(TAG, text);
	}

	protected int lookup(KeyEvent event, Spannable content)
	{
		return event.getMatch(getAcceptedChars(), event.getMetaState() | getMetaState(content));
	}

	@Override
	public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event)
	{
		int selStart, selEnd;

		{
			int a = Selection.getSelectionStart(content);
			int b = Selection.getSelectionEnd(content);

			selStart = Math.min(a, b);
			selEnd = Math.max(a, b);
		}

		if ((selStart < 0) || (selEnd < 0))
		{
			selStart = selEnd = 0;
			Selection.setSelection(content, 0);
		}

		int i = event != null ? lookup(event, content) : 0;
		int repeatCount = event != null ? event.getRepeatCount() : 0;
		if (repeatCount == 0)
		{
			if (i != 0)
			{
				if (selStart != selEnd)
				{
					Selection.setSelection(content, selEnd);
				}

				content.replace(selStart, selEnd, String.valueOf((char) i));

				adjustMetaAfterKeypress(content);
				return true;
			}
		}
		else if ((i == '0') && (repeatCount == 1))
		{
			// Pretty hackish, it replaces the 0 with the +

			if ((selStart == selEnd) && (selEnd > 0) && (content.charAt(selStart - 1) == '0'))
			{
				content.replace(selStart - 1, selEnd, String.valueOf('+'));
				adjustMetaAfterKeypress(content);
				return true;
			}
		}

		adjustMetaAfterKeypress(content);
		return super.onKeyDown(view, content, keyCode, event);
	}
}
