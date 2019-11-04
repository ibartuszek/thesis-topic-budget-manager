package hu.elte.bm.calculationservice.web.schema;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.calculationservice.service.StatisticsSchemaService;
import hu.elte.bm.calculationservice.statistics.schema.StatisticsSchema;

@RestController
public class StatisticsSchemaController {

    private static final String APPLICATION_JSON = "application/json";
    private final StatisticsSchemaService service;

    @Value("${schema.schema_has_been_saved:Schema has been saved.}")
    private String schemaHasBeenSaved;

    @Value("${schema.schema_has_been_updated:Schema has been updated.}")
    private String schemaHasBeenUpdated;

    @Value("${schema.schema_has_been_deleted:Schema has been deleted.}")
    private String schemaHasBeenDeleted;

    public StatisticsSchemaController(final StatisticsSchemaService service) {
        this.service = service;
    }

    @RequestMapping(value = "/bm/statistics/schema/findAll", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public StatisticsSchemaListResponse getSchemaList(
        @NotNull @RequestParam(value = "userId") final Long userId) {
        StatisticsSchema standardSchema = service.getStandardSchema(userId);
        List<StatisticsSchema> customSchemas = service.getCustomSchemas(userId);
        return StatisticsSchemaListResponse.createSuccessfulResponse(standardSchema, customSchemas);
    }

    @RequestMapping(value = "/bm/statistics/schema/create", method = RequestMethod.POST, produces = APPLICATION_JSON)
    public StatisticsSchemaResponse createTransaction(@Valid @RequestBody final StatisticsSchemaRequestContext context) {
        StatisticsSchema schema = service.save(context.getSchema(), context.getUserId());
        return StatisticsSchemaResponse.createSuccessfulResponse(schema, schemaHasBeenSaved);
    }

    @RequestMapping(value = "/bm/statistics/schema/update", method = RequestMethod.PUT, produces = APPLICATION_JSON)
    public StatisticsSchemaResponse updateTransaction(@Valid @RequestBody final StatisticsSchemaRequestContext context) {
        StatisticsSchema schema = service.update(context.getSchema(), context.getUserId());
        return StatisticsSchemaResponse.createSuccessfulResponse(schema, schemaHasBeenSaved);
    }

    @RequestMapping(value = "/bm/statistics/schema/delete", method = RequestMethod.DELETE, produces = APPLICATION_JSON)
    public StatisticsSchemaResponse deleteTransaction(@Valid @RequestBody final StatisticsSchemaRequestContext context) {
        StatisticsSchema schema = service.delete(context.getSchema(), context.getUserId());
        return StatisticsSchemaResponse.createSuccessfulResponse(schema, schemaHasBeenSaved);
    }

}
