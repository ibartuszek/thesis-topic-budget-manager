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

    private MainCategory(final MainCategoryBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.categoryType = builder.categoryType;
        this.subCategorySet = builder.subCategorySet;
    }

    public static MainCategoryBuilder builder() {
        return new MainCategoryBuilder();
    }

    public static MainCategoryBuilder builder(final MainCategory mainCategory) {
        return new MainCategoryBuilder(mainCategory);
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

        private MainCategoryBuilder(final MainCategory mainCategory) {
            this.id = mainCategory.id;
            this.name = mainCategory.name;
            this.categoryType = mainCategory.categoryType;
            this.subCategorySet = mainCategory.subCategorySet;
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
