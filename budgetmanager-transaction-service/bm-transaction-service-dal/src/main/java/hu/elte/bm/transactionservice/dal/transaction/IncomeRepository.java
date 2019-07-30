package hu.elte.bm.transactionservice.dal.transaction;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IncomeRepository extends CrudRepository<IncomeEntity, Long> {

    @Query("select i from IncomeEntity i where i.date >= :start and i.date <= :end")
    Iterable<IncomeEntity> findAll(@Param("start") Date start, @Param("end") Date end);

    Iterable<IncomeEntity> findByTitle(String title);
}
