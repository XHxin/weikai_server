package cc.messcat.dao.member;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import cc.messcat.bases.BaseDaoImpl;
import cc.messcat.entity.ExpenseTotal;
import cc.modules.commons.Pager;
import cc.modules.util.ObjValid;
import org.springframework.dao.DataAccessResourceFailureException;

public class ExpenseTotalDaoImpl extends BaseDaoImpl implements ExpenseTotalDao {

    @Override
    public void update(ExpenseTotal expenseTotal) {
        getHibernateTemplate().update(expenseTotal);
    }

    @Override
    public void save(ExpenseTotal expenseTotal) {
        getHibernateTemplate().save(expenseTotal);
    }

    @Override
    public Pager findBuysRecordsByCondiction(Long memberId, String type, int pageNo, int pageSize) {
        try {
            Session session = this.getSession();
            Criteria criteria = session.createCriteria(ExpenseTotal.class);
            criteria.add(Restrictions.eq("memberId", memberId));
            criteria.add(Restrictions.eq("payStatus", "1"));
            if (!"0".equals(type)) {
                criteria.add(Restrictions.eq("type", type));
            }
            criteria.addOrder(Order.desc("payTime"));
            int rowCount = ((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
            criteria.setProjection(null);
            int startIndex = pageSize * (pageNo - 1);
            criteria.setFirstResult(startIndex);
            criteria.setMaxResults(pageSize);
            List result = criteria.list();
            return new Pager(pageSize, pageNo, rowCount, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pager(pageSize, pageNo, 0, new ArrayList());
    }

    /**
     * 用于判断问题是否被购买
     */
    @SuppressWarnings("unchecked")
    @Override
    public ExpenseTotal getReplyBuys(Long memberId, Long expertId, Long problemId) {
        Criteria criteria = getSession().createCriteria(ExpenseTotal.class)
                .add(Restrictions.eq("memberId", memberId)).add(Restrictions.eq("relatedId", problemId))
                // .add(Restrictions.eq("replyExpertId", expertId)) //
                // 有可能出现,用户向不同的专家提同样的问题,所以用这个字段区别不同的专家
                .add(Restrictions.eq("payStatus", "1")).add(Restrictions.eq("type", "4"));
        List<ExpenseTotal> list = criteria.list();
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据条件查询购买
     */
    @Override
    public List<ExpenseTotal> getBuyRecordByCon(ExpenseTotal record) {
        StringBuffer sb = new StringBuffer("from ExpenseTotal where payStatus = 1 ");
        if (ObjValid.isValid(record.getMemberId())) {
            sb.append(" and memberId= ").append(record.getMemberId()).append(" ");
        }
        if (ObjValid.isValid(record.getRegionId())) {
            sb.append(" and regionId= ").append(record.getRegionId()).append(" ");
        }
        if (ObjValid.isValid(record.getRelatedId())) {
            sb.append(" and relatedId = ").append(record.getRelatedId()).append(" ");
        }
        if (ObjValid.isValid(record.getType())) {
            sb.append(" and type = '").append(record.getType()).append("' ");
        }
        List<ExpenseTotal> list = this.getHibernateTemplate().find(sb.toString());
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExpenseTotal getBuys(Long memberId, Long readId, String type) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.eq("memberId", memberId));
        criteria.add(Restrictions.eq("relatedId", readId));
        criteria.add(Restrictions.eq("payStatus", "1"));
        List<ExpenseTotal> list = criteria.list();
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExpenseTotal getEbusinessBuys(Long memberId, Long ebusinessProductId, String type) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.eq("memberId", memberId));
        criteria.add(Restrictions.eq("relatedId", ebusinessProductId));
        criteria.add(Restrictions.eq("payStatus", "1"));
        List<ExpenseTotal> list = criteria.list();
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExpenseTotal getProblemRecord(Long expertId, Long problemId) {
        Criteria criteria = getSession().createCriteria(ExpenseTotal.class)
                .add(Restrictions.eq("relatedId", problemId)).add(Restrictions.eq("expertId", expertId))
                .add(Restrictions.eq("payStatus", "1"));
        List<ExpenseTotal> list = criteria.list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public int findAllBuysByMemberId(Long memberId) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.eq("memberId", memberId));
        List<ExpenseTotal> recordNews = criteria.list();
        if (!recordNews.isEmpty() && recordNews.size() > 0) {
            return recordNews.size();
        }
        return 0;
    }

    @Override
    public List<ExpenseTotal> findAllBuysByLiveVideoId(Long[] relateId) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.in("relatedId", relateId));
        criteria.add(Restrictions.eq("type", "8"));
        List list = criteria.list();
        return list;
    }

    @Override
    public ExpenseTotal getBuyStatus(Long memberId, Long relateId, String type) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.eq("memberId", memberId));
        criteria.add(Restrictions.eq("relatedId", relateId));
        criteria.add(Restrictions.eq("payStatus", "1"));
        criteria.add(Restrictions.eq("type", type));
        List<ExpenseTotal> list = criteria.list();
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ExpenseTotal> findExpenseTotalByCondiction(Long memberId, String type, int pageNo, int pageSize) {
        try {
            Session session = this.getSession();
            Criteria criteria = session.createCriteria(ExpenseTotal.class);
            criteria.add(Restrictions.eq("memberId", memberId));
            criteria.add(Restrictions.eq("payStatus", "1"));
            if (!"0".equals(type)) {
                if ("2".equals(type)) {
                    criteria.add(Restrictions.in("type", new String[]{"2", "3"}));
                } else {
                    criteria.add(Restrictions.eq("type", type));
                }
            }

            if ("0".equals(type) || "8".equals(type)) {
                criteria.add(Restrictions.ne("content", "购买章节下的子视频（钱包支付）"));
                criteria.add(Restrictions.ne("content", "购买系列下的章节（钱包支付）"));
                criteria.add(Restrictions.ne("content", "购买系列下的子视频（钱包支付）"));
            }

            criteria.addOrder(Order.desc("payTime"));
            List<ExpenseTotal> result = criteria.list();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ExpenseTotal checkAriticleBuyStatus(Long memberId, Long articleId) {
        Criteria criteria = getSession().createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.eq("memberId", memberId));
        criteria.add(Restrictions.eq("payStatus", "1"));
        criteria.add(Restrictions.eq("relatedId", articleId));
        criteria.add(Restrictions.or(Restrictions.eq("type", "2"), Restrictions.eq("type", "3")));
        List<ExpenseTotal> recordNews = criteria.list();
        if (!recordNews.isEmpty() && recordNews.size() > 0) {
            return recordNews.get(0);
        }
        return null;
    }

    @Override
    public List<ExpenseTotal> getBuysRecordByTypeAndRelatedId(String type, Long relateId) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.eq("type", type))
                .add(Restrictions.eq("relatedId", relateId));
        List<ExpenseTotal> expenseTotals = criteria.list();
        return expenseTotals;
    }

    @Override
    public ExpenseTotal getBuysRecordByOrderNum(String merchantOrderNum) {
        Session session = this.getSession();
        Criteria criteria = session.createCriteria(ExpenseTotal.class);
        criteria.add(Restrictions.eq("number", merchantOrderNum));
        List<ExpenseTotal> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ExpenseTotal> getOneDayExpenseTotal(int year, int month, int day, Integer payType) {
        Session session = this.getSession();
        String sql = "select * from expense_total where year(pay_time)=? and month(pay_time)=? and day(pay_time) = ? and pay_type = ?";
        SQLQuery query = session.createSQLQuery(sql).addEntity(ExpenseTotal.class);
        query.setInteger(0, year);
        query.setInteger(1, month);
        query.setInteger(2, day);
        query.setInteger(3, payType);
        query.list();
        return query.list();
    }

    @Override
    public int updateVersion(ExpenseTotal order) {
        int affect = 0;
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        String sql = "update expense_total set version = version + 1 where order_num = ? and version = ?";
        SQLQuery query = session.createSQLQuery(sql).addEntity(ExpenseTotal.class);
        query.setString(0, order.getNumber());
        query.setInteger(1, order.getVersion());
        affect = query.executeUpdate();
        transaction.commit();
        return affect;
    }
}
