package cc.messcat.dao.system;

import cc.messcat.entity.DivideScaleLable;

/**
 * @auther xiehuaxin
 * @create 2018-09-07 11:39
 * @todo
 */
public interface DivideScaleLableDao {

    /**
     * 根据分成比例标签id查询分成比例
     * @param lableId
     */
    DivideScaleLable getDivideScaleLableById(Long lableId);
}
