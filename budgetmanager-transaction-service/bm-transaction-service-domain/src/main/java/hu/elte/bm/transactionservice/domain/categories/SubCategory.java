package hu.elte.bm.transactionservice.domain.categories;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * It represents the secondary category of the transaction (can be income or outcome).
 */
public final class SubCategory {

    private final Long id;
    @NotBlank
    private final String name;
    @NotNull
    private final CategoryType categoryType;

    private SubCategory(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.categoryType = builder.categoryType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubCategory that = (SubCategory) o;
        return Objects.equals(name, that.name)
            && categoryType == that.categoryType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, categoryType);
    }

    @Override
    public String toString() {
        return "SubCategory{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", categoryType=" + categoryType
            + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private CategoryType categoryType;

        private Builder() {
        }

        public Builder withId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withCategoryType(final CategoryType categoryType) {
            this.categoryType = categoryType;
            return this;
        }

        public SubCategory build() {
            return new SubCategory(this);
        }
    }
}
