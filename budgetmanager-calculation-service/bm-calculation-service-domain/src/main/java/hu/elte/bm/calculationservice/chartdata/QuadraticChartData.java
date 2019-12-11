package hu.elte.bm.calculationservice.chartdata;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = QuadraticChartData.Builder.class)
public final class QuadraticChartData implements ChartData {

    private static final int MAXIMUM_LEGEND_LENGTH = 20;

    @NotEmpty(message = "Legend cannot be empty!")
    @Length(max = MAXIMUM_LEGEND_LENGTH, message = "Legend must be shorter than 20 characters!")
    private final String legend;
    @NotEmpty
    @Valid
    private final List<QuadraticPoint> dataPoints;

    private QuadraticChartData(final Builder builder) {
        this.legend = builder.legend;
        this.dataPoints = builder.dataPoints;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getLegend() {
        return legend;
    }

    @Override
    public Object[] getDataPoints() {
        return dataPoints.toArray();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuadraticChartData that = (QuadraticChartData) o;

        return new EqualsBuilder()
            .append(legend, that.legend)
            .append(dataPoints, that.dataPoints)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(legend)
            .append(dataPoints)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "QuadraticChartData{"
            + "legend='" + legend + '\''
            + ", dataPoints=" + dataPoints
            + '}';
    }

    public static final class Builder {

        private String legend;
        private List<QuadraticPoint> dataPoints;

        private Builder() {
        }

        public Builder withLegend(final String legend) {
            this.legend = legend;
            return this;
        }

        public Builder withDataPoints(final List<QuadraticPoint> dataPoints) {
            this.dataPoints = dataPoints;
            return this;
        }

        public QuadraticChartData build() {
            return new QuadraticChartData(this);
        }

    }

}
