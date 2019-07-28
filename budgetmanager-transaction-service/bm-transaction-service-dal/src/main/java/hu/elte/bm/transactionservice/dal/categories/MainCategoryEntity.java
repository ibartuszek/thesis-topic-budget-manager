package hu.elte.bm.transactionservice.dal.categories;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import hu.elte.bm.transactionservice.domain.categories.CategoryType;

@Entity
@Table(name = "main_category",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "category_type" }) })
public final class MainCategoryEntity {

    private static final int MAXIMUM_NAME_LENGTH = 50;

    @Column(name = "name", length = MAXIMUM_NAME_LENGTH, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type")
    private CategoryType categoryType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = SubCategoryEntity.class)
    @JoinTable(name = "category_join_table",
        joinColumns = @JoinColumn(name = "main_category_id"),
        inverseJoinColumns = @JoinColumn(name = "sub_category_id"))
    private Set<SubCategoryEntity> subCategoryEntitySet;

    private MainCategoryEntity() {
    }

    private MainCategoryEntity(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.categoryType = builder.categoryType;
        this.subCategoryEntitySet = builder.subCategoryEntitySet;
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

    public Set<SubCategoryEntity> getSubCategoryEntitySet() {
        return subCategoryEntitySet;
    }

    public static final class Builder {
        private Long id;
        private String name;
        private CategoryType categoryType;
        private Set<SubCategoryEntity> subCategoryEntitySet;

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

        public Builder withSubCategoryEntitySet(final Set<SubCategoryEntity> subCategoryEntitySet) {
            this.subCategoryEntitySet = subCategoryEntitySet;
            return this;
        }

        public MainCategoryEntity build() {
            return new MainCategoryEntity(this);
        }
    }
}
