package com.wyx.androidlib.utils;

import java.lang.reflect.Field;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ListAdapter;

public class GridViewUtil {

	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
		// 获取GridView对应的Adapter
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int rows;
		int columns = 0;
		int horizontalBorderHeight = 0;
		Class<?> clazz = gridView.getClass();
		try {
			// 利用反射，取得每行显示的个数
			Field column = clazz.getDeclaredField("mRequestedNumColumns");
			column.setAccessible(true);
			columns = (Integer) column.get(gridView);
			// 利用反射，取得横向分割线高度
			Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
			horizontalSpacing.setAccessible(true);
			horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加�?�?
		if (listAdapter.getCount() % columns > 0) {
			rows = listAdapter.getCount() / columns + 1;
		} else {
			rows = listAdapter.getCount() / columns;
		}
		int totalHeight = 0;
		for (int i = 0; i < rows; i++) { // 只计算每项高�?*行数
			View listItem = listAdapter.getView(i, null, gridView);
			LayoutParams layoutParams = listItem.getLayoutParams();
			if (layoutParams == null) {
				listItem.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0); // 计算子项View 的宽�?
			totalHeight += listItem.getMeasuredHeight() + listItem.getPaddingTop() + listItem.getPaddingBottom(); // 统计�?有子项的总高�?
		}
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = totalHeight + horizontalBorderHeight * (rows - 1);// �?后加上分割线总高�?
		gridView.setLayoutParams(params);
	}

}
