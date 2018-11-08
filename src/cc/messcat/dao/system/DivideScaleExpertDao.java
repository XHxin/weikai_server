package cc.messcat.dao.system;

import cc.messcat.entity.DivideScaleExpert;

/**
 * @auther xiehuaxin
 * @create 2018-09-07 10:13
 * @todo 商品分成比例实体
 */
public interface DivideScaleExpertDao {
    /**
     * 根据商品的类型及商品编号，获取商品对应的各方分成比例
     * @param type
     * @param relatedId
     * @return
     */
    public DivideScaleExpert getDivideScaleExpertByReleateIdAndType(String type, Long relatedId);

    /**
     * 根据类型type为10，确定是打算分成比例，然后再根据被打赏人的id,查询出被打赏人的分成比例
     * @param beRewardId
     * @param s
     * @return
     */
    DivideScaleExpert getDivideScaleExpertByMemberIdAndType(Long beRewardId, String s);
}
