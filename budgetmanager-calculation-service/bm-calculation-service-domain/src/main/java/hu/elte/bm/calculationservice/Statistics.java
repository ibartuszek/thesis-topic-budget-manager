package hu.elte.bm.calculationservice;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;

import hu.elte.bm.calculationservice.chartdata.ChartData;

public final class Statistics {

    @Past
    private final LocalDate startDate;
    @PastOrPresent
    private final LocalDate endDate;
    @Valid
    private final StatisticsSchema schema;
    @Valid
    private final ChartData chartData;

    private Statistics(final Builder builder) {
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.schema = builder.schema;
        this.chartData = builder.chartData;
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

    @Override
    public String toString() {
        return "Statistics{"
                + "startDate=" + startDate
                + ", endDate=" + endDate
                + ", schema=" + schema
                + ", chartData=" + chartData
                + '}';
    }

    public static final class Builder {

        private LocalDate startDate;
        private LocalDate endDate;
        private StatisticsSchema schema;
        private ChartData chartData;

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

        public Statistics build() {
            return new Statistics(this);
        }
    }
}
