package hu.elte.bm.calculationservice.schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import hu.elte.bm.transactionservice.Currency;
import hu.elte.bm.transactionservice.MainCategory;
import hu.elte.bm.transactionservice.SubCategory;

@JsonDeserialize(builder = StatisticsSchema.Builder.class)
public final class StatisticsSchema {

    private static final int MAXIMUM_TITLE_LENGTH = 20;

    private final Long id;
    @NotEmpty(message = "Title cannot be empty!")
    @Length(max = MAXIMUM_TITLE_LENGTH, message = "Title must be shorter than 20 characters!")
    private final String title;
    @NotNull(message = "Types cannot be null!")
    private final StatisticsType type;
    @NotNull(message = "Currency cannot be null!")
    private final Currency currency;
    @NotNull(message = "ChartType cannot be null!")
    private final ChartType chartType;
    private final MainCategory mainCategory;
    private final SubCategory subCategory;

    private StatisticsSchema(final Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.type = builder.type;
        this.currency = builder.currency;
        this.chartType = builder.chartType;
        this.mainCategory = builder.mainCategory;
        this.subCategory = builder.subCategory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public StatisticsType getType() {
        return type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StatisticsSchema that = (StatisticsSchema) o;

        return new EqualsBuilder()
            .append(title, that.title)
            .append(type, that.type)
            .append(currency, that.currency)
            .append(chartType, that.chartType)
            .append(mainCategory, that.mainCategory)
            .append(subCategory, that.subCategory)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(title)
            .append(type)
            .append(currency)
            .append(chartType)
            .append(mainCategory)
            .append(subCategory)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "StatisticsSchema{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", type=" + type
            + ", currency=" + currency
            + ", chartType=" + chartType
            + ", mainCategory=" + mainCategory
            + ", subCategory=" + subCategory
            + '}';
    }

    public static final class Builder {

        private Long id;
        private String title;
        private StatisticsType type;
        private Currency currency;
        private ChartType chartType;
        private MainCategory mainCategory;
        private SubCategory subCategory;

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

        public Builder withMainCategory(final MainCategory mainCategory) {
            this.mainCategory = mainCategory;
            return this;
        }

        public Builder withSubCategory(final SubCategory subCategory) {
            this.subCategory = subCategory;
            return this;
        }

        public StatisticsSchema build() {
            return new StatisticsSchema(this);
        }

    }
}
