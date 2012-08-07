/**
 * 
 */
package it.d20.tools.android.activities.rng;

import it.d20.tools.android.R;
import it.d20.tools.android.db.PreferencesDatabase;
import it.d20.tools.android.dialogs.RngNumResultsDialog;
import it.gecko.text.RandomStrings;
import it.gecko.text.StringUtils;
import it.gecko.utils.MersenneTwister;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author kLeZ-hAcK
 */
public class RngMainActivity extends Activity
{
	@SuppressWarnings("unused")
	private static final int MAGIC_NUMBER = 33550336;
	public static final MersenneTwister mt = new MersenneTwister();
	private int currentTotalResults = 10;

	ArrayAdapter<String> adapter;
	ListView lv;

	private ArrayList<String> getSelection()
	{
		return getIntent().getExtras().getStringArrayList(RngTabHostActivity.SHARED_P_L_NAME);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rngmain_layout);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		lv = ((ListView) findViewById(R.id.listViewResults));
		lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);
		adapter.setNotifyOnChange(true);
		updateCurentTotalResultsValue();
		setGenerateButtonText(currentTotalResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.rng_main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.rng_clear_res:
				adapter.clear();
				return true;
			case R.id.rng_res_n:
				initResultsDialog().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private Dialog initResultsDialog()
	{
		RngNumResultsDialog dialog = new RngNumResultsDialog(this, currentTotalResults);
		dialog.setOnDismissListener(new RngNumResultsDismissListener(this));
		return dialog;
	}

	public void updateCurentTotalResultsValue()
	{
		PreferencesDatabase db = new PreferencesDatabase(this);
		db.open();
		String key, value;
		key = getString(R.string.rng_prefs_res_to_gen);
		value = db.getPreference(key);
		currentTotalResults = Integer.valueOf(value);
		db.close();
	}

	public void setGenerateButtonText(int value)
	{
		Button b = (Button) findViewById(R.id.generate_button);
		String msg = "";
		msg = String.format("%s (%d)", getString(R.string.buttonGenerateText), value);
		b.setText(msg);
	}

	public void generateRandomNames(View view)
	{
		adapter.clear();
		try
		{
			if (!getSelection().isEmpty())
			{
				RandomStrings rs = new RandomStrings();
				ArrayList<String> results = rs.getRandomNamesFromPatternList(mt, getSelection(), currentTotalResults);
				for (String res : results)
				{
					adapter.add(res);
				}
			}
			else
			{
				Toast.makeText(this, getString(R.string.pl_empty_generate_btn), Toast.LENGTH_SHORT).show();
			}
		}
		catch (Throwable e)
		{
			Toast.makeText(this, StringUtils.throwableToString(e), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * @return the currentTotalResults
	 */
	public int getCurrentTotalResults()
	{
		return currentTotalResults;
	}

	public class RngNumResultsDismissListener implements OnDismissListener
	{
		private RngMainActivity rngMainActivity;

		/**
		 * @param rngMainActivity
		 */
		public RngNumResultsDismissListener(RngMainActivity rngMainActivity)
		{
			this.rngMainActivity = rngMainActivity;
		}

		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
		 */
		@Override
		public void onDismiss(DialogInterface dialog)
		{
			rngMainActivity.updateCurentTotalResultsValue();
			rngMainActivity.setGenerateButtonText(rngMainActivity.getCurrentTotalResults());
		}
	}
}
