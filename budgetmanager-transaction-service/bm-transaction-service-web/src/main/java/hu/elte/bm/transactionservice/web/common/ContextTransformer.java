package hu.elte.bm.transactionservice.web.common;

import org.springframework.stereotype.Component;

import hu.elte.bm.transactionservice.domain.transaction.TransactionType;
import hu.elte.bm.transactionservice.service.transaction.TransactionContext;

@Component
public class ContextTransformer {

    public TransactionContext transform(final RequestContext context) {
        return this.transform(context.getTransactionType(), context.getUserId());
    }

    public TransactionContext transform(final TransactionType transactionType, final Long userId) {
        return TransactionContext.builder()
            .withTransactionType(transactionType)
            .withUserId(userId)
            .build();
    }

}
