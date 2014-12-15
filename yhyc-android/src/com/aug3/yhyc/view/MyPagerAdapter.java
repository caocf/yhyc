package com.aug3.yhyc.view;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> fragmentsList;

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragmentsList) {
		super(fm);
		this.fragmentsList = fragmentsList;
	}

	@Override
	public Fragment getItem(int i) {

		return fragmentsList.get(i);
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}

}