package cc.messcat.service.member;

import cc.messcat.entity.Wallet;

/**
 * @author nelson
 *
 */
public interface WalletManagerDao {

	/**
	 * 根据memberId获取该member对应的钱包账户信息
	 * @param memberId
	 * @return
	 */
	public Wallet get(Long memberId);
	
	/**
	 * 更新钱包对象
	 */
	public void update(Wallet wallet);
}
