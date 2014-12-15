package com.aug3.yhyc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.aug3.yhyc.R;

public class CommentTopView extends FrameLayout {

	public CommentTopView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.comment_top_view, this);

	}

}