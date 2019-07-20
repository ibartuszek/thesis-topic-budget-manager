package hu.elte.bm.transactionservice.domain.categories;

import java.util.Objects;
import java.util.Set;

public final class MainCategory {
    private final Long id;
    private final String name;
    private final CategoryType categoryType;
    private final Set<SubCategory> subCategorySet;

    private MainCategory(final MainCategoryBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.categoryType = builder.categoryType;
        this.subCategorySet = builder.subCategorySet;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MainCategory that = (MainCategory) o;
        return Objects.equals(name, that.name)
            && categoryType == that.categoryType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, categoryType);
    }

    public static final class MainCategoryBuilder {
        private Long id;
        private String name;
        private CategoryType categoryType;
        private Set<SubCategory> subCategorySet;

        private MainCategoryBuilder() {
        }

        public static MainCategoryBuilder newInstance() {
            return new MainCategoryBuilder();
        }

        public static MainCategoryBuilder newInstance(final MainCategory mainCategory) {
            MainCategoryBuilder builder = new MainCategoryBuilder();
            builder.withId(mainCategory.id);
            builder.withName(mainCategory.name);
            builder.withCategoryType(mainCategory.categoryType);
            builder.withSubCategorySet(mainCategory.subCategorySet);
            return builder;
        }

        public MainCategoryBuilder withId(final Long id) {
            this.id = id;
            return this;
        }

        public MainCategoryBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public MainCategoryBuilder withCategoryType(final CategoryType categoryType) {
            this.categoryType = categoryType;
            return this;
        }

        public MainCategoryBuilder withSubCategorySet(final Set<SubCategory> subCategorySet) {
            this.subCategorySet = subCategorySet;
            return this;
        }

        public MainCategory build() {
            return new MainCategory(this);
        }
    }
}
