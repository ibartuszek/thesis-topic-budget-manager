package hu.elte.bm.transactionservice.dal.transaction;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IncomeRepository extends CrudRepository<IncomeEntity, Long> {

    @Query("select i from IncomeEntity i where i.userId = :userId "
        + "and (i.date >= :start and i.date <= :end "
        + "or (i.monthly = true and (i.endDate >= :start or i.endDate = null)))")
    Iterable<IncomeEntity> findAll(@Param("start") Date start, @Param("end") Date end, @Param("userId") Long userId);

    @Query("select i from IncomeEntity i where i.userId = :userId "
        + "and (i.date <= :end )")
    Iterable<IncomeEntity> findAll(@Param("end") Date end, @Param("userId") Long userId);

    @Query("select i from IncomeEntity i where i.userId = :userId ")
    Iterable<IncomeEntity> findAll(@Param("userId") Long userId);

    Optional<IncomeEntity> findByIdAndUserId(Long id, Long userId);

    Iterable<IncomeEntity> findByTitleAndUserId(String title, Long userId);
}
