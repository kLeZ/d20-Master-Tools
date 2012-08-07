/**
 * 
 */
package it.d20.tools.android.db;

import it.d20.tools.android.R;
import it.d20.tools.android.activities.rng.RngUtils;

import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author kLeZ-hAcK
 */
public class DbHelper extends SQLiteOpenHelper
{
	private Context ctx;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DbHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		ctx = context;
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String[] queries = ctx.getResources().getStringArray(R.array.Q_d20MasterTools_db_create);
		for (String query : queries)
		{
			db.execSQL(query);
		}
		initPatterns(db);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (newVersion > oldVersion)
		{
			String[] queries = ctx.getResources().getStringArray(R.array.Q_d20MasterTools_db_update);
			for (String query : queries)
			{
				db.execSQL(query);
			}

			String table, patFld;
			table = ctx.getString(R.string.Q_namepatterns_table_name);
			patFld = ctx.getString(R.string.Q_namepatterns_pat_field_name);
			Cursor cur = db.query(table, new String[] { patFld }, null, null, null, null, null);
			if ((cur == null) || (cur.getCount() <= 0))
			{
				initPatterns(db);
			}
		}
	}

	private void initPatterns(SQLiteDatabase db)
	{
		Vector<String> defaultPatternList = RngUtils.getPatternList(ctx.getResources());
		String table, patFld;
		table = ctx.getString(R.string.Q_namepatterns_table_name);
		patFld = ctx.getString(R.string.Q_namepatterns_pat_field_name);

		ContentValues cv;
		for (String pattern : defaultPatternList)
		{
			cv = new ContentValues();
			cv.put(patFld, pattern);
			db.insert(table, null, cv);
		}
	}
}
