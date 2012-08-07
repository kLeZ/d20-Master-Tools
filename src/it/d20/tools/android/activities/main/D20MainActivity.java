package it.d20.tools.android.activities.main;

import it.d20.tools.android.R;
import it.d20.tools.android.activities.rng.RngTabHostActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class D20MainActivity extends Activity
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d20_main);
	}

	public void goToRng(View view)
	{
		startActivity(new Intent(this, RngTabHostActivity.class));
	}

	public void goToThrowDice(View view)
	{

	}
}
