/**
 * 
 */
package it.gecko.android.widgets;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author kLeZ-hAcK
 */
public class ExpandAnimation extends Animation
{
	@SuppressWarnings("unused")
	private static final String TAG = "it.gecko.android.widgets.ExpandAnimation[%s]";

	private final View _view;
	private final int _startHeight;
	private final int _finishHeight;
	public final int DefaultDuration = 220;

	public ExpandAnimation(View view, int startHeight, int finishHeight)
	{
		_view = view;
		_startHeight = startHeight;
		_finishHeight = finishHeight;
		setDuration(DefaultDuration);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t)
	{
		final int newHeight = (int) (((_finishHeight - _startHeight) * interpolatedTime) + _startHeight);
		_view.getLayoutParams().height = newHeight;
		_view.requestLayout();
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight)
	{
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds()
	{
		return true;
	}

	public static int measureViewHeight(View view2Expand, View view2Measure)
	{
		try
		{
			Method m = view2Measure.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
			m.setAccessible(true);
			m.invoke(view2Measure, MeasureSpec.makeMeasureSpec(view2Expand.getWidth(), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		}
		catch (Exception e)
		{
			return -1;
		}

		int measuredHeight = view2Measure.getMeasuredHeight();
		return measuredHeight;
	}

	public static void expandOrCollapse(View view2Expand, View view2Measure, int collapsedHeight)
	{
		//		Log.d(String.format(TAG, "expandOrCollapse"), "Start Of Method; Caller View: " + view2Expand.getId());
		if (view2Expand.getHeight() < collapsedHeight) { return; }

		int measuredHeight = measureViewHeight(view2Expand, view2Measure);

		//		Log.d(String.format(TAG, "expandOrCollapse"), "measuredHeight: " + measuredHeight);

		if (measuredHeight < collapsedHeight)
		{
			measuredHeight = collapsedHeight;
		}

		final int startHeight = view2Expand.getHeight();
		final int finishHeight = startHeight <= collapsedHeight ? measuredHeight : collapsedHeight;

		//		String msg = "startHeight: " + startHeight + "; finishHeight: " + finishHeight + "; measuredHeight: " + measuredHeight + "; collapsedHeight: " + collapsedHeight;
		//		Log.d(String.format(TAG, "expandOrCollapse"), msg);

		view2Expand.startAnimation(new ExpandAnimation(view2Expand, startHeight, finishHeight));
		//		Log.d(String.format(TAG, "expandOrCollapse"), "After startAnimation method");
	}

	public static void expandOrCollapse(View view2Expand, int expandedHeight, int collapsedHeight)
	{
		//		Log.d(String.format(TAG, "expandOrCollapse"), "Start Of Method; Caller View: " + view2Expand.getId());
		if (view2Expand.getHeight() < collapsedHeight) { return; }

		if (expandedHeight < collapsedHeight)
		{
			expandedHeight = collapsedHeight;
		}

		final int startHeight = view2Expand.getHeight();
		final int finishHeight = startHeight <= collapsedHeight ? expandedHeight : collapsedHeight;

		//		String msg = "startHeight: " + startHeight + "; finishHeight: " + finishHeight + "; expandedHeight: " + expandedHeight + "; collapsedHeight: " + collapsedHeight;
		//		Log.d(String.format(TAG, "expandOrCollapse"), msg);

		view2Expand.startAnimation(new ExpandAnimation(view2Expand, startHeight, finishHeight));
		//		Log.d(String.format(TAG, "expandOrCollapse"), "After startAnimation method");
	}
}
