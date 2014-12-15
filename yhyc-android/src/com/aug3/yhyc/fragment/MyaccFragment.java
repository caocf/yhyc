package com.aug3.yhyc.fragment;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aug3.yhyc.AboutusActivity;
import com.aug3.yhyc.JoinusActivity;
import com.aug3.yhyc.MyFavoriateActivity;
import com.aug3.yhyc.MyOrdersActivity;
import com.aug3.yhyc.MessageListActivity;
import com.aug3.yhyc.R;
import com.aug3.yhyc.SettingsActivity;
import com.aug3.yhyc.WebStoreActivity;
import com.aug3.yhyc.base.Constants;
import com.aug3.yhyc.http.ServiceAgent;
import com.aug3.yhyc.http.YhycRestClient;
import com.aug3.yhyc.model.ListItemSimple;
import com.aug3.yhyc.model.UserPreference;
import com.aug3.yhyc.util.Tools;
import com.aug3.yhyc.view.PicPickerDialog;
import com.aug3.yhyc.view.SimpleViewHolder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

@SuppressLint("NewApi")
public class MyaccFragment extends BaseFragment {

	public static TextView tv_username, user_ac;

	private View view;

	int[] itemTexts = { R.string.title_my_orders, R.string.title_myfavorite,
			R.string.title_web_store, R.string.label_aboutus,
			R.string.title_join_us };// R.string.title_upload_works,R.string.title_appeal_call
	// "地址管理",
	// "积分/抵用券",
	// 营养菜谱计划（菜谱日历、系统考评）

	int[] icons = { R.drawable.my_order, R.drawable.my_favorite,
			R.drawable.my_jfsc, R.drawable.my_about, R.drawable.my_jiameng };// R.drawable.my_uploads,

	private ListView listView = null;
	private ArrayList<ListItemSimple> listItemData = null;

	private ImageView avatarView;
	private File avatarFile;

