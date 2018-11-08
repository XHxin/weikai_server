package cc.messcat.entity;


import javax.persistence.*;

/**
 * @author leo
 * @date 2018/6/11 9:56
 */
@Entity
@Table(name="bank_member")
public class BankMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "open_bank")
    private String openBank;

    @Column(name = "bank_card")
    private String bankCard;

    @Column(name="cardholder")
    private String cardHolder;

    @Column(name="bank_mobile")
    private String bankMobile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }
}
