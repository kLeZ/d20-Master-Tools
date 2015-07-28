package it.d20.tools.android.activities.main;

import it.d20.tools.android.R;
import android.app.Activity;
import android.os.Bundle;

public class InfoActivity extends Activity
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
	}
}
