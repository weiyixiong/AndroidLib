package com.wyx.androidlib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopIndicatorBar extends LinearLayout {

	private TextView[] titlesText;
	private final int paddingTop = 20;
	private final int paddingBottom = 20;
	private final int paddingLeft = 25;
	private final int paddingRight = 25;
	private final int textSize = 15;
	private int currentIndex;

	private IndicatorBarPagerScrollListener indicatorBarPagerScrollListener = null;
	private TopIndicatorTabChangeListener tabChangeListenerlistener = null;
	private ViewPager viewPager;

	public TopIndicatorBar(Context context) {
		this(context, null);
	}

	public TopIndicatorBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopIndicatorBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setDrawingCacheEnabled(true);

		middleLine = new Path();
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(8);
		setGravity(Gravity.CENTER_VERTICAL);
	}

	public void init(String[] titlesStr, ViewPager viewPager, HorizontalScrollView parent) {
		// destoryDrawCache();
		removeAllViews();
		titlesText = new TextView[titlesStr.length];
		this.viewPager = viewPager;
		for (int i = 0; i < titlesStr.length; i++) {
			TextView title = produceTextView(titlesStr, i);
			initOnClickListener(this.titlesText[i], viewPager, i);
			initOnScrollListener(viewPager, parent);
			addView(title);
		}

	}

	public void initWithEqualStyle(String[] titlesStr, ViewPager viewPager) {
		this.viewPager = viewPager;
		// destoryDrawCache();
		removeAllViews();
		titlesText = new TextView[titlesStr.length];
		for (int i = 0; i < titlesStr.length; i++) {
			TextView title = produceTextView(titlesStr, i);
			initOnClickListener(this.titlesText[i], viewPager, i);
			initOnScrollListener(viewPager, null);
			title.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
			addView(title);
		}

	}

	public void initWithEqualStyle(String[] titlesStr, TopIndicatorTabChangeListener listener) {
		this.tabChangeListenerlistener = listener;
		// destoryDrawCache();
		removeAllViews();
		titlesText = new TextView[titlesStr.length];
		for (int i = 0; i < titlesStr.length; i++) {
			TextView title = produceTextView(titlesStr, i);
			initOnClickLitener(this.titlesText[i], i);
			title.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
			addView(title);
		}
	}

	// public void destoryDrawCache() {
	// if (black != null) {
	// black.recycle();
	// black = null;
	// redbitmap.recycle();
	// redbitmap = null;
	// blackBitmap.recycle();
	// blackBitmap = null;
	// destroyDrawingCache();
	// }
	// }

	private TextView produceTextView(String[] titlesStr, int i) {
		titlesText[i] = new TextView(getContext());
		TextView title = titlesText[i];
		title.setText(titlesStr[i]);
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		title.setTextColor(Color.BLACK);
		title.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		title.setDrawingCacheEnabled(true);
		title.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		title.layout(0, 0, title.getMeasuredWidth(), title.getMeasuredHeight());
		title.setGravity(Gravity.CENTER);
		return title;
	}

	private void initOnClickLitener(final TextView titlesText2, final int selected) {
		titlesText2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateColorOnClick(selected);
			}
		});
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	private void initOnClickListener(final TextView titlesText2, final ViewPager viewPager, final int i) {
		titlesText2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateColorOnClick(i);
				viewPager.setCurrentItem(i, false);
			}
		});
	}

	public IndicatorBarPagerScrollListener getIndicatorBarPagerScrollListener() {
		return indicatorBarPagerScrollListener;
	}

	public void setIndicatorBarPagerScrollListener(IndicatorBarPagerScrollListener indicatorBarPagerScrollListener) {
		this.indicatorBarPagerScrollListener = indicatorBarPagerScrollListener;
	}

	private void initOnScrollListener(final ViewPager viewPager, final HorizontalScrollView parent) {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (indicatorBarPagerScrollListener != null) {
					indicatorBarPagerScrollListener.onPageSelected(arg0);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				updateColorOnScroll(arg0, arg1);
				if (parent != null) {
					int leftBorder = getLeftoffset(arg0, arg1);
					int rightBorder = getRightoffset(arg0, arg1);

					if (rightBorder > parent.getWidth() + parent.getScrollX()) {
						parent.scrollTo(rightBorder - parent.getWidth(), 0);
					}
					if (leftBorder < parent.getScrollX()) {
						parent.scrollTo(leftBorder, 0);
					}
				}
				if (indicatorBarPagerScrollListener != null) {
					indicatorBarPagerScrollListener.onPageScrolled(arg0, arg1, arg2);
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (indicatorBarPagerScrollListener != null) {
					indicatorBarPagerScrollListener.onPageScrollStateChanged(arg0);
				}
			}
		});
	}

	private void updateNextColor(int index, float offsetx) {
		TextView tmp = this.titlesText[index];
		float width = offsetx * tmp.getWidth();
		this.offsetX = offsetx;
		this.shaderWidth += width;
	}

	private void updateCurrentColor(int index, float offsetx) {
		TextView tmp = this.titlesText[index];
		float width = offsetx * tmp.getWidth();
		this.offsetX = offsetx;
		this.shaderWidth = tmp.getWidth() - (int) width;
		this.shaderLeft = tmp.getLeft() + (int) width;
		this.shaderHeight = getHeight();
	}

	private void changeRedOnClick(int currentIndex) {
		TextView tmp = this.titlesText[currentIndex];
		this.shaderWidth = tmp.getWidth();
		this.shaderLeft = tmp.getLeft();
		this.shaderHeight = getMeasuredHeight();
	}

	public void updateColorOnClick(int currentIndex) {
		if (tabChangeListenerlistener != null) {
			tabChangeListenerlistener.onViewChange(currentIndex);
		}
		changeRedOnClick(currentIndex);
		this.currentIndex = currentIndex;
		invalidate();
	}

	public void updateColorOnScroll(int currentIndex, float offsetx) {
		updateCurrentColor(currentIndex, offsetx);
		if (currentIndex + 1 < this.titlesText.length) {
			updateNextColor(currentIndex + 1, offsetx);
		}
		this.currentIndex = currentIndex;
		invalidate();

	}

	public int getLeftoffset(int index, float offsetx) {
		float width = offsetx * this.titlesText[index].getWidth();
		return (int) (this.titlesText[index].getLeft() + width);
	}

	public int getRightoffset(int index, float offsetx) {
		if (index + 1 > this.titlesText.length - 1) {
			index--;
		}
		float width = offsetx * this.titlesText[index + 1].getWidth();
		return (int) (this.titlesText[index + 1].getLeft() + width);
	}

	private final Paint paint;
	private final Path middleLine;
	private float offsetX = 0;

	private void makePath() {
		int start = this.titlesText[currentIndex].getLeft();
		int currentWidth = this.titlesText[currentIndex].getWidth();
		int currentLeft = this.titlesText[currentIndex].getLeft();
		middleLine.reset();
		middleLine.moveTo(start + offsetX * currentWidth, getHeight());
		if (currentIndex < this.titlesText.length - 1) {
			int nextWidth = this.titlesText[currentIndex + 1].getWidth();
			int nextLeft = this.titlesText[currentIndex + 1].getLeft();
			middleLine.lineTo(start + currentWidth + offsetX * (nextLeft + nextWidth - currentLeft - currentWidth), getHeight());
		} else {
			middleLine.lineTo(start + currentWidth, getHeight());
		}

	}

	private Bitmap getRedBitmap(int width, int height) {
		if (height == 0) {
			height = 1;
		}
		if (width == 0) {
			width = 1;
		}
		Bitmap red = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas2 = new Canvas(red);
		canvas2.drawColor(Color.RED);
		return red;
	}

	Bitmap black = null;
	Bitmap redbitmap = null;
	Bitmap blackBitmap = null;
	Paint srcInPaint = null;
	private int shaderWidth;
	private int shaderHeight;
	private int shaderLeft;

	private int init = 0;

	private int initIndex = 0;

	public void setInitColorIndex(int initColorIndex) {
		this.initIndex = initColorIndex;
		init = 2;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (this.titlesText != null) {
			makePath();
			canvas.drawPath(middleLine, paint);
		}
		if (shaderLeft == 0 && titlesText != null && init > 1) {
			currentIndex = initIndex;
			updateCurrentColor(currentIndex, 0);
			init--;
			if (viewPager != null) {
				viewPager.setCurrentItem(currentIndex, false);
			}
		}
		super.dispatchDraw(canvas);
	}

	// private void drawRedText(Canvas canvas, int left, int width, int height)
	// {
	// if (redbitmap == null) {
	// redbitmap = getRedBitmap(black.getWidth(), black.getHeight());
	// }
	// Rect rect = new Rect(left, 0, left + width, height);
	// Canvas canvas2 = new Canvas(blackBitmap);
	// srcInPaint.setXfermode(new
	// PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
	// canvas2.drawBitmap(redbitmap, rect, rect, srcInPaint);
	// srcInPaint.setXfermode(new
	// PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_OVER));
	// canvas.drawBitmap(blackBitmap, rect, rect, srcInPaint);
	// }

	public interface IndicatorBarPagerScrollListener {
		public void onPageSelected(int arg0);

		public void onPageScrolled(int arg0, float arg1, int arg2);

		public void onPageScrollStateChanged(int arg0);
	}

	public interface TopIndicatorTabChangeListener {
		void onViewChange(int selected);
	}

}
