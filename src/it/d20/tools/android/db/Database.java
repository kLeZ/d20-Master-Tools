/**
 * 
 */
package it.d20.tools.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author kLeZ-hAcK
 */
public abstract class Database
{
	private static final String DB_NAME = "d20MasterTools";
	private static final int DB_VERSION = 1;

	private DbHelper helper;
	private Context ctx;
	protected SQLiteDatabase db;
	protected String table;
	protected String[] selectedCols;

	public Database(Context context)
	{
		ctx = context;
		helper = new DbHelper(ctx, DB_NAME, null, DB_VERSION);
	}

	public void open()
	{
		db = helper.getWritableDatabase();
	}

	public void close()
	{
		db.close();
	}

	protected String getParm(int paramID)
	{
		return ctx.getString(paramID);
	}

	protected Cursor query(String where)
	{
		return db.query(table, selectedCols, where, null, null, null, null);
	}
}
