package cc.modules.util;

import java.text.SimpleDateFormat;
import cc.messcat.entity.EnterpriseNews;
import cc.messcat.vo.EnterpriseNewsVo;

public class NewsUtil {
	
	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认会员头像
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接
	private static String webDomain = PropertiesFileReader.getByKey("web.domain");// 项目域名
	
	public static EnterpriseNewsVo setEnterpriseNewsVoInfo(EnterpriseNews en, boolean isCollect){
		EnterpriseNewsVo env = new EnterpriseNewsVo();
		if(isCollect) {
			env.setStatus(1);
		}else{
			env.setStatus(0);
		}
		env.setId(en.getId());
		env.setTitle(en.getTitle()==null?"":en.getTitle());
		if(en.getEnterpriseColumn().getFrontNum().equals("app_advan") || en.getEnterpriseColumn().getFrontNum().equals("server_advan")){
			env.setContents(en.getContents());
		}else{
			env.setContents("");
		}
		if(ObjValid.isValid(en.getEditeTime())){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time = sdf.format(en.getEditeTime());
			env.setEditeTime(time);
		}else{
			env.setEditeTime("");
		}
		if(ObjValid.isValid(en.getPhoto())){
			env.setPhoto(jointUrl+en.getPhoto());
		}else{
			env.setPhoto(defaultPhoto);
		}
		if(en.getEnterpriseColumn().getFrontNum().equals("app_feedback") || en.getEnterpriseColumn().getFrontNum().equals("app_cooperate") || en.getEnterpriseColumn().getFrontNum().equals("app_service")){
			env.setPriKey(en.getPriKey());
		}else{
			env.setPriKey("");
		}
		if(en.getVideoId() != null) {
			env.setVideoId(en.getVideoId());
		}else {
			env.setVideoId(-1L);
		}
		env.setShortMeta(en.getShortMeta()==null?"":en.getShortMeta());
		env.setUrl(webDomain.replace("www","admin")+"/epFrontNewsAction!news.action?selectNewsId="+en.getId()+"&shareUrl="+en.getShareUrl());
		return env;
	}
}
