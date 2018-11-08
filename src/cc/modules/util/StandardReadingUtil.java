package cc.modules.util;

import java.util.ArrayList;
import java.util.List;

import com.baidubce.services.doc.model.GetDocumentResponse;

import cc.messcat.entity.BuysRecord;
import cc.messcat.entity.ExpenseTotal;
import cc.messcat.entity.Member;
import cc.messcat.entity.StandardReading;
import cc.messcat.vo.Adjunct;
import cc.messcat.vo.QualityShareListVo;

public class StandardReadingUtil {

	private static String defaultPhoto = PropertiesFileReader.getByKey("default.photo.url");// 默认会员头像
	private static String jointUrl = PropertiesFileReader.getByKey("joint.photo.url");// 图片拼接

	public static QualityShareListVo setStandardReadingVoInfo(StandardReading read, Member member, BuysRecord buys,
			String collect, String portType, String shareURL) {
		QualityShareListVo vo = new QualityShareListVo();
		try {
			/**
			 * NUll值转换
			 */
			vo.setStandardReadingId(String.valueOf(read.getId()) == null ? "" : String.valueOf(read.getId()));
			vo.setTitle(read.getTitle() == null ? "" : read.getTitle());
			vo.setAuthor(""); // 不需要返回专家
			String time = DateHelper.dataToString(read.getAddTime(), "yyyy-MM-dd HH:mm:ss");
			vo.setTime(time);
			vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
			vo.setPhoto(read.getPhoto() == null ? "" : jointUrl + read.getPhoto());
			vo.setPhoto2(read.getPhoto2() == null ? "" : jointUrl + read.getPhoto2());
			vo.setPhoto3(read.getPhoto3() == null ? "" : jointUrl + read.getPhoto3());
			vo.setPhoto4(read.getPhoto4() == null ? "" : jointUrl + read.getPhoto4());
			vo.setCover(read.getCover() == null ? defaultPhoto : jointUrl + read.getCover());
			vo.setMoney(read.getMoney());
			vo.setType(read.getType() == null ? "" : read.getType());
			vo.setIntro(read.getIntro() == null ? "" : read.getIntro());
			vo.setContentType(read.getContentType() == null ? "" : read.getContentType());
			vo.setContent(read.getContent() == null ? "" : read.getContent());
			vo.setVoice(read.getVoice() == null ? "" : read.getVoice());
			vo.setVoiceDuration(read.getVoiceDuration() == null ? "" : read.getVoiceDuration());
			vo.setQualityId(read.getQualityId());
			if (buys == null) {
				vo.setBuyStatus("0");
			} else {
				vo.setBuyStatus(buys.getPayStatus());
			}
			vo.setCollectStatus(collect);
			vo.setShareURL(shareURL); // 分享URL

			List<Adjunct> adjunctList = new ArrayList<Adjunct>();
			Adjunct adjunct = new Adjunct();
			if (portType != null && "1".equals(portType)) {

				/**
				 * 把多个附件的信息拼接起来
				 */
				String documentID = read.getDocumentId();
				String voices = read.getVoice();

				String[] strList = documentID.split(",");
				StringBuffer file = new StringBuffer();
				StringBuffer document = new StringBuffer();
				StringBuffer format = new StringBuffer();
				StringBuffer pageCount = new StringBuffer();
				StringBuffer vocsbf = new StringBuffer();

				for (int i = 0; i <= strList.length - 1; i++) {
					GetDocumentResponse doc = BDocHelper.readDocument(strList[i]);
					file.append(",").append(doc.getTitle());
					document.append(",").append(doc.getDocumentId());
					format.append(",").append(doc.getFormat());
					pageCount.append(",").append(doc.getPublishInfo().getPageCount());
					if (voices != null && !"".equals(voices)) { // 附件中可能没有音频文件
						if (voices.contains(",")) {
							String[] vocstr = voices.split(",");
							if (vocstr.length > 0 && vocstr.length >= i + 1) {
								vocsbf.append(",").append(jointUrl + "voice/" + vocstr[i]); // 拼接音频文件
							} else {
								vocsbf.append(","); // 没有音频文件就用空字符串代替
							}
						} else {
							adjunct.setVoice(jointUrl + "voice/"+read.getVoice());
						}
					} else {
						adjunct.setVoice("");
					}
				}
				adjunct.setTitle(read.getTitle());
				adjunct.setIntro(read.getIntro());
				adjunct.setFileName(file.toString().replaceFirst(",", ""));
				adjunct.setDocumentID(document.toString().replaceFirst(",", ""));
				adjunct.setFormat(format.toString().replaceFirst(",", ""));
				adjunct.setPageCount(pageCount.toString().replaceFirst(",", ""));
				if (voices != null && !"".equals(voices)) {
					adjunct.setVoice(vocsbf.toString().replaceFirst(",", ""));
				} else {
					adjunct.setVoice("");
				}
				adjunctList.add(adjunct);
				vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo; // 把单个解读对象封装到列表中去
	}
	
	public static QualityShareListVo setStandardReadingVoInfo(StandardReading read, Member member, ExpenseTotal buys,
			String collect, String portType, String shareURL) {
		QualityShareListVo vo = new QualityShareListVo();
		try {
			/**
			 * NUll值转换
			 */
			vo.setStandardReadingId(String.valueOf(read.getId()) == null ? "" : String.valueOf(read.getId()));
			vo.setTitle(read.getTitle() == null ? "" : read.getTitle());
			vo.setAuthor(""); // 不需要返回专家
			String time = DateHelper.dataToString(read.getAddTime(), "yyyy-MM-dd HH:mm:ss");
			vo.setTime(time);
			vo.setAuthorIntro(member.getIntro() == null ? "" : member.getIntro());
			vo.setPhoto(read.getPhoto() == null ? "" : jointUrl + read.getPhoto());
			vo.setPhoto2(read.getPhoto2() == null ? "" : jointUrl + read.getPhoto2());
			vo.setPhoto3(read.getPhoto3() == null ? "" : jointUrl + read.getPhoto3());
			vo.setPhoto4(read.getPhoto4() == null ? "" : jointUrl + read.getPhoto4());
			vo.setCover(read.getCover() == null ? defaultPhoto : jointUrl + read.getCover());
			vo.setMoney(read.getMoney());
			vo.setType(read.getType() == null ? "" : read.getType());
			vo.setIntro(read.getIntro() == null ? "" : read.getIntro());
			vo.setContentType(read.getContentType() == null ? "" : read.getContentType());
			vo.setContent(read.getContent() == null ? "" : read.getContent());
			vo.setVoice(read.getVoice() == null ? "" : read.getVoice());
			vo.setVoiceDuration(read.getVoiceDuration() == null ? "" : read.getVoiceDuration());
			vo.setQualityId(read.getQualityId());
			if (buys == null) {
				vo.setBuyStatus("0");
			} else {
				vo.setBuyStatus(buys.getPayStatus());
			}
			vo.setCollectStatus(collect);
			vo.setShareURL(shareURL); // 分享URL

			List<Adjunct> adjunctList = new ArrayList<Adjunct>();
			Adjunct adjunct = new Adjunct();
			if (portType != null && "1".equals(portType)) {

				/**
				 * 把多个附件的信息拼接起来
				 */
				String documentID = read.getDocumentId();
				String voices = read.getVoice();

				String[] strList = documentID.split(",");
				StringBuffer file = new StringBuffer();
				StringBuffer document = new StringBuffer();
				StringBuffer format = new StringBuffer();
				StringBuffer pageCount = new StringBuffer();
				StringBuffer vocsbf = new StringBuffer();

				for (int i = 0; i <= strList.length - 1; i++) {
					GetDocumentResponse doc = BDocHelper.readDocument(strList[i]);
					file.append(",").append(doc.getTitle());
					document.append(",").append(doc.getDocumentId());
					format.append(",").append(doc.getFormat());
					pageCount.append(",").append(doc.getPublishInfo().getPageCount());
					if (voices != null && !"".equals(voices)) { // 附件中可能没有音频文件
						if (voices.contains(",")) {
							String[] vocstr = voices.split(",");
							if (vocstr.length > 0 && vocstr.length >= i + 1) {
								vocsbf.append(",").append(jointUrl + "voice/" + vocstr[i]); // 拼接音频文件
							} else {
								vocsbf.append(","); // 没有音频文件就用空字符串代替
							}
						} else {
							adjunct.setVoice(jointUrl + "voice/"+read.getVoice());
						}
					} else {
						adjunct.setVoice("");
					}
				}
				adjunct.setTitle(read.getTitle());
				adjunct.setIntro(read.getIntro());
				adjunct.setFileName(file.toString().replaceFirst(",", ""));
				adjunct.setDocumentID(document.toString().replaceFirst(",", ""));
				adjunct.setFormat(format.toString().replaceFirst(",", ""));
				adjunct.setPageCount(pageCount.toString().replaceFirst(",", ""));
				if (voices != null && !"".equals(voices)) {
					adjunct.setVoice(vocsbf.toString().replaceFirst(",", ""));
				} else {
					adjunct.setVoice("");
				}
				adjunctList.add(adjunct);
				vo.setSubStandardReadingList(adjunctList); // 把附件列表封装到解读对象中去
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo; // 把单个解读对象封装到列表中去
	}

}
