package hu.elte.bm.calculationservice.web.statistics;

import java.text.MessageFormat;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.bm.calculationservice.service.statistics.StatisticsFactory;
import hu.elte.bm.calculationservice.statistics.Statistics;

@RestController
public class StatisticsController {

    private static final String APPLICATION_JSON = "application/json";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private final StatisticsFactory statisticsFactory;

    @Value("${statistics.standard_statistics_successfully_created:Standard statistics successfully created.}")
    private String createStandardStatistics;

    @Value("${statistics.custom_statistics_successfully_created:Custom statistics:{0} successfully created.}")
    private String createCustomStatistics;

    public StatisticsController(final StatisticsFactory statisticsFactory) {
        this.statisticsFactory = statisticsFactory;
    }

    @RequestMapping(value = "/bm/statistics/getStandardStatistics", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public StatisticsResponse getStandardStatistics(
        @NotNull @RequestParam(value = "userId") final Long userId,
        @NotNull @RequestParam(value = "startDate") @DateTimeFormat(pattern = DATE_PATTERN) final LocalDate start,
        @NotNull @RequestParam(value = "endDate") @DateTimeFormat(pattern = DATE_PATTERN) final LocalDate end) {
        Statistics standardStatistics = statisticsFactory.createStandardStatistics(userId, start, end);
        return StatisticsResponse.createSuccessfulResponse(standardStatistics, createStandardStatistics);
    }

    @RequestMapping(value = "/bm/statistics/getCustomStatistics", method = RequestMethod.GET, produces = APPLICATION_JSON)
    public StatisticsResponse getCustomStatistics(
        @NotNull @RequestParam(value = "userId") final Long userId,
        @NotNull @RequestParam(value = "schemaId") final Long schemaId,
        @NotNull @RequestParam(value = "startDate") @DateTimeFormat(pattern = DATE_PATTERN) final LocalDate start,
        @NotNull @RequestParam(value = "endDate") @DateTimeFormat(pattern = DATE_PATTERN) final LocalDate end) {
        Statistics customStatistics = statisticsFactory.createCustomStatistics(userId, schemaId, start, end);
        return StatisticsResponse.createSuccessfulResponse(customStatistics, MessageFormat.format(createCustomStatistics, schemaId));
    }

}
