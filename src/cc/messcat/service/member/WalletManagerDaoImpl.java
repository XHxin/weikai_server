package cc.messcat.service.member;

import cc.messcat.bases.BaseManagerDaoImpl;
import cc.messcat.entity.Wallet;

@SuppressWarnings("serial")
public class WalletManagerDaoImpl extends BaseManagerDaoImpl implements WalletManagerDao{

	@Override
	public Wallet get(Long memberId) {
		
		return walletDao.get(memberId);
	}

	@Override
	public void update(Wallet wallet) {
		walletDao.update(wallet);
	}

	
}
