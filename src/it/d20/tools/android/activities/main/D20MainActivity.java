package it.d20.tools.android.activities.main;

import it.d20.tools.android.R;
import it.d20.tools.android.activities.rng.RngTabHostActivity;
import it.d20.tools.android.activities.throwdice.ThrowDiceActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

	public void goTo(View view)
	{
		Button button = (Button) view;
		Intent intent;
		switch (button.getId())
		{
			case R.id.rng_button:
				intent = new Intent(this, RngTabHostActivity.class);
				break;
			case R.id.throwdice_button:
				intent = new Intent(this, ThrowDiceActivity.class);
				break;
			default:
				intent = new Intent(this, D20MainActivity.class);
				break;
		}
		startActivity(intent);
	}
}
