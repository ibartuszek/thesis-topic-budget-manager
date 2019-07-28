package hu.elte.bm.transactionservice.dal.categories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;

@Entity
@Table(name = "sub_category",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "category_type" }) })
public final class SubCategoryEntity {

    private static final int MAXIMUM_NAME_LENGTH = 50;

    @Column(name = "name", length = MAXIMUM_NAME_LENGTH, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private CategoryType categoryType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private SubCategoryEntity() {
    }

    private SubCategoryEntity(final Builder builder) {
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

        public SubCategoryEntity build() {
            return new SubCategoryEntity(this);
        }
    }
}
