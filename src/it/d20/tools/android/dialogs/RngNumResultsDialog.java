/**
 * 
 */
package it.d20.tools.android.dialogs;

import it.d20.tools.android.R;
import it.d20.tools.android.db.PreferencesDatabase;
import it.gecko.android.widgets.NumberPicker;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author kLeZ-hAcK
 */
public class RngNumResultsDialog extends Dialog
{
	private int currentTotalResults;
	private NumberPicker np;

	/**
	 * @param context
	 */
	public RngNumResultsDialog(Context context, int currentTotalResults)
	{
		super(context);
		this.currentTotalResults = currentTotalResults;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rng_res_n_layout);
		setTitle(R.string.rng_res_n_title);
		np = ((NumberPicker) findViewById(R.id.np_n_results));
		np.setCurrent(currentTotalResults);
		setCancelable(true);
		((Button) findViewById(R.id.save_n_res)).setOnClickListener(new SavePreferencesListener(this));
	}

	/**
	 * @return the currentTotalResults
	 */
	public int getCurrentTotalResults()
	{
		if (np.getCurrent() != currentTotalResults)
		{
			currentTotalResults = np.getCurrent();
		}
		return currentTotalResults;
	}

	public class SavePreferencesListener implements View.OnClickListener
	{
		private RngNumResultsDialog dialog;

		/**
         * 
         */
		public SavePreferencesListener(RngNumResultsDialog dialog)
		{
			this.dialog = dialog;
		}

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v)
		{
			PreferencesDatabase db = new PreferencesDatabase(dialog.getContext());
			db.open();
			String key, value;
			key = dialog.getContext().getString(R.string.rng_prefs_res_to_gen);
			value = String.valueOf(dialog.getCurrentTotalResults());
			db.setPreference(key, value);
			db.close();
			dismiss();
		}
	}
}
