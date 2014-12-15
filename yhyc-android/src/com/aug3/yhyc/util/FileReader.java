package com.aug3.yhyc.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class FileReader {

	Context context;

	public FileReader(Context context) {
		this.context = context;
	}

	public List<String[]> loadCommaSeparated(int resId) {

		List<String[]> list = new ArrayList<String[]>();

		BufferedReader bufferedReader = null;
		try {
			String line;
			InputStream in = context.getResources().openRawResource(resId);
			bufferedReader = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			while ((line = bufferedReader.readLine()) != null) {
				String[] parts = line.trim().split(",");
				list.add(parts);
				line = null;
			}
		} catch (Exception e) {

		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {

				}
			}
		}

		return list;

	}

}
