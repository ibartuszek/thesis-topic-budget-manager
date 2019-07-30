package hu.elte.bm.transactionservice.dal.transaction;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.domain.Currency;

@Entity
@Table(name = "incomes")
public final class IncomeEntity {

    private static final int TITLE_LENGTH = 100;
    private static final int DESCRIPTION_LENGTH = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = TITLE_LENGTH, nullable = false)
    private String title;
    private double amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "main_category_id", nullable = false)
    private MainCategoryEntity mainCategoryEntity;
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_category_id")
    private SubCategoryEntity subCategoryEntity;
    @Basic
    private boolean monthly;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(length = DESCRIPTION_LENGTH)
    private String description;
    private boolean locked;

    IncomeEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(final double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public MainCategoryEntity getMainCategoryEntity() {
        return mainCategoryEntity;
    }

    public void setMainCategoryEntity(final MainCategoryEntity mainCategoryEntity) {
        this.mainCategoryEntity = mainCategoryEntity;
    }

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }

    public void setSubCategoryEntity(final SubCategoryEntity subCategoryEntity) {
        this.subCategoryEntity = subCategoryEntity;
    }

    public boolean isMonthly() {
        return monthly;
    }

    public void setMonthly(final boolean monthly) {
        this.monthly = monthly;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(final boolean locked) {
        this.locked = locked;
    }
}
