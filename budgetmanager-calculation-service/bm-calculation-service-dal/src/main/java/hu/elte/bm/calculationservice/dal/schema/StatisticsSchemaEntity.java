package hu.elte.bm.calculationservice.dal.schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import hu.elte.bm.calculationservice.schema.ChartType;
import hu.elte.bm.calculationservice.schema.StatisticsType;
import hu.elte.bm.transactionservice.Currency;

@Entity
@Table(name = "statistics_schema", uniqueConstraints = { @UniqueConstraint(columnNames = { "title", "user_id" }) })
public final class StatisticsSchemaEntity {

    private static final int MAXIMUM_TITLE_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", length = MAXIMUM_TITLE_LENGTH, nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "statistics_type")
    private StatisticsType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "chart_type")
    private ChartType chartType;

    @Column(name = "main_category_id")
    private Long mainCategoryId;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Column(name = "user_id")
    private Long userId;

    private StatisticsSchemaEntity() {
    }

    private StatisticsSchemaEntity(final Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.type = builder.type;
        this.currency = builder.currency;
        this.chartType = builder.chartType;
        this.mainCategoryId = builder.mainCategoryId;
        this.subCategoryId = builder.subCategoryId;
        this.userId = builder.userId;
    }

    public static Builder builder() {
        return new Builder();
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

    public StatisticsType getType() {
        return type;
    }

    public void setType(final StatisticsType type) {
        this.type = type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(final ChartType chartType) {
        this.chartType = chartType;
    }

    public Long getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(final Long mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(final Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public static final class Builder {
        private Long id;
        private String title;
        private StatisticsType type;
        private Currency currency;
        private ChartType chartType;
        private Long mainCategoryId;
        private Long subCategoryId;
        private Long userId;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder withType(final StatisticsType type) {
            this.type = type;
            return this;
        }

        public Builder withCurrency(final Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder withChartType(final ChartType chartType) {
            this.chartType = chartType;
            return this;
        }

        public Builder withMainCategoryId(final Long mainCategoryId) {
            this.mainCategoryId = mainCategoryId;
            return this;
        }

        public Builder withSubCategoryId(final Long subCategoryId) {
            this.subCategoryId = subCategoryId;
            return this;
        }

        public Builder withUserId(final Long userId) {
            this.userId = userId;
            return this;
        }

        public StatisticsSchemaEntity build() {
            return new StatisticsSchemaEntity(this);
        }

    }
}
