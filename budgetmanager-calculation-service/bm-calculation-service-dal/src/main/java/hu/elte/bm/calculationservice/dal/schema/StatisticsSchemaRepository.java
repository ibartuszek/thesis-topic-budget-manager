package hu.elte.bm.calculationservice.dal.schema;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface StatisticsSchemaRepository extends CrudRepository<StatisticsSchemaEntity, Long> {

    @Query("select s from StatisticsSchemaEntity s "
            + "where s.userId = ?1")
    Iterable<StatisticsSchemaEntity> findAllSubcategory(Long userId);

    Optional<StatisticsSchemaEntity> findByIdAndUserId(Long id, Long userId);

    @Query("select s from StatisticsSchemaEntity s "
            + "where s.title = ?1 and s.userId = ?2")
    Optional<StatisticsSchemaEntity> findByTitle(String title, Long userId);

}
