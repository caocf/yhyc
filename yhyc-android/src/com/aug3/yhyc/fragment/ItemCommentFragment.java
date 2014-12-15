package com.aug3.yhyc.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aug3.yhyc.R;
import com.aug3.yhyc.api.Comment;
import com.aug3.yhyc.api.CommentDTO;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.util.JSONUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ItemCommentFragment extends Fragment {

	private CommentDTO comment;

	private List<Comment> commentlist;

	private CommentListAdapter listAdapter;

	private long itemid;

	private TextView tv_goodrating, tv_total, tv_good, tv_middle, tv_bad;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_item_comment,
				container, false);

		initCommentView(rootView);

		return rootView;
	}

	private void initCommentView(View v) {

		tv_goodrating = (TextView) v.findViewById(R.id.good_score_rating_tv);
		tv_total = (TextView) v.findViewById(R.id.all_comment_count_tv);
		tv_good = (TextView) v.findViewById(R.id.good_comment_count_tv);
		tv_middle = (TextView) v.findViewById(R.id.midlle_comment_count_tv);
		tv_bad = (TextView) v.findViewById(R.id.bad_comment_count_tv);

		commentlist = new ArrayList<Comment>();
		listAdapter = new CommentListAdapter(v.getContext(), commentlist);
		((ListView) v.findViewById(R.id.comment_list)).setAdapter(listAdapter);

		getCommentData();

	}

	private void getCommentData() {

		// add cache for 10mins
		Bundle bundle = getActivity().getIntent().getExtras();
		itemid = bundle.getLong(Constants.Extra.ITEMID);

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.ITEM, itemid);
		p.put(Constants.ReqParam.PAGE, 1);

		YhycRestClient.get(Constants.ReqUrl.ITEM_COMMENT, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {
							JSONObject commentJSON = (JSONObject) response
									.get(Constants.RESP_RESULT);
							comment = JSONUtil.fromJson(commentJSON.toString(),
									CommentDTO.class);

							commentlist.clear();
							commentlist.addAll(comment.getComments());

							listAdapter.notifyDataSetChanged();

							if (comment.getCount() != 0) {
								tv_total.setText(String.valueOf(comment
										.getCount()));
								tv_good.setText(String.valueOf(comment
										.getGood()));
								tv_middle.setText(String.valueOf(comment
										.getNorm()));
								tv_bad.setText(String.valueOf(comment.getBad()));
								float rating = comment.getGood()
										/ comment.getCount();
								tv_goodrating.setText(Double.toString(Math
										.round(rating * 10000) / 100) + "%");
							}

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
					}
				});

	}

	class ViewHolder {

		public RatingBar ratingbar;
		public TextView tv_comment;
		public TextView tv_name;
		public TextView tv_time;

	}

	class CommentListAdapter extends BaseAdapter {

		private List<Comment> listComments;
		private Context context;

		public CommentListAdapter(Context ctx, List<Comment> listComments) {
			this.listComments = listComments;
			this.context = ctx;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View vi = convertView;
			ViewHolder holder;
			if (convertView == null) {
				vi = LayoutInflater.from(context).inflate(
						R.layout.list_item_comment, null);
				holder = new ViewHolder();
				holder.ratingbar = (RatingBar) vi
						.findViewById(R.id.comment_item_rating);
				holder.tv_comment = (TextView) vi
						.findViewById(R.id.comment_detail_item_tv);
				holder.tv_name = (TextView) vi
						.findViewById(R.id.comment_name_item_tv);
				holder.tv_time = (TextView) vi
						.findViewById(R.id.comment_date_item_tv);

				vi.setTag(holder);
			} else {
				holder = (ViewHolder) vi.getTag();
			}

			Comment comment = listComments.get(position);
			holder.ratingbar.setRating(comment.getScore());
			holder.tv_comment.setText(comment.getContent());
			if (TextUtils.isEmpty(comment.getContent())) {
				if (comment.getScore() > 4)
					holder.tv_comment.setText(R.string.label_comment_star_5);
				if (comment.getScore() == 4)
					holder.tv_comment.setText(R.string.label_comment_star_4);
			}
			holder.tv_name.setText(comment.getName());
			holder.tv_time.setText(comment.getTs());

			return vi;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return listComments.get(position);
		}

		@Override
		public int getCount() {
			return listComments.size();
		}

	}

}
