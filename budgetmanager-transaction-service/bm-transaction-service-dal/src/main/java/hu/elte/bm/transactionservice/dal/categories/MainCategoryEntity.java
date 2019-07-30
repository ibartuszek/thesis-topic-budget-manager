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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Entity
@Table(name = "main_category",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "transaction_type" }) })
public final class MainCategoryEntity {

    private static final int MAXIMUM_NAME_LENGTH = 50;

    @Column(name = "name", length = MAXIMUM_NAME_LENGTH, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = SubCategoryEntity.class)
    @JoinTable(name = "category_join_table",
        joinColumns = @JoinColumn(name = "main_category_id"),
        inverseJoinColumns = @JoinColumn(name = "sub_category_id"))
    private Set<SubCategoryEntity> subCategoryEntitySet;

    public MainCategoryEntity() {
    }

    public MainCategoryEntity(final String name, final TransactionType transactionType,
        final Set<SubCategoryEntity> subCategoryEntitySet) {
        this(null, name, transactionType, subCategoryEntitySet);
    }

    public MainCategoryEntity(final Long id, final String name, final TransactionType transactionType,
        final Set<SubCategoryEntity> subCategoryEntitySet) {
        this.id = id;
        this.name = name;
        this.transactionType = transactionType;
        this.subCategoryEntitySet = subCategoryEntitySet;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(final TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Set<SubCategoryEntity> getSubCategoryEntitySet() {
        return subCategoryEntitySet;
    }

    public void setSubCategoryEntitySet(final Set<SubCategoryEntity> subCategoryEntitySet) {
        this.subCategoryEntitySet = subCategoryEntitySet;
    }

}
