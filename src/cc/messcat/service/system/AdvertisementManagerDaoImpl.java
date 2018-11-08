package cc.messcat.service.system;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Advertisement;
import cc.messcat.entity.LiveVideo;
import cc.messcat.vo.AdvertisementVo;
import cc.modules.util.PropertiesFileReader;

@SuppressWarnings("serial")
public class AdvertisementManagerDaoImpl extends BaseManagerDaoImpl implements AdvertisementManagerDao{

	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 上传图片路径
	
	@Override
	public AdvertisementVo getAdvertisement(int type) {
		Advertisement entity=advertisementDao.getAdvertisement(type);
		AdvertisementVo vo=new AdvertisementVo();
		if(entity!=null) {
			vo.setCopyRight(entity.getCopyRight());
			vo.setId(entity.getId());
			vo.setPhoto(jointUrl+entity.getPhoto());
			vo.setStatus(entity.getStatus());
			vo.setTitle(entity.getTitle());
			vo.setType(entity.getType());
			String str=entity.getUri();
			/**
			 * 直播转录播时， uri自动把直播(live) 转为 课程(course)
			 */
			if(str.contains("live")==true) {  //判断uri中是否含有live
				if(str.contains("&")){
					String [] strArray=str.split("&"); //把字符串按照 & 去拆分
					for(String s:strArray){
						if(s.contains("id")){  //找到含有id的字符串片段
							LiveVideo video=liveDao.getLiveVideoById(Long.valueOf(s.replace("id=","")));  //截取出id值
							if(video!=null){
								if(!"0".equals(video.getVideoType())) {
									vo.setUri(str.replace("live", "course"));
									vo.setUrl(entity.getUrl().replace("live", "course"));
								}else {
									vo.setUri(str);
									vo.setUrl(entity.getUrl());
								}
							}
						}
					}
				}
			}else {
				vo.setUri(str);
				vo.setUrl(entity.getUrl());
			}
		}else {
			return null;
		}
		return vo;
	}

}
