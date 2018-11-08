package com.mipush;

import java.util.HashMap;

import com.google.gson.Gson;

public class ExtraFactory {

	public static boolean TARGET_DEVICE_IOS;

	public enum Target {
		ANDROID, IOS
	}

	/**
	 * ACT 前缀属于Android页面
	 */
	private static final String TARGET_ACT_WEB = ".view.webview.WebViewActivity";
	private static final String TARGET_ACT_EXPERT = ".module.find.activity.ExpertInfoProfileActivity";

	/**
	 * COT 前缀属于iOS页面
	 */
	private static final String TARGET_COT_WEB = "";
	private static final String TARGET_COT_EXPERT = "";

	public static void main(String[] args) {
		String ret = openWeb(Target.ANDROID, "http://www.baidu.com");
		System.out.println(ret);
	}

	/**
	 * 打开APP中的网页
	 * 
	 */
	public static String openWeb(Target target, String url) {
		HashMap<String, String> map = new HashMap<>();
		map.put("_target", target == Target.ANDROID ? TARGET_ACT_WEB : TARGET_COT_WEB);
		map.put("url", url);
		return new Gson().toJson(map);
	}

	/**
	 * 打开专家详情
	 * 
	 */
	public static String openExpert(Target target, String expertId) {
		HashMap<String, String> map = new HashMap<>();
		map.put("_target", target == Target.ANDROID ? TARGET_ACT_EXPERT : TARGET_COT_EXPERT);
		map.put("expertId", expertId);
		return new Gson().toJson(map);
	}
}
