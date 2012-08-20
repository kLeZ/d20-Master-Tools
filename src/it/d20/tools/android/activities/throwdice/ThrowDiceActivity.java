package it.d20.tools.android.activities.throwdice;

import it.d20.tools.android.R;
import it.gecko.android.utils.DiceExpressionKeyListener;
import it.gecko.utils.Dice;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ThrowDiceActivity extends Activity
{
	private TextView tvResults;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.throwdice_layout);
		EditText et = (EditText) findViewById(R.id.diceExpressionEdit);
		tvResults = (TextView) findViewById(R.id.throwdice_tvResults);
		et.setKeyListener(new DiceExpressionKeyListener(tvResults, (CheckBox) findViewById(R.id.throwdice_debugCheck)));
	}

	public void onThrowDice(View view)
	{
		Integer rollResult = 0;
		if (tvResults.getText().length() >= 3)
		{
			rollResult = Dice.rollSum(Dice.parseMany(tvResults.getText().toString()));
		}
		Toast.makeText(this, getText(R.string.throwdice_rolled_show_text) + rollResult.toString(), Toast.LENGTH_LONG).show();
	}
}
