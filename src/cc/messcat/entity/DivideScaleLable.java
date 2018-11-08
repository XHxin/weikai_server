package cc.messcat.entity;

import org.hibernate.annotations.Entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @auther xiehuaxin
 * @create 2018-09-07 11:42
 * @todo 分成比例标签实体
 */
@Entity
@Table(name = "divide_scale_label")
public class DivideScaleLable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**分成比例集合，中间用逗号隔开,与memberIds一一对应*/
    @Column(name = "proportions")
    private String proportions;

    /**分组类型(0:自定义;1:直播;2:录播;3:标准解读、质量分析、专栏)*/
    @Column(name = "group_type")
    private String groupType;

    /**标签名*/
    @Column(name = "name")
    private String name;

    /**参与分成的memberId集合，中间用英文逗号隔开*/
    @Column(name = "memberIds")
    private String memberIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProportions() {
        return proportions;
    }

    public void setProportions(String proportions) {
        this.proportions = proportions;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }
}
