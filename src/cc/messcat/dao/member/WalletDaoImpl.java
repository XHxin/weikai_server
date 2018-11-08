package cc.messcat.dao.member;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.Wallet;

public class WalletDaoImpl extends BaseDaoImpl implements WalletDao {

	@Override
	public Wallet get(Long memberId) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Wallet.class);
		criteria.add(Restrictions.eq("memberId", memberId));
		List<Wallet> list = criteria.list();
		if(!list.isEmpty() && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
