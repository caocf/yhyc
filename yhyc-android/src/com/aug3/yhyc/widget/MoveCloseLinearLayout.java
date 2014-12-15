package com.aug3.yhyc.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MoveCloseLinearLayout extends RelativeLayout {

	View leftView = null;
	View rightView = null;

	public MoveCloseLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = new View(context, attrs);
		view.setBackgroundColor(Color.BLACK);
		this.addView(view, 0);
	}

	/**
	 * ����
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (getChildCount() != 2) {
			try {
				// �Զ���Exception
				throw new Exception() {
					@Override
					public void printStackTrace() {
						System.err.println("MyView��ֻ�ܴ���һ��View");
						super.printStackTrace();
					}

				};
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		leftView = getChildAt(0);
		// ����leftview�ĸߺͿ�
		leftView.measure(widthMeasureSpec, heightMeasureSpec);
		rightView = getChildAt(1);
		// ����rightview�ĸߺͿ�
		rightView.measure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * ����
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (leftView != null & rightView != null) {
			// ����leftview��λ��,����Ļ�Ҳ�(��ʼ���ɼ�)
			leftView.layout(-r, 0, 0, b);
			// ����rightView��λ��,����Ļ��
			rightView.layout(l, t, r, b);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final float X = event.getX();
		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE:
			if (X < 100) {
				scrollTo(0, 0);
			} else if (X > rightView.getMeasuredWidth() - 100) {// ���û����������ұ�Ե100ʱ,ҳ��ر�
				new Thread(new Runnable() {// �½��߳�,�����ر�
							@Override
							public void run() {
								for (int i = 0;; i++) {
									try {
										Thread.sleep(10);// rightViewÿ��10ms����3,��֤��������
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									int len = (int) (X + 3 * i);
									Message message = new Message();// android�з�UI�̲߳�����ֱ�Ӳ����ؼ�,���Խ���Ϣ���������̵߳�handler��
									if (len >= rightView.getMeasuredWidth()) {
										message.what = 1;
										handler.sendMessage(message);// ������Ϣ
																		// �ر�View
										break;
									} else {
										message.what = 0; // ������Ϣ �Զ�����
										handler.sendMessage(message);
									}
								}
							}
						}).start();
			} else {
				scrollTo((int) -X, 0);
				// ����͸������Ϣ
				float alpha = (float) (1.0 - (float) (1.0 / 400) * X);
				// ����͸����
				leftView.setAlpha(alpha);
			}
			break;
		}
		// ����true,����event�¼�,�������⴫��
		return true;
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				scrollBy(-3, 0);
			} else if (msg.what == 1) {
				Toast.makeText(getContext(), "�ر�", 50).show();
				setVisibility(View.GONE);
			}
		}

	};

}
