package hu.elte.bm.calculationservice.statistics.chartdata;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = SectorPoint.Builder.class)
public final class SectorPoint {

    private static final int MAXIMUM_LABEL_LENGTH = 50;

    @Positive(message = "Angle must be positive!")
    private final double angle;
    @NotEmpty(message = "Label cannot be empty!")
    @Length(max = MAXIMUM_LABEL_LENGTH, message = "Label must be shorter than 50 characters!")
    private final String label;

    private SectorPoint(final Builder builder) {
        this.angle = builder.angle;
        this.label = builder.label;
    }

    public static Builder builder() {
        return new Builder();
    }

    public double getAngle() {
        return angle;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SectorPoint that = (SectorPoint) o;

        return new EqualsBuilder()
                .append(angle, that.angle)
                .append(label, that.label)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(angle)
                .append(label)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "SectorPoint{"
                + "angle=" + angle
                + ", label='" + label + '\''
                + '}';
    }

    public static final class Builder {

        private double angle;
        private String label;

        private Builder() {
        }

        public Builder withAngle(final double angle) {
            this.angle = angle;
            return this;
        }

        public Builder withLabel(final String label) {
            this.label = label;
            return this;
        }

        public SectorPoint build() {
            return new SectorPoint(this);
        }

    }

}
