package cc.messcat.service.standardread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Attention;
import cc.messcat.entity.ExpertClassify;
import cc.messcat.entity.Member;
import cc.messcat.entity.HotReplyFees;
import cc.messcat.vo.ExpertVo;
import cc.messcat.vo.HotExpertListVo;
import cc.messcat.vo.PayConsultExpertVo;
import cc.modules.commons.Pager;
import cc.modules.util.PropertiesFileReader;

@SuppressWarnings("serial")
public class ExpertManagerDaoImpl extends BaseManagerDaoImpl implements ExpertManagerDao {
	private String defaultMemberPhoto = PropertiesFileReader.getByKey("member.photo.url");// 默认会员头像
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接

	/*
	 * 获取标准解读模块的 热门专家推荐专栏
	 */
	@Override
	public Pager getHotExpertList(int pageNo, int pageSize1, Member puMember) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		String recommendflag = "standardRead";
		List<HotExpertListVo> list = expertDao.getHotExpertList(recommendflag);
		if (!list.isEmpty() && list.size() > 0) {
			resultList = nullConvert(list, pageNo, pageSize1, puMember);
		}
		return new Pager(pageSize1, pageNo, resultList.size(), resultList);
	}

	/*
	 * 获取付费咨询模块的 热门专家推荐专栏
	 */
	@Override
	public List<HotExpertListVo> getPayExpertList(int pageNo, int pageSize2, Member puMember) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		String recommendflag = "payConsult";
		List<HotExpertListVo> list = expertDao.getHotExpertList(recommendflag);
		if (!list.isEmpty() && list.size() > 0) {
			resultList = nullConvert(list, pageNo, pageSize2, puMember);
		}
		return resultList;
	}

	@Override
	public Pager getHotExpertList2(int pageNo, int pageSize, Member puMember) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		List<HotExpertListVo> hotList = expertDao.getHotExpertList2();
		if (!hotList.isEmpty() && hotList.size() > 0) {
			resultList = nullConvert(hotList, pageNo, pageSize, puMember);
		}
		return new Pager(pageSize, pageNo, hotList.size(), resultList);
	}

	/*
	 * 专家详情
	 */
	@Override
	public HotExpertListVo getHotExpertDetail(Long expertId, Member puMember) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		List<HotExpertListVo> hotList = expertDao.getHotExpertDetail(expertId);
		if (!hotList.isEmpty() && hotList.size() > 0) {
			int pageNo = 1, pageSize = 10; // 提供这两个参数只为能调用公共方法
			resultList = nullConvert(hotList, pageNo, pageSize, puMember);
		}
		return resultList.get(0);
	}

	@Override
	public boolean clickAttention(Long memberId, Long expertId, String attentionStatus) {
		Attention attention = new Attention();
		/**
		 * 点击关注 返回true, 再次点击,就是取消关注,返回false
		 */
		boolean bok = false;
		if (attentionStatus.equals("1")) {
			attention.setAddTime(new Date());
			attention.setEditTime(new Date());
			attention.setMemberId(memberId);
			attention.setBeMemberId(expertId);
			attention.setStatus("1");
			bok = expertDao.addAttention(attention);
		} else {
			attention = expertDao.getAttention(memberId, expertId);
			expertDao.deleteAttention(attention);
		}
		return bok;
	}

	@Override
	public Pager getMyAttention(int pageNo, int pageSize, Member puMember) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		List<HotExpertListVo> list = expertDao.getMyAttention(puMember.getId());
		if (list != null && list.size() != 0) {
			resultList = nullConvert(list, pageNo, pageSize, puMember);
			return new Pager(pageSize, pageNo, list.size(), resultList);
		}
		return null;
	}

	/**
	 * 公共方法 null值转换
	 */
	private List<HotExpertListVo> nullConvert(List<HotExpertListVo> list, int pageNo, int pageSize, Member puMember) {
		List<HotExpertListVo> resultList = new ArrayList<HotExpertListVo>();
		for (int i = (pageNo - 1) * pageSize; i < list.size(); i++) {
			HotExpertListVo vo = new HotExpertListVo();
			vo.setExpertId(list.get(i).getExpertId() == null ? "" : list.get(i).getExpertId()); // NULL值转换
			vo.setIntro(list.get(i).getIntro() == null ? "" : list.get(i).getIntro());
			vo.setName(list.get(i).getName() == null ? "" : list.get(i).getName());
			vo.setPhoto(list.get(i).getPhoto() == null ? defaultMemberPhoto : jointUrl + list.get(i).getPhoto());
			vo.setAttentionSum(list.get(i).getAttentionSum());
			vo.setReadingSum(list.get(i).getReadingSum());
			vo.setField(list.get(i).getField() == null ? "" : list.get(i).getField());
			vo.setCompany(list.get(i).getCompany() == null ? "" : list.get(i).getCompany());
			vo.setMajor(list.get(i).getMajor() == null ? "" : list.get(i).getMajor());
			vo.setPosition(list.get(i).getPosition() == null ? "" : list.get(i).getPosition());
			vo.setProfession(list.get(i).getProfession() == null ? "" : list.get(i).getProfession());
			vo.setSchool(list.get(i).getSchool() == null ? "" : list.get(i).getSchool());
			vo.setWorkYear(list.get(i).getWorkYear() == null ? "" : list.get(i).getWorkYear());
			vo.setMoney(list.get(i).getMoney());
			vo.setPrivateMoney(list.get(i).getPrivateMoney());
			// 分享URL
			String shareURL = expertDao.getShareURL();
			vo.setShareURL(shareURL + "&expertId=" + list.get(i).getExpertId());
			/*
			 * 加入游客身份之后做的判断(三期)
			 */
			if (puMember.getId() != null) {
				// 根据memberId查看当前用户是否关注了该专家
				String bok = expertDao.isAttention(list.get(i).getExpertId(), puMember.getId());
				vo.setAttentionStatus(bok);
				// 点赞时间,用于判断是否已过当天的24点,每天仅可点赞一次,不可取消点赞
				String likeTime = expertDao.getLikeTime(list.get(i).getExpertId(), puMember.getId());
				if (likeTime != null && !"".equals(likeTime)) {
					vo.setLikeTime(likeTime);
				} else {
					vo.setLikeTime("");
				}
			} else {
				vo.setAttentionStatus("0");
				vo.setLikeTime("");
			}
			// 获取点赞总数(计算此专家被多少人点过赞)
			int likeSum = expertDao.getLikeSum(list.get(i).getExpertId());
			vo.setLikeSum(likeSum);
			resultList.add(vo);
			vo.setAnswerSum(list.get(i).getAnswerSum());
			if (resultList.size() >= pageSize) { // 控制每次翻页显示专家信息条数
				break;
			}
		}
		return resultList;
	}

	/*
	 * 点赞功能
	 */
	@Override
	public boolean clickLike(Long memberId, Long expertId, Date today) {
		return expertDao.clickLike(memberId, expertId, today);
	}

	/*
	 * 获取分类专家列表
	 */
	@Override
	public Pager getClassifyList(int pageNo, int pageSize, Long classifyId, Member puMember) {
		List<PayConsultExpertVo> resultList = new ArrayList<PayConsultExpertVo>();
		List<Member> list = new ArrayList<Member>(); // 从专家列表中得到Pager所需要的rowCount
		ExpertClassify classify = expertDao.getExpertList(classifyId);
		if (classify != null) {
			List<Member> expertlist = classify.getExperts();
			/*
			 * 筛选出需要遍历的专家集合, 没有必要全部做遍历
			 */
			// outer为跳出循环体的自定义标志
			outer: for (int i = (pageNo - 1) * pageSize; i <= expertlist.size() - 1; i++) {
				list.add(expertlist.get(i));
				if (list.size() >= pageSize) {
					break outer;
				}
			}
			for (Member member : list) {
				PayConsultExpertVo vo = gainExpert(member, puMember);
				resultList.add(vo);
			}
		}
		return new Pager(pageSize, pageNo, list.size(), resultList);
	}

	/*
	 * 查看更多的提问专家
	 */
	@Override
	public Pager getProExpertList(int pageNo, int pageSize, Member puMember) {
		List<PayConsultExpertVo> resultList = new ArrayList<PayConsultExpertVo>();
		List<Member> memberList = expertDao.getMember();
		List<Member> list = new ArrayList<Member>();
		outer: for (int i = (pageNo - 1) * pageSize; i <= memberList.size() - 1; i++) {
			list.add(memberList.get(i));
			if (list.size() >= pageSize) {
				break outer;
			}
		}
		if (list != null && list.size() != 0) {
			for (Member member : list) {
				PayConsultExpertVo vo = gainExpert(member, puMember);
				resultList.add(vo);
			}
		}
		return new Pager(pageSize, pageNo, memberList.size(), resultList);
	}

	@Override
	public PayConsultExpertVo getPayExpertDetail(Long expertId, Member puMember) {
		Member expert = expertDao.getExpert(expertId);
		PayConsultExpertVo vo = gainExpert(expert, puMember);
		return vo;
	}

	/*
	 * 付费咨询 专家公共方法
	 */
	private PayConsultExpertVo gainExpert(Member expert, Member puMember) {
		PayConsultExpertVo vo = new PayConsultExpertVo();
		vo.setExpertId(expert.getId());
		vo.setName(expert.getRealname() == null ? "" : expert.getRealname());
		vo.setIntro(expert.getIntro() == null ? "" : expert.getIntro());
		vo.setPhoto(expert.getPhoto() == null ? defaultMemberPhoto : jointUrl + expert.getPhoto());
		HotReplyFees fees = expertDao.getReplyFees(expert.getId());
		if (fees != null) {
			vo.setMoney(fees.getMoney());
			vo.setPrivateMoney(fees.getPrivateMoney());
		} else {
			vo.setMoney(new BigDecimal(0));
			vo.setPrivateMoney(new BigDecimal(0));
		}
		int answerSum = expertDao.getAnswerSum(expert.getId()); // 回答量
		vo.setAnswerSum(answerSum);
		int attentionSum = expertDao.getAttentionSum(expert.getId()); // 关注量
		vo.setAttentionSum(attentionSum);
		int likeSum = expertDao.getLikeSum(expert.getId());
		vo.setLikeSum(likeSum);
		/*
		 * 游客身份访问
		 */
		if (puMember.getId() != null) {
			// 关注状态
			String bok = expertDao.isAttention(String.valueOf(expert.getId()), puMember.getId());
			vo.setAttentionStatus(bok);
			// 点赞时间
			String likedTime = expertDao.getLikeTime(String.valueOf(expert.getId()), puMember.getId());
			vo.setLikeTime(likedTime == null ? "" : likedTime);
		} else {
			vo.setAttentionStatus("0");
			vo.setLikeTime("");
		}
		vo.setField(expert.getField() == null ? "" : expert.getField());
		vo.setProfession(expert.getProfession() == null ? "" : expert.getProfession());
		vo.setWorkYear(expert.getWorkYears() == null ? "" : expert.getWorkYears());
		vo.setSchool(expert.getSchool() == null ? "" : expert.getSchool());
		vo.setMajor(expert.getMajor() == null ? "" : expert.getMajor());
		vo.setCompany(expert.getCompany() == null ? "" : expert.getCompany());
		vo.setPosition(expert.getPosition() == null ? "" : expert.getPosition());
		vo.setIntro(expert.getIntro() == null ? "" : expert.getIntro());
		// 他的文章量
		int readSum = expertDao.getReadSum(expert.getId());
		vo.setReadSum(readSum);
		vo.setReadingSum(readSum);
		// 分享URL
		String shareURL = expertDao.getShareURL();
		vo.setShareURL(shareURL + "&expertId=" + expert.getId());
		return vo;
	}

	@Override
	public List<ExpertVo> getExpertBySearchKeyWord(String searchKeyWord) {

		List<ExpertVo> expertVos = new ArrayList<>();
		List<Member> allExpert = expertDao.getExpertBySearchKeyWord(searchKeyWord);

		for (Member member : allExpert) {
			ExpertVo expertVo = new ExpertVo();
			Long id = member.getId();
			expertVo.setExpertId(id);
			// expertVo.setMoney(expertDao.getReplyFees(id).getMoney());
			expertVo.setExpertName(member.getRealname());
			expertVo.setPhoto(jointUrl + member.getPhoto());
			// expertVo.setReadSum(expertDao.getReadSum(id));
			expertVo.setIntro(member.getIntro());
			expertVo.setAttentionSum(expertDao.getAttentionSum(member.getId()));
			expertVo.setAnswerSum(expertDao.getAnswerSum(member.getId()));
			expertVo.setArticleSum(expertDao.getArticleSum(member.getId()));
			expertVos.add(expertVo);
		}
		return expertVos;
	}

	@Override
	public Pager getMoreExpert(String searchKeyWord, int pageNo, int pageSize) {

		List<ExpertVo> expertVos = new ArrayList<>();
		List<Member> allExpert = expertDao.getMoreExpert(searchKeyWord, pageNo, pageSize);

		for (Member member : allExpert) {
			if(member.getRealname().contains("世界认证地图")||member.getRealname().contains("测试")){
				continue;
			}
			ExpertVo expertVo = new ExpertVo();
			Long id = member.getId();
			expertVo.setExpertId(id);
			// expertVo.setMoney(expertDao.getReplyFees(id).getMoney());
			expertVo.setExpertName(member.getRealname());
			expertVo.setPhoto(jointUrl + member.getPhoto());
			// expertVo.setReadSum(expertDao.getReadSum(id));
			expertVo.setAttentionSum(expertDao.getAttentionSum(member.getId()));
			expertVo.setAnswerSum(expertDao.getAnswerSum(member.getId()));
			expertVo.setArticleSum(expertDao.getArticleSum(member.getId()));
			expertVo.setAttentionSum(expertDao.getAttentionSum(id));
			expertVo.setIntro(member.getIntro());
			expertVos.add(expertVo);
		}
		return new Pager(pageSize, pageNo, expertVos.size(), expertVos);
	}
}
