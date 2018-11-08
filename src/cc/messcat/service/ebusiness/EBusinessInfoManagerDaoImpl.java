package cc.messcat.service.ebusiness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.baidubce.services.doc.model.GetDocumentResponse;
import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.EBusinessInfo;
import cc.messcat.entity.EBusinessProduct;
import cc.messcat.entity.Member;
import cc.messcat.vo.EBusinessInfoVo;
import cc.messcat.vo.EBusinessInfoVo2;
import cc.messcat.vo.PlatformVo;
import cc.modules.commons.Pager;
import cc.modules.util.BDocHelper;
import cc.modules.util.ObjValid;
import cc.modules.util.PropertiesFileReader;

public class EBusinessInfoManagerDaoImpl extends BaseManagerDaoImpl implements EBusinessInfoManagerDao {

	private static final long serialVersionUID = 1L;
    private static String domain = PropertiesFileReader.getByKey("web.domain");// 图片拼接
	public EBusinessInfoManagerDaoImpl() {
	}

	public void addEBusinessInfo(EBusinessInfo info) {
		this.businessInfoDao.save(info);
	}

	public void modifyEBusinessInfo(EBusinessInfo info) {
		this.businessInfoDao.update(info);
	}

	public void removeEBusinessInfo(EBusinessInfo info) {
		this.businessInfoDao.delete(info);
	}

	public void removeEBusinessInfo(Long id) {
		this.businessInfoDao.delete(id);
	}

	public EBusinessInfo retrieveEBusinessInfo(Long id) {
		return (EBusinessInfo) this.businessInfoDao.get(id);
	}

	@SuppressWarnings("rawtypes")
	public List retrieveAllEBusinessInfos() {
		return this.businessInfoDao.findAll();
	}

	public Pager retrieveEBusinessInfosPager(int pageSize, int pageNo) {
		return this.businessInfoDao.getPager(pageSize, pageNo);
	}

	public Pager findEBusinessInfos(int pageSize, int pageNo, String statu) {
		Pager pager = businessInfoDao.getObjectListByClass(pageSize, pageNo, EBusinessInfo.class, statu);
		return pager;
	}

	/**
	 * 根据产品名查询产品
	 */
	@SuppressWarnings("rawtypes")
	public List getEBusinessInfoByName(String name) {
		StringBuffer sb = new StringBuffer("from EBusinessInfo where status = 1 ");
		// sb.append(" and name like '%").append(name.trim()).append("%' ");
		List list = businessInfoDao.findByhql(sb.toString());
		return list;
	}

	public Pager getEBusinessInfoByEProduct(String name, int pageNo, int pageSize) {
		List<EBusinessInfoVo> resultList = new ArrayList<EBusinessInfoVo>();
		// 根据电商产品名获取电商产品列表
		StringBuffer sb = new StringBuffer("from EBusinessProduct where status = 1 ");
		sb.append(" and name like '%").append(name.trim()).append("%' ");
		List<EBusinessProduct> list = businessProductDao.findByhql(sb.toString());
		int rowCount = list.size();
		int startIndex = pageSize * (pageNo - 1);
		int count = (int) rowCount;
		if (startIndex > count) {
			list = null;
		} else {
			list = list.subList(startIndex, (pageSize + startIndex) <= rowCount ? (pageSize + startIndex) : rowCount);
		}
		if (list != null) {
			for (EBusinessProduct eproduct : list) {
				EBusinessInfoVo vo = new EBusinessInfoVo();
				vo.setEbusinessProductId(eproduct.getId());
				vo.setTitle(eproduct.getName());
				// vo.setUrl("http://www.cert-map.com/epReportAction!getAppEBusiness.action?id="+eproduct.getId());
				resultList.add(vo);
			}
		}
		return new Pager(pageSize, pageNo, rowCount, resultList);
	}
	
	/**
	 * 根据产品id查产品
	 */

