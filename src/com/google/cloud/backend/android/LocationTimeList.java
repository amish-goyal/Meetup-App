package com.google.cloud.backend.android;

import java.util.ArrayList;

public class LocationTimeList {
	private ArrayList<String> locTiList;
	
	public LocationTimeList(ArrayList<String> list) {
		locTiList = list;
	}
	
	public LocationTimeList() {
		locTiList = new ArrayList<String>();
	}
	
	public boolean add(String locTi) {
		return locTiList.add(locTi);
	}
	
	public boolean contains(String locTi) {
		return locTiList.contains(locTi);
	}
	
	public String[] toArray() {
		String[] arr = new String[locTiList.size()];
		String[] str = locTiList.toArray(arr);
		return str;
	}
}
