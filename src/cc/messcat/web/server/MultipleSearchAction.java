package cc.messcat.web.server;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import cc.messcat.entity.Member;
import cc.messcat.vo.ExpertVo;
import cc.messcat.vo.ExpertVoList;
import cc.messcat.vo.LiveVo;
import cc.messcat.vo.LiveVoList;
import cc.messcat.vo.MultpleVo;
import cc.messcat.vo.ReplyVo;
import cc.messcat.vo.ReplyVoList;
import cc.messcat.vo.ResponseBean;
import cc.messcat.vo.SpecialList;
import cc.messcat.vo.SpecialVo;
import cc.messcat.vo.StandReadingVo;
import cc.messcat.vo.StandReadingVoList;
import cc.messcat.vo.StandardReadListVo3;
import cc.messcat.vo.StandardReadListVo3List;
import cc.modules.commons.PageAction;
import cc.modules.commons.Pager;
import cc.modules.constants.Constants;
import cc.modules.util.ObjValid;
/**
 * 
 * @author wenyu
 *
 */
public class MultipleSearchAction extends PageAction {

	private static final long serialVersionUID = 1L;
	private String accessToken; // 请求accessToken
	private String searchKeyWord; // 关键字
	private Long memberId;
	private Object object;
	private String qualityId; // 质量分享id

	@Override
	public String execute() {
		if(memberId==null){
			memberId=0L;
		}
		Member member = new Member();
		if (memberId > 0) { // memberId为-1,则为游客身份登录, 直接判断,不用再去数据库查询是否有此会员
			member = memberManagerDao.retrieveMember(memberId);
			if (ObjValid.isValid(member)) {
				if (!accessToken.equals(member.getAccessToken())) {
					object = new ResponseBean(Constants.EXCEPTION_CODE_600, Constants.LOGIN_VERIFY_TOKEN_ERROR);
					return "success";
				}
			}
		}
		if (!StringUtils.isNotBlank(searchKeyWord)) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_INPUT_NOEMPTY);
		} else {
			MultpleVo result = new MultpleVo();

			searchKeyWord = searchKeyWord.trim();
			
			//质量分享
			List<StandReadingVo> standReadingShare = standardReadManagerDao.getStandardReadListBySearchKeyWord(searchKeyWord,memberId);
			//全部专家
			List<ExpertVo> experts = expertManagerDao.getExpertBySearchKeyWord(searchKeyWord);
			//付费咨询答问
			List<ReplyVo> hotReplyVos = hotReplyService.getReplyBySearchKeyWord(searchKeyWord);
			//视频课程
			List<LiveVo> liveVideos = liveService.getLiveVideBySearchKeyWord(searchKeyWord);
			//专题
			List<SpecialVo> SpecialVos = liveService.getSpecialBySearchKeyWord(searchKeyWord);
			//专栏
			List<StandardReadListVo3> standReading = standardReadManagerDao.getStandardReadSerialise(searchKeyWord,memberId);
			//编码的奥秘
			result.setStandReadingColumn(standReading);
			result.setStandReadingShare(standReadingShare);
			result.setExperts(experts);
			result.setHotReplyVos(hotReplyVos);
			result.setLiveVideos(liveVideos);
			result.setSpecialList(SpecialVos);
			result.setSearchKeyWord(searchKeyWord);
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		}
		return "success";
	}
	
	/**
	 * 点击查看更多的质量分享
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMoreStandReadShare(){
		
		if (!StringUtils.isNotBlank(searchKeyWord)) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_INPUT_NOEMPTY);
		} else {
			
			searchKeyWord = searchKeyWord.trim();
			
			StandReadingVoList result = new StandReadingVoList();
			
			Pager pager = standardReadManagerDao.getMoreStandardRead(searchKeyWord, qualityId, pageNo, pageSize,memberId);
			
			result.setRowCount(pager.getRowCount());
			result.setSearchKeyWord(searchKeyWord);
			result.setStandReadingShare(pager.getResultList());
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		}
		
		return "success";
	}
	
	/**
	 * 点击查看更多的专家
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMoreExpert(){

		if (!StringUtils.isNotBlank(searchKeyWord)) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_INPUT_NOEMPTY);
		} else {
			
			searchKeyWord = searchKeyWord.trim();
			
			ExpertVoList result = new ExpertVoList();
			
			 Pager pager = expertManagerDao.getMoreExpert(searchKeyWord, pageNo, pageSize);
			 
			 result.setRowCount(pager.getRowCount());
			 result.setSearchKeyWord(searchKeyWord);
			 result.setExperts(pager.getResultList());
			
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		}
		
		return "success";
	}
	
	/**
	 * 点击查看更多的付费咨询
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMoreHotReply(){
		
		if (!StringUtils.isNotBlank(searchKeyWord)) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_INPUT_NOEMPTY);
		} else {
			
			searchKeyWord = searchKeyWord.trim();
			
			ReplyVoList result = new ReplyVoList();
			
			Pager pager = hotReplyService.getMoreHotReply(searchKeyWord, pageNo, pageSize);
			
			result.setRowCount(pager.getRowCount());
			result.setSearchKeyWord(searchKeyWord);
			result.setHotReplyVos(pager.getResultList());
			
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		}
		
		return "success";
	}
	
	/**
	 * 点击查看更多的视频课程(包括直播)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMoreLiveVideo(){

		if (!StringUtils.isNotBlank(searchKeyWord)) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_INPUT_NOEMPTY);
		} else {
			
			searchKeyWord = searchKeyWord.trim();
			
			LiveVoList result = new LiveVoList();
			
			Pager pager = liveService.getMoreLiveVideo(searchKeyWord, pageNo, pageSize);
			
			result.setLiveVideos(pager.getResultList());
			result.setRowCount(pager.getRowCount());
			result.setSearchKeyWord(searchKeyWord);
			
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		}
		
		return "success";
	}
	
	/**
	 * 点击查看更多的专题
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMoreLiveSubject(){

		if (!StringUtils.isNotBlank(searchKeyWord)) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_INPUT_NOEMPTY);
		} else {
			
			searchKeyWord = searchKeyWord.trim();
			
			SpecialList result = new SpecialList();
			
			Pager pager = liveService.getMoreSpecial(searchKeyWord, pageNo, pageSize);
			
			result.setSpecialList(pager.getResultList());
			result.setRowCount(pager.getRowCount());
			result.setSearchKeyWord(searchKeyWord);
			
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		}
		return "success";
	}
	
	/**
	 * 点击查看更多的专栏
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getMoreStandReadSerialise(){
		
		if (!StringUtils.isNotBlank(searchKeyWord)) {
			object = new ResponseBean(Constants.EXCEPTION_CODE_500, Constants.MSG_INPUT_NOEMPTY);
		} else {
			
			searchKeyWord = searchKeyWord.trim();
			
			StandardReadListVo3List result = new StandardReadListVo3List();
			
			Pager pager = standardReadManagerDao.getMoreStandReadSerialise(searchKeyWord, pageNo, pageSize,memberId);
			
			result.setStandReadingColumn(pager.getResultList());
			result.setRowCount(pager.getRowCount());
			result.setSearchKeyWord(searchKeyWord);
			
			object = new ResponseBean(Constants.SUCCESS_CODE_200, Constants.MSG_FIND_SUCCESS, result);
		}
		return "success";
	}
	

	// ---------------------各种getter/setter方法-------------------------

	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getQualityId() {
		return qualityId;
	}

	public void setQualityId(String qualityId) {
		this.qualityId = qualityId;
	}
}
