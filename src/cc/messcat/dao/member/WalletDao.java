package cc.messcat.dao.member;

import cc.messcat.bases.BaseDao;
import cc.messcat.entity.Wallet;

/**
 * @author nelson
 */
public interface WalletDao extends BaseDao{

	/**
	 * 根据memberId获取该member对应的钱包账户信息
	 * @param memberId
	 * @return
	 */
	public Wallet get(Long memberId);
}
