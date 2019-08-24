package hu.elte.bm.transactionservice.dal.transaction;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OutcomeRepository extends CrudRepository<OutcomeEntity, Long> {

    @Query("select o from OutcomeEntity o where o.date >= :start and o.date <= :end and o.userId = :userId")
    Iterable<OutcomeEntity> findAll(@Param("start") Date start, @Param("end") Date end, @Param("userId") Long userId);

    Optional<OutcomeEntity> findByIdAndUserId(Long id, Long userId);

    Iterable<OutcomeEntity> findByTitleAndUserId(String title, Long userID);
}
