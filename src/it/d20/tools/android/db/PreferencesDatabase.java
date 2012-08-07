/**
 * 
 */
package it.d20.tools.android.db;

import it.d20.tools.android.R;
import it.gecko.text.StringUtils;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

/**
 * @author kLeZ-hAcK
 */
public class PreferencesDatabase extends Database
{
	private String keyFld, valFld;

	/**
	 * @param context
	 */
	public PreferencesDatabase(Context context)
	{
		super(context);
		table = getParm(R.string.Q_preferences_table_name);
		keyFld = getParm(R.string.Q_preferences_key_field_name);
		valFld = getParm(R.string.Q_preferences_value_field_name);
		selectedCols = new String[] { keyFld, valFld };
	}

	/* (non-Javadoc)
	 * @see it.d20.tools.android.db.Database#setPreference(java.lang.String, java.lang.String)
	 */
	public long setPreference(String key, String value)
	{
		long ret = 0;
		if (checkPreference(key))
		{
			ContentValues cv = new ContentValues();
			cv.put(keyFld, key);
			cv.put(valFld, value);
			String where = String.format("%s = '%s'", keyFld, key);
			ret = db.update(table, cv, where, null);
		}
		else
		{
			ContentValues cv = new ContentValues();
			cv.put(keyFld, key);
			cv.put(valFld, value);
			try
			{
				ret = db.insertOrThrow(table, null, cv);
			}
			catch (SQLException e)
			{
				Log.e("it.d20.tools.android.db.PreferenceDatabase[setPreference]", StringUtils.throwableToString(e));
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see it.d20.tools.android.db.Database#removePreference(java.lang.String)
	 */
	public int removePreference(String key)
	{
		int ret = 0;
		if (checkPreference(key))
		{
			String where;
			where = String.format("%s = '%s'", keyFld, key);
			ret = db.delete(table, where, null);
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see it.d20.tools.android.db.Database#checkPreference(java.lang.String)
	 */
	public boolean checkPreference(String key)
	{
		return !getPreference(key).isEmpty();
	}

	public String getPreference(String key)
	{
		String where, ret = "";
		where = String.format("%s = '%s'", keyFld, key);
		Cursor cur = query(where);
		if ((cur != null) && (cur.getCount() > 0))
		{
			cur.moveToFirst();
			ret = cur.getString(cur.getColumnIndex(valFld));
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see it.d20.tools.android.db.Database#getPreferences()
	 */
	public Map<String, String> getPreferences()
	{
		Map<String, String> ret = new HashMap<String, String>();
		Cursor cur = query(null);
		if ((cur != null) && (cur.getCount() > 0))
		{
			cur.moveToFirst();
			String key, value;
			do
			{
				key = cur.getString(cur.getColumnIndex(keyFld));
				value = cur.getString(cur.getColumnIndex(valFld));
				ret.put(key, value);
			}
			while (cur.moveToNext());
		}
		return ret;
	}
}
