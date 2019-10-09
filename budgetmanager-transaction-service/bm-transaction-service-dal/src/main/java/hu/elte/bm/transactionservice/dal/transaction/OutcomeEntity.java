package hu.elte.bm.transactionservice.dal.transaction;

import java.util.Date;

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

import hu.elte.bm.transactionservice.dal.categories.MainCategoryEntity;
import hu.elte.bm.transactionservice.dal.categories.SubCategoryEntity;
import hu.elte.bm.transactionservice.domain.Currency;

@Entity
@Table(name = "outcomes")
public final class OutcomeEntity {

    private static final int TITLE_LENGTH = 50;
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
    private boolean monthly;
    private Date date;
    private Date endDate;
    @Column(length = DESCRIPTION_LENGTH)
    private String description;
    private boolean locked;
    @Column(name = "user_id")
    private Long userId;
    private Double latitude;
    private Double longitude;
    private Long pictureId;

    OutcomeEntity() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(final Long pictureId) {
        this.pictureId = pictureId;
    }
}
