package it.d20.tools.android.activities.throwdice;

import it.d20.tools.android.R;
import it.gecko.android.utils.DiceExpressionKeyListener;
import it.gecko.utils.Dice;
import it.gecko.utils.OperatorType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;

public class ThrowDiceActivity extends Activity
{
	private static final String TAG = "it.d20.tools.android.activities.throwdice.ThrowDiceActivity";
	private TextView tvDiceExpression;
	private TextView tvResults;
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.throwdice_layout);
		et = (EditText) findViewById(R.id.diceExpressionEdit);
		tvDiceExpression = (TextView) findViewById(R.id.throwdice_tvDiceExpression);
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
			case R.id.throwdice_menu_clearTextView:
				onClear();
				return true;
			case R.id.throwdice_menu_resetDiceExpression:
				onResetDiceExpression();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onResetDiceExpression()
	{
		tvDiceExpression.setText("");
	}

	public void onClear()
	{
		tvResults.setText("");
	}

	public void onThrowDice()
	{
		try
		{
			String msg = getText(R.string.throwdice_rolled_show_text).toString();
			msg = msg.concat(System.getProperty("line.separator"));
			msg = msg.concat(Dice.rollShowResults(tvDiceExpression.getText().toString()));
			tvResults.append(System.getProperty("line.separator"));
			tvResults.append(msg);
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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
		tvDiceExpression.append(d.toEnclosedString());
		getOperatorChoiceDialog().show();
		et.setText("");
	}

	private ArrayAdapter<OperatorType> getAdapter()
	{
		int spinner_view = android.R.layout.simple_spinner_item;
		return new ArrayAdapter<OperatorType>(this, spinner_view, OperatorType.values());
	}

	private Dialog getOperatorChoiceDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final Spinner opCombo = new Spinner(this);
		opCombo.setAdapter(getAdapter());
		builder.setView(opCombo);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				OperatorType opType = (OperatorType) opCombo.getSelectedItem();
				if (opType != null)
				{
					tvDiceExpression.append(opType.toString());
					dialog.dismiss();
				}
				else
				{
					Context ctx = ((Dialog) dialog).getContext();
					CharSequence msg = ctx.getText(R.string.throwdice_choose_operator_msg);
					Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
				}
			}
		});
		return builder.create();
	}
}
