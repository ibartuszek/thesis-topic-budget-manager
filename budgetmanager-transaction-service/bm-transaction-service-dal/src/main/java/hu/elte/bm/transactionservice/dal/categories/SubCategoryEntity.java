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

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;

@Entity
@Table(name = "sub_category",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "transaction_type" }) })
public final class SubCategoryEntity {

    private static final int MAXIMUM_NAME_LENGTH = 50;

    @Column(name = "name", length = MAXIMUM_NAME_LENGTH, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public SubCategoryEntity() {
    }

    public SubCategoryEntity(final String name, final TransactionType transactionType) {
        this(null, name, transactionType);
    }

    public SubCategoryEntity(final Long id, final String name, final TransactionType transactionType) {
        this.id = id;
        this.name = name;
        this.transactionType = transactionType;
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

}
