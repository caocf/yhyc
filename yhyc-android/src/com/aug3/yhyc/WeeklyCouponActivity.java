package com.aug3.yhyc;

import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aug3.yhyc.view.ActionBarView;

public class WeeklyCouponActivity extends BaseActivity {

	private ActionBarView ab;

	private TableLayout weeklyTable;

	private Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weekly_coupon);

		ab = (ActionBarView) findViewById(R.id.actionbar_back);
		ab.setTitleText(R.string.title_health_vegetable);

		resources = getResources();

		initWeeklyCoupon();

	}

	private void initWeeklyCoupon() {

		weeklyTable = (TableLayout) findViewById(R.id.weekly_coupon_table);

		// ImageLoadingListener animateFirstListener = new
		// AnimateFirstDisplayListener();
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(10, 0, 5, 0);

		int iv_size = (int) resources.getDimension(R.dimen.view_size_50dp);
		TableRow.LayoutParams ivLayoutParam = new TableRow.LayoutParams(
				iv_size, iv_size);

		for (int w = 1; w <= 5; w++) {
			TableRow tr = new TableRow(this);

			TextView wk = new TextView(this);
			wk.setText("ÖÜ" + w);
			wk.setTextSize(resources.getDimension(R.dimen.tv_size_normal));
			wk.setLayoutParams(layoutParams);
			tr.addView(wk);

			LinearLayout imgLayout = new LinearLayout(this);
			imgLayout.setOrientation(LinearLayout.HORIZONTAL);
			imgLayout.setLayoutParams(layoutParams);

			for (int i = 0; i < 4; i++) {
				ImageView iv = new ImageView(this);
				iv.setImageResource(R.drawable.category_fruit);
				// imageLoader.displayImage(i.getUrl(), holder.image, options,
				// animateFirstListener);
				iv.setLayoutParams(ivLayoutParam);
				iv.setPadding(1, 2, 1, 2);
				// R.dimen.view_size_40dp, R.dimen.view_size_40dp));
				imgLayout.addView(iv);
			}
			tr.addView(imgLayout);

			LinearLayout price = new LinearLayout(this);
			price.setOrientation(LinearLayout.VERTICAL);
			price.setLayoutParams(layoutParams);

			TextView tv_pp = new TextView(this);
			tv_pp.setText("£¤22.0");
			tv_pp.setTextColor(resources.getColor(R.color.red));
			TextView tv_mp = new TextView(this);
			tv_mp.setText("£¤32.0");
			tv_mp.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			price.addView(tv_pp);
			price.addView(tv_mp);

			tr.addView(price);

			Button b = new Button(this);
			b.setText(R.string.btn_goto_pay);
			tr.addView(b);

			weeklyTable.addView(tr);
		}

	}
}
