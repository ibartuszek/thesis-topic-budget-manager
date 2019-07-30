package hu.elte.bm.transactionservice.dal.transaction;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OutcomeRepository extends CrudRepository<OutcomeEntity, Long> {

    @Query("select o from OutcomeEntity o where o.date >= :start and o.date <= :end")
    Iterable<OutcomeEntity> findAll(@Param("start") Date start, @Param("end") Date end);

    Iterable<OutcomeEntity> findByTitle(String title);
}
