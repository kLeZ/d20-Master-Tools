/**
 * 
 */
package it.d20.tools.android.activities.rng;

import it.d20.tools.android.R;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

import android.content.res.Resources;

/**
 * @author kLeZ-hAcK
 */
public class RngUtils
{
	public static Vector<String> getPatternList(Resources resources)
	{
		InputStream is = resources.openRawResource(R.raw.namesrc);
		Scanner scn = new Scanner(is);
		Vector<String> namesrc = new Vector<String>();
		while (scn.hasNextLine())
		{
			namesrc.add(scn.nextLine());
		}
		return namesrc;
	}
}
