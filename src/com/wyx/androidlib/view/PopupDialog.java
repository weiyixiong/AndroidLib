package com.wyx.androidlib.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyx.androidlib.R;
import com.wyx.androidlib.R.drawable;
import com.wyx.androidlib.R.id;
import com.wyx.androidlib.R.layout;
import com.wyx.androidlib.R.style;

public class PopupDialog extends Dialog {
	private final View popupView;
	private final Activity activity;

	public static final int ENSUREDIALOG = 110;
	public static final int COMMONDIALOG = 111;
	private TextView dialogTitle;
	private Button negative;
	private Button positive;

	private DialogActionListener nagativeClick;
	private DialogActionListener positionClick;
	private DialogActionListener outsideClick;
	private LinearLayout container;
	private boolean isDismiss = true;

	public PopupDialog(Activity activity, int layoutId) {
		super(activity, R.style.emptyDialog);
		this.activity = activity;
		popupView = View.inflate(activity, layoutId, null);
		setContentView(popupView);
	}

	public PopupDialog(Activity activity, View contentView) {
		super(activity, R.style.emptyDialog);
		this.activity = activity;
		this.popupView = contentView;
		setContentView(popupView);
	}

	public PopupDialog(Activity activity, int title, int leftButtonText, int rightButtonText) {
		this(activity, activity.getString(title), activity.getString(leftButtonText), activity.getString(rightButtonText));
	}

	public PopupDialog(Activity activity, String title, String leftButtonText, String rightButtonText) {
		super(activity, R.style.emptyDialog);
		this.activity = activity;
		super.getWindow().setWindowAnimations(R.style.popup_animation);
		popupView = View.inflate(activity, R.layout.popup_dialog, null);
		initView();
		setTitle(title);
		setButtonText(leftButtonText, rightButtonText);
		initclickListener();
		setContentView(popupView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Rect dialogBounds = new Rect();
		getWindow().getDecorView().getHitRect(dialogBounds);
		if (!dialogBounds.contains((int) event.getX(), (int) event.getY()) && outsideClick != null) {
			outsideClick.onClick();
		}
		return super.onTouchEvent(event);
	}

	public void setStyle(int style) {
		switch (style) {
		case ENSUREDIALOG:
			positive.setVisibility(View.GONE);
			popupView.findViewById(R.id.button_divider).setVisibility(View.GONE);
			negative.setBackgroundResource(R.drawable.popup_bottom_button);
			break;
		case COMMONDIALOG:
			positive.setVisibility(View.VISIBLE);
			popupView.findViewById(R.id.button_divider).setVisibility(View.VISIBLE);
			negative.setBackgroundResource(R.drawable.popup_dialog_right);
			break;

		default:
			break;
		}

	}

	public void setButtonText(String leftButtonText, String rightButtonText) {
		negative.setText(leftButtonText);
		positive.setText(rightButtonText);
	}

	public void showAtTop(int x, int y) {
		showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, x, y);
	}

	public void showAtCenter(int x, int y) {
		showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, x, y);
	}

	public void showAtBottom(int x, int y) {
		showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, x, y);
	}

	public void addContainView(View view) {
		container.addView(view);

	}

	public DialogActionListener getNagativeClick() {
		return nagativeClick;
	}

	public void setNagativeClick(DialogActionListener nagativeClick) {
		this.nagativeClick = nagativeClick;
	}

	public DialogActionListener getPositionClick() {
		return positionClick;
	}

	public void setPositionClick(DialogActionListener positionClick) {
		this.positionClick = positionClick;
	}

	public void setTitle(String title) {
		dialogTitle.setHint(title);
	}

	private void initView() {
		dialogTitle = (TextView) popupView.findViewById(R.id.dialog_title);
		negative = (Button) popupView.findViewById(R.id.dialog_nagative);
		positive = (Button) popupView.findViewById(R.id.dialog_positive);
		container = (LinearLayout) popupView.findViewById(R.id.contain);

	}

	private void initclickListener() {

		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (nagativeClick != null) {
					isDismiss = nagativeClick.onClick();
				}
				if (isDismiss) {
					dismiss();
				}
			}
		});
		positive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (positionClick != null) {
					isDismiss = positionClick.onClick();
				}
				if (isDismiss) {
					dismiss();
				}
			}
		});

	}

	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.show();
	}

	public DialogActionListener getOutsideClick() {
		return outsideClick;
	}

	public void setOutsideClick(DialogActionListener outsideClick) {
		this.outsideClick = outsideClick;
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	public interface DialogActionListener {
		boolean onClick();
	}

}
