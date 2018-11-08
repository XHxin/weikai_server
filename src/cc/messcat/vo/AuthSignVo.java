package cc.messcat.vo;

import java.util.List;

public class AuthSignVo {

	/*
	 * 认证要求
	 */
	private String title;  //文字
	private List<String> image;  //图片

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}
}
