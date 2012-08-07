/**
 * 
 */
package it.gecko.text;

import it.gecko.utils.MersenneTwister;

import java.util.ArrayList;

/**
 * @author kLeZ-hAcK
 */
public class RandomStrings
{
	@SuppressWarnings("unchecked")
	public ArrayList<String> getRandomNamesFromPatternList(final MersenneTwister random, final ArrayList<String> patterns, int nToGenerate)
	{
		ArrayList<String> ret = new ArrayList<String>();
		String pattern;
		int randomNumber;
		ArrayList<String> patternsClone = (ArrayList<String>) patterns.clone();
		for (int i = 0; i < nToGenerate; i++)
		{
			if (patternsClone.isEmpty())
			{
				patternsClone = (ArrayList<String>) patterns.clone();
			}
			int seed = patternsClone.size() - 1;
			randomNumber = random.nextInt((seed > 0 ? seed : 1));
			pattern = patternsClone.get(randomNumber);
			ret.add(generateRandomName(random, pattern));
			patternsClone.remove(pattern);
		}
		return ret;
	}

	/**
	 * @param random
	 * @param pattern
	 * @return
	 */
	private String generateRandomName(MersenneTwister random, String pattern)
	{
		String ret = "";
		// Dichiarazione costanti
		final char LNV = '@', // Consonante Minuscola
		UNV = '$', // Consonante Maiuscola
		LV = '!', // Vocale Minuscola
		UV = '&', // Vocale Maiuscola
		AL = '*', // Qualsiasi lettera minuscola
		AU = '~', // Qualsiasi lettera maiuscola
		I = '#', // Numero Intero
		OU = '^', // Una 'o' o una 'u' 
		UOU = '>', // Una 'O' o una 'U'
		AE = '%', // Una 'a' o una 'e'
		UAE = '_';// Una 'A' o una 'E'

		// ------------------------------------------------------------------
		// Dichiarazione array di caratteri
		char[] ArrayAlfabeto = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		char[] ArrayConsonanti = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z' };
		char[] ArrayVocali = { 'a', 'e', 'i', 'o', 'u' };
		// ------------------------------------------------------------------
		for (char c : pattern.toCharArray())
		{
			String generated;
			boolean lower = false;
			switch (c)
			{
				case LNV:
					lower = true;
				case UNV:
					generated = String.valueOf(ArrayConsonanti[random.nextInt(20)]);
					break;
				case LV:
					lower = true;
				case UV:
					generated = String.valueOf(ArrayVocali[random.nextInt(4)]);
					break;
				case AL:
					lower = true;
				case AU:
					generated = String.valueOf(ArrayAlfabeto[random.nextInt(25)]);
					break;
				case I:
					generated = String.valueOf(random.nextInt(9));
					break;
				case OU:
					lower = true;
				case UOU:
					generated = String.valueOf(ArrayVocali[random.next(3, 4)]);
					break;
				case AE:
					lower = true;
				case UAE:
					generated = String.valueOf(ArrayVocali[random.nextInt(1)]);
					break;
				default:
					generated = String.valueOf(c);
					break;
			}
			ret += (lower ? generated.toLowerCase() : generated.toUpperCase());
		}
		return ret;
	}
}
