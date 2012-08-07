/**
 * 
 */
package it.d20.tools.android.db;

import java.util.ArrayList;
import it.d20.tools.android.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author kLeZ-hAcK
 */
public class PatternDatabase extends Database
{
	private String patFld, delFld;

	/**
	 * @param context
	 */
	public PatternDatabase(Context context)
	{
		super(context);
		table = getParm(R.string.Q_namepatterns_table_name);
		patFld = getParm(R.string.Q_namepatterns_pat_field_name);
		delFld = getParm(R.string.Q_namepatterns_del_field_name);
		selectedCols = new String[] { patFld };
	}

	public long setPattern(String pattern)
	{
		long ret = 0;
		if (checkPattern(pattern))
		{
			ContentValues cv = new ContentValues();
			cv.put(patFld, pattern);
			cv.put(delFld, 0);
			String where = String.format("%s = '%s'", patFld, pattern);
			ret = db.update(table, cv, where, null);
		}
		else
		{
			ContentValues cv = new ContentValues();
			cv.put(patFld, pattern);
			ret = db.insert(table, null, cv);
		}
		return ret;
	}

	public long removePattern(String pattern)
	{
		long ret = 0;
		if (checkPattern(pattern))
		{
			ContentValues cv = new ContentValues();
			cv.put(patFld, pattern);
			cv.put(delFld, 1);
			String where = String.format("%s = '%s'", patFld, pattern);
			ret = db.update(table, cv, where, null);
		}
		return ret;
	}

	public boolean checkPattern(String pattern)
	{
		boolean ret = false;
		Cursor cur = query(String.format("%s = '%s'", patFld, pattern));
		ret = (cur != null) && (cur.getCount() > 0);
		return ret;
	}

	public ArrayList<String> getPatternList()
	{
		ArrayList<String> ret = new ArrayList<String>();

		Cursor cur = query(String.format("%s = %d", delFld, 0));
		if ((cur != null) && (cur.getCount() > 0))
		{
			cur.moveToFirst();
			do
			{
				ret.add(cur.getString(cur.getColumnIndex(patFld)));
			}
			while (cur.moveToNext());
		}
		return ret;
	}
}
