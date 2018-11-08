package cc.modules.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import cc.messcat.entity.Member;
import cc.messcat.vo.MemberVo;

/**
 * Aec Tool
 * @ClassName: AesUtil 
 * @Description: TODO
 * @author StevenWang
 */
public class MemberUtil {
	
	private static String defaultMemberPhoto = PropertiesFileReader.getByKey("member.photo.url");// 默认会员头像
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接
	
	public static MemberVo setMemberVoInfo(Member member){
		MemberVo memberVo = new MemberVo();
		memberVo.setMemberId(member.getId());
		memberVo.setUuid(StringUtil.isBlank(member.getUuid())?"":member.getUuid());
		memberVo.setTourist(StringUtil.isBlank(member.getTourist())?"":member.getTourist());
		memberVo.setMobile(StringUtil.isBlank(member.getMobile())?"":member.getMobile());
		memberVo.setGrade(StringUtil.isBlank(member.getGrade())?0:Integer.parseInt(member.getGrade()));
		memberVo.setRole(StringUtil.isBlank(member.getRole())?1:Integer.parseInt(member.getRole()));
		memberVo.setJob(member.getJob());
		
		member.setRole(StringUtil.isBlank(member.getRole())?"1":member.getRole());
		if(member.getRole().equals("2")){
			memberVo.setExpertCheckStatus(StringUtil.isBlank(member.getExpertCheckStatus())?0:Integer.parseInt(member.getExpertCheckStatus()));
		}else{
			memberVo.setExpertCheckStatus(0);
		}
		memberVo.setAccessToken(StringUtil.isBlank(member.getAccessToken())?"":member.getAccessToken());
		memberVo.setVersionType(StringUtil.isBlank(member.getAppVersion())?"":member.getAppVersion());
		memberVo.setType(StringUtil.isBlank(member.getType())?2:Integer.parseInt(member.getType()));
		if(StringUtil.isNotBlank(member.getPhoto())) {
			memberVo.setPhoto(jointUrl + member.getPhoto());
		}else {
			if(StringUtil.isNotBlank(member.getImagePath())) {
				memberVo.setPhoto(member.getImagePath());
			} else {
				memberVo.setPhoto(defaultMemberPhoto);
			}
		}
		if(member.getUuid() == null || member.getUuid().equals("0")){
			memberVo.setRealname(StringUtil.isBlank(member.getRealname())?member.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"):member.getRealname());
		}else {
			memberVo.setRealname("游客"+member.getId());
		}
		memberVo.setCompany(member.getCompany()==null?"":member.getCompany());
		memberVo.setAddress(member.getAddress()==null?"":member.getAddress());
		if(ObjValid.isValid(member.getRole())){
			if(member.getRole().equals("2")){
				memberVo.setWorkYears(member.getWorkYears()==null?"":member.getWorkYears());
				memberVo.setField(member.getField()==null?"":member.getField());
				memberVo.setEmail(member.getEmail()==null?"":member.getEmail());
				memberVo.setSchool(member.getSchool()==null?"":member.getSchool());
				memberVo.setMajor(member.getMajor()==null?"":member.getMajor());
				memberVo.setIntro(member.getIntro()==null?"":member.getIntro());
				memberVo.setOpenBank(memberVo.getOpenBank()==null?"":memberVo.getOpenBank());
				memberVo.setBankCard(memberVo.getBankCard()==null?"":memberVo.getBankCard());
				memberVo.setCardholder(memberVo.getCardholder()==null?"":memberVo.getCardholder());
				memberVo.setBankMobile(memberVo.getBankMobile()==null?"":memberVo.getOpenBank());
				memberVo.setProfession(member.getProfession()==null?"":member.getProfession());
				memberVo.setPosition(member.getPosition()==null?"":member.getPosition());
				memberVo.setWorkCard(member.getWorkCard()==null || member.getWorkCard().isEmpty()?"":jointUrl+member.getWorkCard());
				memberVo.setVisitCard(member.getVisitCard()==null || member.getVisitCard().isEmpty()?"":jointUrl+member.getVisitCard());
				memberVo.setIdcardFront(member.getIdcardFront()==null || member.getIdcardFront().isEmpty()?"":jointUrl+member.getIdcardFront());
				memberVo.setIdcardBack(member.getIdcardBack()==null || member.getIdcardBack().isEmpty()?"":jointUrl+member.getIdcardBack());
			}else{
				memberVo.setJob(member.getJob()==null?"":member.getJob());
				memberVo.setWorkYears("");
				memberVo.setField("");
				memberVo.setEmail("");
				memberVo.setSchool("");
				memberVo.setMajor("");
				memberVo.setIntro("");
				memberVo.setProfession("");
				memberVo.setOpenBank(memberVo.getOpenBank()==null?"":memberVo.getOpenBank());
				memberVo.setBankCard(memberVo.getBankCard()==null?"":memberVo.getBankCard());
				memberVo.setCardholder(memberVo.getCardholder()==null?"":memberVo.getCardholder());
				memberVo.setBankMobile(memberVo.getBankMobile()==null?"":memberVo.getOpenBank());
				memberVo.setPosition("");
				memberVo.setWorkCard("");
				memberVo.setVisitCard("");
				memberVo.setIdcardFront("");
				memberVo.setIdcardBack("");
			}
		}else{
			memberVo.setJob(member.getJob()==null?"":member.getJob());
			memberVo.setWorkYears("");
			memberVo.setField("");
			memberVo.setEmail("");
			memberVo.setSchool("");
			memberVo.setMajor("");
			memberVo.setIntro("");
			memberVo.setProfession("");
			memberVo.setPosition("");
			memberVo.setWorkCard("");
			memberVo.setVisitCard("");
			memberVo.setIdcardFront("");
			memberVo.setIdcardBack("");
		}
		/*if(ObjValid.isValid(member.getGrade()) && member.getGrade().equals("1") && ObjValid.isValid(member.getEndTime()) && ObjValid.isValid(member.getYearEndTime())){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = "";
			if (member.getEndTime().before(new Date())) {
				time = sdf.format(member.getYearEndTime());
			} else {
				time = sdf.format(member.getEndTime());
			}
			memberVo.setEndTime(time);
		}else{
			memberVo.setEndTime("");
		}*/

		return memberVo;
	}

}
