package hu.elte.bm.authenticationservice.dal;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InvalidTokenRepository extends CrudRepository<InvalidTokenEntity, Long> {

    @Query("select t from InvalidTokenEntity t where t.invalidationDate < :date")
    Iterable<InvalidTokenEntity> findExpiredTokens(@Param("date") Date date);

}
