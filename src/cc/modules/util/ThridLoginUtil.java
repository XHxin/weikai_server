package cc.modules.util;

import java.text.SimpleDateFormat;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.MemAuth;
import cc.messcat.entity.Member;
import cc.messcat.vo.MemberVo;

/**
 * Aec Tool
 * @ClassName: AesUtil 
 * @Description: TODO
 * @author StevenWang
 */
@SuppressWarnings("serial")
public class ThridLoginUtil extends BaseManagerDaoImpl{
	
	private static String jointUrl = PropertiesFileReader.getByKey("joint.photo.url");// 图片拼接
	
	public MemberVo setMemberVoInfo(Member member, MemAuth memAuth){
		MemberVo memberVo = new MemberVo();
		memberVo.setMemberId(member.getId());
		memberVo.setMobile(member.getMobile()==null?"":member.getMobile());
		memberVo.setGrade(member.getGrade()==null || member.getGrade().isEmpty()?0:Integer.parseInt(member.getGrade()));
		memberVo.setRole(member.getRole()==null || member.getRole().isEmpty()?1:Integer.parseInt(member.getRole()));
		member.setRole(member.getRole()==null || member.getRole().isEmpty()?"1":member.getRole());
		if(member.getRole().equals("2")){
			memberVo.setExpertCheckStatus(member.getExpertCheckStatus()==null || member.getExpertCheckStatus().isEmpty()?0:Integer.parseInt(member.getExpertCheckStatus()));
		}else{
			memberVo.setExpertCheckStatus(0);
		}
		memberVo.setAccessToken(member.getAccessToken()==null?"":member.getAccessToken());
		memberVo.setVersionType(member.getAppVersion()==null?"":member.getAppVersion());
		memberVo.setType(member.getType()==null || member.getType().isEmpty()?2:Integer.parseInt(member.getType()));
		if(memAuth.getLoginType().equals("1")){
			memberVo.setPhoto(memAuth.getWeiXinPhoto());
		}else{
			memberVo.setPhoto(memAuth.getQqPhoto());
		}
		memberVo.setRealname(member.getRealname()==null?member.getMobile():member.getRealname());
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
				memberVo.setWorkCard(member.getWorkCard()==null?"":jointUrl+member.getWorkCard());
				memberVo.setVisitCard(member.getVisitCard()==null?"":jointUrl+member.getVisitCard());
				memberVo.setIdcardFront(member.getIdcardFront()==null?"":jointUrl+member.getIdcardFront());
				memberVo.setIdcardBack(member.getIdcardBack()==null?"":jointUrl+member.getIdcardBack());
			
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
				memberVo.setOpenBank(memberVo.getOpenBank()==null?"":memberVo.getOpenBank());
				memberVo.setBankCard(memberVo.getBankCard()==null?"":memberVo.getBankCard());
				memberVo.setCardholder(memberVo.getCardholder()==null?"":memberVo.getCardholder());
				memberVo.setBankMobile(memberVo.getBankMobile()==null?"":memberVo.getOpenBank());
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
		if(ObjValid.isValid(member.getGrade()) && member.getGrade().equals("1") && ObjValid.isValid(member.getEndTime())){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(member.getEndTime());
			memberVo.setEndTime(time);
		}else{
			memberVo.setEndTime("");
		}
		return memberVo;
	}

}
