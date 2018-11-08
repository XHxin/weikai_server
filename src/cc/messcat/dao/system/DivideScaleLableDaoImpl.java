package cc.messcat.dao.system;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.DivideScaleLable;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @auther xiehuaxin
 * @create 2018-09-07 11:54
 * @todo
 */
public class DivideScaleLableDaoImpl extends BaseDaoImpl implements DivideScaleLableDao {

    @Override
    public DivideScaleLable getDivideScaleLableById(Long lableId) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(DivideScaleLable.class);
        criteria.add(Restrictions.eq("id",lableId));
        List<DivideScaleLable> divideScaleLableList = criteria.list();
        if(!divideScaleLableList.isEmpty() && divideScaleLableList.size() > 0) {
            return divideScaleLableList.get(0);
        }
        return null;
    }
}
