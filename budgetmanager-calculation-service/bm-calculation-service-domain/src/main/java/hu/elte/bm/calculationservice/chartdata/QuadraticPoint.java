package hu.elte.bm.calculationservice.chartdata;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = QuadraticPoint.Builder.class)
public final class QuadraticPoint {

    private static final int MAXIMUM_LABEL_LENGTH = 50;

    private final int x;
    private final double y;
    @NotEmpty(message = "Label cannot be empty!")
    @Length(max = MAXIMUM_LABEL_LENGTH, message = "Label must be shorter than 50 characters!")
    private final String label;
    @PastOrPresent(message = "Date must be in the past!")
    private final LocalDate date;

    private QuadraticPoint(final Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.label = builder.label;
        this.date = builder.date;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getLabel() {
        return label;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuadraticPoint that = (QuadraticPoint) o;

        return new EqualsBuilder()
            .append(x, that.x)
            .append(y, that.y)
            .append(label, that.label)
            .append(date, that.date)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(x)
            .append(y)
            .append(label)
            .append(date)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "SectorPoint{"
            + "x=" + x
            + ", y=" + y
            + ", label='" + label + '\''
            + ", date='" + date + '\''
            + '}';
    }

    public static final class Builder {

        private int x;
        private double y;
        private String label;
        private LocalDate date;

        private Builder() {
        }

        public Builder withX(final int x) {
            this.x = x;
            return this;
        }

        public Builder withY(final double y) {
            this.y = y;
            return this;
        }

        public Builder withLabel(final String label) {
            this.label = label;
            return this;
        }

        public Builder withDate(final LocalDate date) {
            this.date = date;
            return this;
        }

        public QuadraticPoint build() {
            return new QuadraticPoint(this);
        }

    }

}
