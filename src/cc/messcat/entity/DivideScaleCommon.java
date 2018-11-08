package cc.messcat.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author leo
 * @date 2018/7/3 10:44
 */
@Entity
@Table(name="divide_scale_common")
public class DivideScaleCommon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="free_modules")
    private String freeModules;

    @Column(name="unify_fee")
    private BigDecimal unifyFee;

    @Column(name="month_vip_discount")
    private Integer monthVipDiscount;

    @Column(name="year_vip_discount")
    private Integer yearVipDiscount;

   /* @Column(name="expert_proportion")
    private Integer expertProportion;*/

    @Column(name="pay_other_problem_unifyfee")
    private BigDecimal payOtherProblemUnifyFee;

    @Column(name="pay_other_problem_discount")
    private Integer payOtherProblemDiscount;

    @Column(name="pay_other_problem_member")
    private Integer payOtherProblemMember;

    @Column(name="pay_other_problem_expert")
    private Integer payOtherProblemExpert;

    @Column(name = "pay_consult_expert")
    private Integer payConsultExpert;

    @Column(name = "divide_into_reward")
    private Integer divideScaleReward;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFreeModules() {
        return freeModules;
    }

    public void setFreeModules(String freeModules) {
        this.freeModules = freeModules;
    }


    public Integer getMonthVipDiscount() {
        return monthVipDiscount;
    }

    public void setMonthVipDiscount(Integer monthVipDiscount) {
        this.monthVipDiscount = monthVipDiscount;
    }

    public Integer getYearVipDiscount() {
        return yearVipDiscount;
    }

    public void setYearVipDiscount(Integer yearVipDiscount) {
        this.yearVipDiscount = yearVipDiscount;
    }

    /*public Integer getExpertProportion() {
        return expertProportion;
    }

    public void setExpertProportion(Integer expertProportion) {
        this.expertProportion = expertProportion;
    }*/

    public BigDecimal getUnifyFee() {
        return unifyFee;
    }

    public void setUnifyFee(BigDecimal unifyFee) {
        this.unifyFee = unifyFee;
    }

    public BigDecimal getPayOtherProblemUnifyFee() {
        return payOtherProblemUnifyFee;
    }

    public void setPayOtherProblemUnifyFee(BigDecimal payOtherProblemUnifyFee) {
        this.payOtherProblemUnifyFee = payOtherProblemUnifyFee;
    }

    public Integer getPayOtherProblemDiscount() {
        return payOtherProblemDiscount;
    }

    public void setPayOtherProblemDiscount(Integer payOtherProblemDiscount) {
        this.payOtherProblemDiscount = payOtherProblemDiscount;
    }

    public Integer getPayOtherProblemMember() {
        return payOtherProblemMember;
    }

    public void setPayOtherProblemMember(Integer payOtherProblemMember) {
        this.payOtherProblemMember = payOtherProblemMember;
    }

    public Integer getPayOtherProblemExpert() {
        return payOtherProblemExpert;
    }

    public void setPayOtherProblemExpert(Integer payOtherProblemExpert) {
        this.payOtherProblemExpert = payOtherProblemExpert;
    }

    public Integer getPayConsultExpert() {
        return payConsultExpert;
    }

    public void setPayConsultExpert(Integer payConsultExpert) {
        this.payConsultExpert = payConsultExpert;
    }

    public Integer getDivideScaleReward() {
        return divideScaleReward;
    }

    public void setDivideScaleReward(Integer divideScaleReward) {
        this.divideScaleReward = divideScaleReward;
    }
}