	public EBusinessInfoVo2 getEBusiness(Long id, Member puMember) {
		EBusinessInfoVo2 vo = new EBusinessInfoVo2();
		EBusinessProduct businessProduct = businessProductDao.get(id);
		// 根据产品id查出
		List<EBusinessInfo> ebs0 = new ArrayList<EBusinessInfo>();
		StringBuffer sb0 = new StringBuffer("from EBusinessInfo where status = 1 ");
		sb0.append(" and (ebusinessProductId = ").append(id);
		sb0.append(" or subEbusinessProductId like '%;").append(id).append(";%') ");
		List<EBusinessInfo> ebs = businessInfoDao.findByhql(sb0.toString());
		if (ObjValid.isValid(ebs)) {
			for (EBusinessInfo ebi : ebs) {
				if (ebs0.contains(ebi)) {
					continue;
				}
				ebs0.add(ebi);
			}
		}
		if (ObjValid.isValid(ebs0)) {
			vo.setUrl(domain+"/h5/market_report/page/commerce_detail.html?productId="+ebs0.get(0).getEbusinessProductId());
			vo.setEbusinessProductId(
					(Long) (ebs0.get(0).getEbusinessProductId() == null ? "" : ebs0.get(0).getEbusinessProductId()));
			vo.setTitle(ebs0.get(0).getEbusinessProductName());
			vo.setMoney((businessProduct.getMoney() == null ? new BigDecimal(0) : businessProduct.getMoney()));
			vo.setEbusinessProductId(
					(Long) (ebs0.get(0).getEbusinessProductId() == null ? "" : ebs0.get(0).getEbusinessProductId()));
			vo.setEbusinessProductName(
					ebs0.get(0).getEbusinessProductName() == null ? "" : ebs0.get(0).getEbusinessProductName());
			vo.setSubEbusinessProductId(
					ebs0.get(0).getSubEbusinessProductId() == null ? "" : ebs0.get(0).getSubEbusinessProductId());
			vo.setSubEbusinessProductName(
					ebs0.get(0).getSubEbusinessProductName() == null ? "" : ebs0.get(0).getSubEbusinessProductName());

			// 分享URL
			String shareURL = businessInfoDao.getShareURL();
			if (shareURL != null) {
				vo.setShareURL(shareURL + "&reportId=" + ebs0.get(0).getEbusinessProductId());
			} else {
				vo.setShareURL("无此分享URL");
			}
			// 判断是否是游客身份访问
			if (ObjValid.isValid(puMember)) {
				boolean collect = businessInfoDao.getCollect(ebs0.get(0).getEbusinessProductId(), puMember.getId());
				if (collect == true) {
					vo.setCollectStatus("1"); // 收藏状态
				} else {
					vo.setCollectStatus("0");
				}
			} else {
				vo.setCollectStatus("0");
			}
			// 判断是否是游客身份访问
			if (ObjValid.isValid(puMember)) {
				boolean buys = businessInfoDao.getBuys(ebs0.get(0).getEbusinessProductId(), puMember.getId());
				if (buys == true) {
					vo.setBuyStatus("1"); // 购买状态
				} else {
					vo.setBuyStatus("0");
				}
			} else {
				vo.setBuyStatus("0");
			}
			/**
			 * 获取电商商品价格
			 */
			EBusinessProduct eProduct = businessInfoDao.getProduct(ebs0.get(0).getEbusinessProductId());
			if (eProduct != null) {
				vo.setMoney(eProduct.getMoney());
			} else {
				vo.setMoney(new BigDecimal(0));
			}

			List<PlatformVo> platforms = new ArrayList<>();

			for (EBusinessInfo ebi : ebs0) {
				PlatformVo platform = new PlatformVo();
				platform.setBigClass(ebi.getBigClass() == null ? "" : ebi.getBigClass());
				platform.setPlatform(ebi.getPlatform() == null ? "" : ebi.getPlatform());
				platform.setSubProducts(ebi.getSubEbusinessProductName());

				platform.setSubProducts(ebi.getSubEbusinessProductName());
				/**
				 * 行业标准---附件信息
				 */
				platform.setIndustrialStandardName(
						ebi.getIndustrialStandardName() == null ? "" : ebi.getIndustrialStandardName()); // 文件
				if (ebi.getIndustrialStandardDocumentId() != null
						&& !"".equals(ebi.getIndustrialStandardDocumentId())) {
					String industDoc = ebi.getIndustrialStandardDocumentId();
					GetDocumentResponse document = BDocHelper.readDocument(industDoc);
					platform.setIndustrialStandardDocumentId(
							document.getDocumentId() == null ? "" : document.getDocumentId());
					platform.setIndustrialStandardFiletype(document.getFormat() == null ? "" : document.getFormat());
					platform.setIndustrialStandardFile(document.getTitle() == null ? "" : document.getTitle());
					platform.setIndustrialStandardPageCount(document.getPublishInfo().getPageCount());
				} else {
					platform.setIndustrialStandardDocumentId("");
					platform.setIndustrialStandardFiletype("");
					platform.setIndustrialStandardFile("");
					platform.setIndustrialStandardPageCount(0);
				}

				/**
				 * 抽查规范 ---附件信息
				 */
				platform.setCheckStandardName(ebi.getCheckStandardName() == null ? "" : ebi.getCheckStandardName()); // 文件
				if (ebi.getCheckStandardDocumentId() != null && !"".equals(ebi.getCheckStandardDocumentId())) {
					String checkDoc = ebi.getCheckStandardDocumentId();
					GetDocumentResponse document = BDocHelper.readDocument(checkDoc);
					platform.setCheckStandardDocumentId(
							document.getDocumentId() == null ? "" : document.getDocumentId());
					platform.setCheckStandardFiletype(document.getFormat() == null ? "" : document.getFormat());
					platform.setCheckStandardFile(document.getTitle() == null ? "" : document.getTitle());
					platform.setCheckStandardPageCount(document.getPublishInfo().getPageCount());
				} else {
					platform.setCheckStandardDocumentId("");
					platform.setCheckStandardFiletype("");
					platform.setCheckStandardFile("");
					platform.setCheckStandardPageCount(0);
				}

				/**
				 * 平台标准---附件信息
				 */
				platform.setPlatformStandardName(
						ebi.getPlatformStandardName() == null ? "" : ebi.getPlatformStandardName()); // 文件
				if ((ebi.getPlatformStandardDocumentId()) != null && !"".equals(ebi.getPlatformStandardDocumentId())) {
					String documentID = ebi.getCheckStandardDocumentId();
					GetDocumentResponse document = BDocHelper.readDocument(documentID);
					platform.setPlatformStandardFile(document.getTitle() == null ? "" : document.getTitle()); // 附件名称
					platform.setPlatformStandardFiletype(document.getFormat() == null ? "" : document.getFormat()); // 附件格式
					platform.setPlatformStandardDocumentId(
							document.getDocumentId() == null ? "" : document.getDocumentId());// 附件documentID
					platform.setPlatformStandardPageCount(document.getPublishInfo().getPageCount()); // 附件页码
				} else {
					platform.setPlatformStandardFile("");
					platform.setPlatformStandardFiletype("");
					platform.setPlatformStandardDocumentId("");
					platform.setPlatformStandardPageCount(0);
				}

				platforms.add(platform);

			}
			vo.setPlatforms(platforms);
		} else {
			vo.setEbusinessProductId(0L);
			vo.setEbusinessProductName("");
			vo.setUrl("");
			vo.setMoney(new BigDecimal(0.01));
			vo.setSubEbusinessProductId("");
			vo.setSubEbusinessProductName("");
			vo.setTitle("电商准入报告");
			List<PlatformVo> platforms = new ArrayList<PlatformVo>();
			vo.setPlatforms(platforms);
			vo.setCollectStatus("0");
			vo.setBuyStatus("0");
		}
		return vo;
	}

}