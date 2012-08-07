/**
 * 
 */
package it.d20.tools.android.activities.rng;

import it.d20.tools.android.R;
import it.d20.tools.android.db.PatternDatabase;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * @author kLeZ-hAcK
 */
public class RngTabHostActivity extends TabActivity
{
	public static final String SHARED_P_L_NAME = "it.d20.tools.android.activities.rng.PatternList";
	ArrayList<String> patternListShared = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rng_layout);

		TabHost tabHost = getTabHost();

		PatternDatabase db = new PatternDatabase(this);
		db.open();
		patternListShared = db.getPatternList();
		db.close();

		// Tab for Main
		TabSpec mainspec = tabHost.newTabSpec("RngMain");
		// setting Title for the Tab
		mainspec.setIndicator(getString(R.string.tab_rng_main_title));
		Intent mainIntent = new Intent(this, RngMainActivity.class);
		mainIntent = mainIntent.putStringArrayListExtra(SHARED_P_L_NAME, patternListShared);
		mainspec.setContent(mainIntent);

		// Tab for Pattern List
		TabSpec patternlistspec = tabHost.newTabSpec("RngPatternList");
		patternlistspec.setIndicator(getString(R.string.tab_rng_patlst_title));
		Intent patternlistIntent = new Intent(this, RngPatternListActivity.class);
		patternlistIntent = patternlistIntent.putStringArrayListExtra(SHARED_P_L_NAME, patternListShared);
		patternlistspec.setContent(patternlistIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(mainspec); // Adding Main tab
		tabHost.addTab(patternlistspec); // Adding Pattern List tab
	}
}
