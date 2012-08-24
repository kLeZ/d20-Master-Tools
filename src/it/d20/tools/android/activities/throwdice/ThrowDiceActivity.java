package it.d20.tools.android.activities.throwdice;

import it.d20.tools.android.R;
import it.gecko.android.utils.DiceExpressionKeyListener;
import it.gecko.utils.Dice;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ThrowDiceActivity extends Activity
{
	private static final String TAG = "it.d20.tools.android.activities.throwdice.ThrowDiceActivity";
	private TextView tvResults;
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.throwdice_layout);
		et = (EditText) findViewById(R.id.diceExpressionEdit);
		tvResults = (TextView) findViewById(R.id.throwdice_tvResults);
		et.setKeyListener(new DiceExpressionKeyListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.throwdice_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.throwdice_menu_throw:
				onThrowDice();
				return true;
			case R.id.throwdice_menu_evaluateSingle:
				onEvaluateSingle();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onThrowDice()
	{
		try
		{
			Integer rollResult = 0;
			if (tvResults.getText().length() >= 3)
			{
				rollResult = Dice.rollSum(Dice.parseMany(tvResults.getText().toString()));
			}
			Toast.makeText(this, getText(R.string.throwdice_rolled_show_text) + rollResult.toString(), Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Log.w(TAG, "Not a dice expression!");
			Toast.makeText(this, getText(R.string.throwdice_nade), Toast.LENGTH_LONG).show();
		}
	}

	public void onEvaluateSingle()
	{
		Dice d = Dice.parse(et.getText().toString());
		tvResults.append(d.toEnclosedString());
	}
}
