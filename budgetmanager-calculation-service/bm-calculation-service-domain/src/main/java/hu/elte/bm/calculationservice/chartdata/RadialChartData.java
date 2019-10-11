package hu.elte.bm.calculationservice.chartdata;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = RadialChartData.Builder.class)
public final class RadialChartData implements ChartData {

    private static final int MAXIMUM_LEGEND_LENGTH = 20;

    @NotEmpty(message = "Legend cannot be empty!")
    @Length(max = MAXIMUM_LEGEND_LENGTH, message = "Legend must be shorter than 20 characters!")
    private final String legend;
    @NotEmpty
    @Valid
    private final List<SectorPoint> sectorPoints;

    private RadialChartData(final Builder builder) {
        this.legend = builder.legend;
        this.sectorPoints = builder.sectorPoints;
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
        return sectorPoints.toArray();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RadialChartData that = (RadialChartData) o;

        return new EqualsBuilder()
                .append(legend, that.legend)
                .append(sectorPoints, that.sectorPoints)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(legend)
                .append(sectorPoints)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RadialChartData{"
                + "legend='" + legend + '\''
                + ", sectorPoints=" + sectorPoints
                + '}';
    }

    public static final class Builder {

        private String legend;
        private List<SectorPoint> sectorPoints;

        private Builder() {
        }

        public Builder withLegend(final String legend) {
            this.legend = legend;
            return this;
        }

        public Builder withSectorPoints(final List<SectorPoint> sectorPoints) {
            this.sectorPoints = sectorPoints;
            return this;
        }

        public RadialChartData build() {
            return new RadialChartData(this);
        }

    }

}
