package hu.elte.bm.transactionservice.domain.categories;

import java.util.Objects;

/**
 * It represents the secondary category of the transaction (can be income or outcome).
 */
public final class SubCategory {

    private final Long id;
    private final String name;
    private final CategoryType categoryType;

    private SubCategory(final SubCategoryBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.categoryType = builder.categoryType;
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

    public static final class SubCategoryBuilder {
        private Long id;
        private String name;
        private CategoryType categoryType;

        private SubCategoryBuilder() {
        }

        public static SubCategoryBuilder newInstance() {
            return new SubCategoryBuilder();
        }

        public static SubCategoryBuilder newInstance(final SubCategory subCategory) {
            SubCategoryBuilder builder = new SubCategoryBuilder();
            builder.id = subCategory.id;
            builder.name = subCategory.name;
            builder.categoryType = subCategory.categoryType;
            return builder;
        }

        public SubCategoryBuilder withId(final Long id) {
            this.id = id;
            return this;
        }

        public SubCategoryBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public SubCategoryBuilder withCategoryType(final CategoryType categoryType) {
            this.categoryType = categoryType;
            return this;
        }

        public SubCategory build() {
            return new SubCategory(this);
        }
    }
}
