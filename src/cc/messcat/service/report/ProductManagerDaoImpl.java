package cc.messcat.service.report;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.*;
import cc.messcat.vo.ProductListVo;
import cc.messcat.vo.ProductVo;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import cc.modules.util.PropertiesFileReader;

public class ProductManagerDaoImpl extends BaseManagerDaoImpl implements ProductManagerDao {

	private static final long serialVersionUID = 1L;
	private static String domain = PropertiesFileReader.getByKey("web.domain");// 图片拼接
	private static String jointUrl = PropertiesFileReader.getByKey("static.domain");// 图片拼接

	public ProductManagerDaoImpl() {
	}

	@Override
	public Product retrieveProduct(Long id) {
		return (Product) this.productDao.get(id);
	}

	@Override
	public List retrieveAllProducts() {
		return this.productDao.findAll();
	}

	@Override
	public Pager retrieveProductsPager(int pageSize, int pageNo) {
		return this.productDao.getPager(pageSize, pageNo);
	}

	@Override
	public Pager findProducts(int pageSize, int pageNo, String statu) {
		Pager pager = productDao.getObjectListByClass(pageSize, pageNo, Product.class, statu);
		return pager;
	}

	/**
	 * 根据产品名查询产品
	 */
	@Override
	public List getProductByName(String name) {
		StringBuffer sb = new StringBuffer("from Product where status = 1 ");
		sb.append(" and name like '%").append(name.trim()).append("%' ");
		List list = productDao.findByhql(sb.toString());
		return list;
	}

