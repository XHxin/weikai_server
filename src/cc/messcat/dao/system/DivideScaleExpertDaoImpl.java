package cc.messcat.dao.system;

import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.DivideScaleExpert;
import org.apache.tools.ant.types.resources.Restrict;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @auther xiehuaxin
 * @create 2018-09-07 10:15
 * @todo
 */
public class DivideScaleExpertDaoImpl extends BaseDaoImpl implements DivideScaleExpertDao {
    @Override
    public DivideScaleExpert getDivideScaleExpertByReleateIdAndType(String type, Long relatedId) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(DivideScaleExpert.class);
        criteria.add(Restrictions.eq("type",type))
                .add(Restrictions.eq("relatedID",relatedId))
                .add(Restrictions.eq("status",1));
        List<DivideScaleExpert> divideScaleExpertList = criteria.list();
        if(!divideScaleExpertList.isEmpty() && divideScaleExpertList.size() > 0) {
            return divideScaleExpertList.get(0);
        }
        return null;
    }

    @Override
    public DivideScaleExpert getDivideScaleExpertByMemberIdAndType(Long beRewardId, String type) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(DivideScaleExpert.class)
                    .add(Restrictions.eq("type",type))
                    .add(Restrictions.eq("memberIds",beRewardId))
                    .add(Restrictions.eq("status",1));
        List<DivideScaleExpert> divideScaleExpertList = criteria.list();
        if(divideScaleExpertList != null && divideScaleExpertList.size() >0) {
            return divideScaleExpertList.get(0);
        }
        return null;
    }
}
