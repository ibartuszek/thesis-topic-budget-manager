package hu.elte.bm.calculationservice.web.schema;

import javax.validation.Valid;

import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;
import hu.elte.bm.calculationservice.web.common.RequestContext;

public final class StatisticsSchemaRequestContext extends RequestContext {

    @Valid
    private StatisticsSchema schema;

    public StatisticsSchema getSchema() {
        return schema;
    }

    public void setSchema(final StatisticsSchema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "TransactionRequestContext{"
            + "userId=" + getUserId()
            + ", schema=" + schema
            + '}';
    }
}
