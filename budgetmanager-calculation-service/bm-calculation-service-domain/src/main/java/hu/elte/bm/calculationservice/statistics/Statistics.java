package hu.elte.bm.calculationservice.statistics;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import hu.elte.bm.calculationservice.budgetdetails.BudgetDetails;
import hu.elte.bm.calculationservice.chartdata.ChartData;
import hu.elte.bm.calculationservice.schema.StatisticsSchema;

@JsonDeserialize(builder = Statistics.Builder.class)
public final class Statistics {

    @Past
    private final LocalDate startDate;
    @PastOrPresent
    private final LocalDate endDate;
    @Valid
    private final StatisticsSchema schema;
    @Valid
    private final ChartData chartData;
    @Valid
    private final BudgetDetails budgetDetails;

    private Statistics(final Builder builder) {
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.schema = builder.schema;
        this.chartData = builder.chartData;
        this.budgetDetails = builder.budgetDetails;
    }

    public static Builder builder() {
        return new Builder();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public StatisticsSchema getSchema() {
        return schema;
    }

    public ChartData getChartData() {
        return chartData;
    }

    public BudgetDetails getBudgetDetails() {
        return budgetDetails;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Statistics that = (Statistics) o;
        return startDate.equals(that.startDate)
            && endDate.equals(that.endDate)
            && schema.equals(that.schema)
            && chartData.equals(that.chartData)
            && budgetDetails.equals(that.budgetDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, schema, chartData, budgetDetails);
    }

    @Override
    public String toString() {
        return "Statistics{"
            + "startDate=" + startDate
            + ", endDate=" + endDate
            + ", schema=" + schema
            + ", chartData=" + chartData
            + ", budgetDetails=" + budgetDetails
            + '}';
    }

    public static final class Builder {

        private LocalDate startDate;
        private LocalDate endDate;
        private StatisticsSchema schema;
        private ChartData chartData;
        private BudgetDetails budgetDetails;

        private Builder() {
        }

        public Builder withStartDate(final LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(final LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder withSchema(final StatisticsSchema schema) {
            this.schema = schema;
            return this;
        }

        public Builder withChartData(final ChartData chartData) {
            this.chartData = chartData;
            return this;
        }

        public Builder withBudgetDetails(final BudgetDetails budgetDetails) {
            this.budgetDetails = budgetDetails;
            return this;
        }

        public Statistics build() {
            return new Statistics(this);
        }
    }
}