	@Override
	public ProductListVo getProductByName(Integer pageSize, Integer pageNo, String name, Long regionId,
			Member puMember) {
		ProductListVo productListVo = new ProductListVo();
		try {
			DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
			StringBuffer sb = new StringBuffer("from Standard where status = 1 and regionID = "+regionId);
			if (ObjValid.isValid(name)) {
				sb.append(" and product_name like '%").append(name.trim()).append("%' ");
			}
			List<Standard> list = standardDao.findByhql(sb.toString());
			List<Product> productlist = new ArrayList<>();
			for (Standard standard : list) {
				Product product = standard.getProductId();
				productlist.add(product);
			}
			List<ProductVo> products = new ArrayList<ProductVo>();
			// 根据查到的产品和地区查询是否有该标准
			if (ObjValid.isValid(productlist)) {
				for (Product p : productlist) {
					if (null != list && list.size() > 0) {
						Standard standard = list.get(0);
						if (standard != null) {
							ProductVo pv = new ProductVo();
							pv.setProductId(p.getId());
							pv.setName(p.getName());
							pv.setRegionId(regionId);
							pv.setAddTime(p.getAddTime());
							pv.setEditTime(p.getEditTime());
							pv.setFatherId(p.getFatherId());
							pv.setFatherName(p.getFatherName()==null? "": p.getFatherName());
							pv.setStatus(p.getStatus());
							//游客身份登录进来,访问的memberId改为0
							if (puMember.getId()!=null) {
								pv.setUrl(domain + "/epReportAction!getAppReport.action?regionId=" + regionId
										+ "&productId=" + p.getId() + "&memberId=" + puMember.getId());
							} else {
								pv.setUrl(domain + "/epReportAction!getAppReport.action?regionId=" + regionId
										+ "&productId=" + p.getId() + "&memberId=" + 0);
							}
							String shareURL = productDao.getShareURL();
							if (shareURL != null) {
								pv.setShareURL(shareURL + "&regionId=" + regionId + "&productId=" + p.getId());
							} else {
								pv.setShareURL("无此分享URL");
							}
							pv.setMoney(divideScaleCommon.getUnifyFee() == null ? new BigDecimal(0) : divideScaleCommon.getUnifyFee());
							// 查询收藏状态(游客身份访问,为未收藏状态)
							if (puMember.getId()!=null) {
								Collect collect = new Collect();
								collect.setMemberId(puMember.getId());
								collect.setRegionId(regionId);
								collect.setRelatedId(p.getId());
								List<Collect> collects = collectDao.getCollectByCon(collect);
								if (ObjValid.isValid(collects)) {
									pv.setCollectStatus("1");// 已收藏
								} else {
									pv.setCollectStatus("0");// 未收藏收藏
								}
							} else {
								pv.setCollectStatus("0");
							}
							// 查询购买状态(游客身份访问,为未购买状态)
							if (puMember.getId()!=null && !"".equals(puMember.getId())) {
								ExpenseTotal record = new ExpenseTotal();
								record.setType("1");// 购买类型为报告购买
								record.setMemberId(puMember.getId());
								record.setRegionId(regionId);
								record.setRelatedId(productDao.get(p.getId()).getId());
								List<ExpenseTotal> records = expenseTotalDao.getBuyRecordByCon(record);
								if (ObjValid.isValid(records)) {
									pv.setBuyStutas("1");// 已购买
								} else {
									pv.setBuyStutas("0");// 未购买
								}
							} else {
								pv.setBuyStutas("0");
							}
							products.add(pv);
						}
					}
				}
			}

		
			int rowCount = products.size();
			if (rowCount < pageSize) {
				if(pageNo>1){
					productListVo.setProducts(new ArrayList<ProductVo>());
					return productListVo;
				}
				pageNo = 1;
			}
			int startIndex = pageSize * (pageNo - 1);
			if (startIndex > rowCount) {
				productListVo = null;
			} else {
				products = products.subList(startIndex,
						(pageSize + startIndex) <= products.size() ? (pageSize + startIndex) : products.size());
				productListVo.setPageNo(pageNo);
				productListVo.setPageSize(pageSize);
				productListVo.setRowCount(rowCount);
				productListVo.setProducts(products);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return productListVo;
	}
	
	
	/*
	 * 获取市场准入报告的详情
	 */
	@Override
	public ProductVo getMarketReportDetail(Long regionId, Long memberId, Long productId) {
		ProductVo pv = new ProductVo();
		// 查询收藏状态
		if(memberId!=0) {
			Collect collect = new Collect();
			collect.setMemberId(memberId);
			collect.setRegionId(regionId);
			collect.setRelatedId(productId);
			List<Collect> collects = collectDao.getCollectByCon(collect);
			if (ObjValid.isValid(collects)) {
				pv.setCollectStatus("1");// 已收藏
			} else {
				pv.setCollectStatus("0");// 未收藏收藏
			}
		}else {
			pv.setCollectStatus("0");
		}
		// 查询购买状态
		if(memberId!=0) {
			ExpenseTotal record = new ExpenseTotal();
			record.setType("1");// 购买类型为报告购买
			record.setMemberId(memberId);
			record.setRegionId(regionId);
			record.setRelatedId(productDao.get(productId).getId());
			List<ExpenseTotal> records = expenseTotalDao.getBuyRecordByCon(record);
			if (ObjValid.isValid(records)) {
				pv.setBuyStutas("1");// 已购买
			} else {
				pv.setBuyStutas("0");// 未购买
			}
		}else {
			pv.setBuyStutas("0");
		}
		// 查找系统的统一报告价格
		DivideScaleCommon divideScaleCommon = webSiteDao.gainDivideScaleCommon();
		pv.setMoney(divideScaleCommon.getUnifyFee() == null ? new BigDecimal(0) : divideScaleCommon.getUnifyFee());
		pv.setProductId(productId);
		pv.setRegionId(regionId);
		pv.setName(productDao.get(productId).getName());
		// 访问网页的URL
		String url = domain+"/epReportAction!getAppReport.action?regionId=" + regionId + "&productId="
				+ productId + "&memberId=" + memberId;
		pv.setUrl(url);
		String urlV2=domain+"/h5/market_report/page/access_report.html?_region="+regionId+"&_product="+productId
				+"&memberId="+memberId;
		pv.setUrlV2(urlV2);
		// 分享用的URL
		String shareURL = productDao.getShareURL();
		if (shareURL != null) {
			pv.setShareURL(shareURL + "&regionId=" + regionId + "&productId=" + productId);
		} else {
			pv.setShareURL("无此分享URL");
		}
		return pv;
	}

	/**
	 * 准入报告条目名称和内容(专为H5)
	 */
	@Override
	public Legal getMarketReportItem(Long regionId, Long productId,Long regionFatherId) {
		return productDao.getMarketReportItem(regionId,productId,regionFatherId);
	}

}