	private long uid = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshUserInfo();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_myacc, container, false);

		uid = UserPreference.getInstance().getUid();

		initUserInfo();

		initList();

		if (uid == 0) {
			view.findViewById(R.id.myacc_msg).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.myacc_msg).setVisibility(View.VISIBLE);
			view.findViewById(R.id.myacc_msg_list)
					.setOnClickListener(viewMyMsg);
			initMsg();
		}

		return view;
	}

	private void initMsg() {

		RequestParams p = new RequestParams();
		p.put(Constants.ReqParam.USERID, uid);

		YhycRestClient.get(Constants.ReqUrl.USER_NOTIFY, p,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {

						try {

							JSONObject notifyObj = (JSONObject) response
									.get(Constants.RESP_RESULT);

							long msgNotRead = notifyObj.getLong("msg");
							TextView tv_msg_flag = (TextView) view
									.findViewById(R.id.myacc_msg_new);
							if (msgNotRead > 0) {
								tv_msg_flag.setVisibility(View.VISIBLE);
								tv_msg_flag.setText(String.valueOf(msgNotRead));
							} else {
								tv_msg_flag.setVisibility(View.GONE);
							}

						} catch (JSONException e) {
						}

					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						Tools.ShowAPIError(getActivity());
					}
				});
	}

	private View.OnClickListener viewMyMsg = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			gotoActivity(getActivity(), new Intent(v.getContext(),
					MessageListActivity.class));

		}
	};

	private void initUserInfo() {

		tv_username = (TextView) view.findViewById(R.id.myacc_username);
		user_ac = (TextView) view.findViewById(R.id.myacc_user_ac);

		view.findViewById(R.id.myacc_user_info_layout).setOnClickListener(
				gotoSettings);
		view.findViewById(R.id.myacc_setting_btn).setOnClickListener(
				gotoSettings);

	}

	private View.OnClickListener gotoSettings = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			gotoActivity(getActivity(), new Intent(v.getContext(),
					SettingsActivity.class));

		}
	};

	private void initList() {

		listView = (ListView) view.findViewById(R.id.listViewMy);

		listItemData = new ArrayList<ListItemSimple>();
		Resources resources = getResources();
		int size = itemTexts.length;
		for (int i = 0; i < size; i++) {
			ListItemSimple itemdata = new ListItemSimple(
					resources.getString(itemTexts[i]), icons[i]);
			listItemData.add(itemdata);
		}

		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(listItemClickListener);
	}

	private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

			doAction(v, pos);

		}

		private void doAction(View v, int pos) {
			Intent intent = null;
			switch (pos) {
			case 0:
				intent = new Intent(v.getContext(), MyOrdersActivity.class);
				break;
			case 1:
				intent = new Intent(v.getContext(), MyFavoriateActivity.class);
				break;
			case 2:
				intent = new Intent(v.getContext(), WebStoreActivity.class);
				break;
			case 3:
				intent = new Intent(v.getContext(), AboutusActivity.class);
				break;
			case 4:
				intent = new Intent(v.getContext(), JoinusActivity.class);
				break;
			default:
				return;

			}

			gotoActivity(getActivity(), intent);
		}
	};

	private void refreshUserInfo() {

		view.findViewById(R.id.myacc_user_info_layout).setVisibility(
				View.VISIBLE);
		tv_username.setText(UserPreference.getInstance().getUserName());

		int ac = UserPreference.getInstance().getAc();
		user_ac.setText(String.valueOf(ac));

		initMyAvatar();

	}

	private void initMyAvatar() {

		avatarFile = new File(Tools.getDirectoryDCIM(),
				Constants.SharedPrefs.fnAvatar.replace("{uid}",
						String.valueOf(UserPreference.getInstance().getUid())));

		avatarView = (ImageView) view.findViewById(R.id.myacc_avatar);
		avatarView.setOnClickListener(avatarListener);

		Bitmap myAvatar = null;
		if (Tools.hasSdcard()) {
			myAvatar = BitmapFactory.decodeFile(avatarFile.getPath(), null);

		}

		// if (myAvatar != null) {
		// myAvatar = Tools.toRoundBitmap(myAvatar);
		// }
		if (myAvatar == null && UserPreference.getInstance().isHasAvatar()) {
			// TODO: getAvatar from remote
		}

		if (myAvatar != null)
			avatarView.setImageBitmap(myAvatar);
	}

	private BaseAdapter listAdapter = new BaseAdapter() {

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return listItemData.get(position);
		}

		@Override
		public int getCount() {
			return listItemData.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View vi = convertView;
			SimpleViewHolder holder;
			if (convertView == null) {
				vi = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_normal, null);
				holder = new SimpleViewHolder();
				holder.image = (ImageView) vi.findViewById(R.id.listitem_img);
				holder.text = (TextView) vi.findViewById(R.id.listitem_title);

				vi.setTag(holder);
			} else {
				holder = (SimpleViewHolder) vi.getTag();
			}
			ListItemSimple itemdata = listItemData.get(position);
			holder.image.setImageResource(itemdata.image);
			holder.text.setText(itemdata.text);

			return vi;
		}
	};

	private View.OnClickListener avatarListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (UserPreference.getInstance().getUid() == 0) {
				Tools.ShowTip(getActivity(), R.string.label_not_login);
				return;
			}

			final PicPickerDialog ad = new PicPickerDialog(getActivity());

			ad.setTitle(R.string.label_choose_avatar);
			ad.setLoadPicListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ad.dismiss();

					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*"); // set file type
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intentFromGallery,
							Constants.IMAGE_REQUEST_CODE);

				}
			});

			ad.setCameraListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					ad.dismiss();

					Intent intentFromCapture = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					// test if storage card can be used
					if (Tools.hasSdcard()) {
						intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(avatarFile));
					}

					startActivityForResult(intentFromCapture,
							Constants.CAMERA_REQUEST_CODE);

				}
			});
		}
	};

	@SuppressLint("NewApi")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		getActivity();
		// 结果码不等于取消时候
		if (resultCode != Activity.RESULT_CANCELED) {

			switch (requestCode) {
			case Constants.IMAGE_REQUEST_CODE:
				startActivityForResult(Tools.getPhotoZoomIntent(data.getData(),
						1, 1, 150, 150), Constants.RESULT_REQUEST_CODE);
				break;
			case Constants.CAMERA_REQUEST_CODE:
				if (Tools.hasSdcard()) {
					startActivityForResult(Tools.getPhotoZoomIntent(
							Uri.fromFile(avatarFile), 1, 1, 150, 150),
							Constants.RESULT_REQUEST_CODE);
				} else {
					Tools.ShowLongTip(getActivity(),
							R.string.label_storage_not_found);
				}
				break;
			case Constants.RESULT_REQUEST_CODE:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap photo = extras.getParcelable("data");

						Tools.saveBitmap(avatarFile.getPath(), photo);

						// save to remote server
						ServiceAgent.uploadBitmap(avatarFile.getPath());
					}
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
