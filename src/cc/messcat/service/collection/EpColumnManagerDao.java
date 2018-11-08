package cc.messcat.service.collection;

import cc.messcat.entity.EnterpriseColumn;

public interface EpColumnManagerDao {
	public EnterpriseColumn getEnterpriseColumn(Long id);

	public EnterpriseColumn getEnterpriseColumn(String state);
}