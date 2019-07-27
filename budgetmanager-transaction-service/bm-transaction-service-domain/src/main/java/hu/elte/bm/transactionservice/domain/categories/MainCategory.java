package hu.elte.bm.transactionservice.domain.categories;

import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * MainCategory represents the main category of the transaction (income or outcome).
 */
public final class MainCategory {

    private final Long id;
    @NotBlank
    private final String name;
    @NotNull
    private final CategoryType categoryType;
    @NotNull
    private final Set<SubCategory> subCategorySet;

    private MainCategory(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.categoryType = builder.categoryType;
        this.subCategorySet = builder.subCategorySet;
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

    public Set<SubCategory> getSubCategorySet() {
        return subCategorySet;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MainCategory that = (MainCategory) o;
        return Objects.equals(name, that.name)
            && categoryType == that.categoryType
            && subCategorySet.size() == that.subCategorySet.size()
            && subCategorySet.containsAll(that.subCategorySet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, categoryType, subCategorySet);
    }

    @Override
    public String toString() {
        return "MainCategory{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", categoryType=" + categoryType
            + ", subCategorySet=" + subCategorySet
            + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private CategoryType categoryType;
        private Set<SubCategory> subCategorySet;

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

        public Builder withSubCategorySet(final Set<SubCategory> subCategorySet) {
            this.subCategorySet = subCategorySet;
            return this;
        }

        public MainCategory build() {
            return new MainCategory(this);
        }
    }
}
