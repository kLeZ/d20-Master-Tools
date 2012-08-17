package it.d20.tools.android.activities.throwdice;

import it.d20.tools.android.R;
import it.gecko.android.utils.DiceExpressionKeyListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ThrowDiceActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.throwdice_layout);
		EditText et = (EditText) findViewById(R.id.diceExpressionEdit);
		et.setKeyListener(DiceExpressionKeyListener.getInstance(this));
	}

	public void onThrowDice(View view)
	{

	}
}
