package db.transaction.deadlock.dbspecific.postgresql.v6

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.*
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType
import javax.persistence.QueryHint


interface PostgresqlNbaPlayerJpaRepositoryV6: JpaRepository<NbaPlayer, Long> {

    @Query("""
        /*+ IndexScan(nba_player) */
        SELECT np.*, pg_sleep(2) FROM nba_player np ORDER BY np.birthdate ASC LIMIT :number for update
        """,
        nativeQuery = true
    )
    fun findByOrderByBirthdateAscNative(number: Int): List<NbaPlayer>

    @Query("""
        /*+ IndexScan(nba_player) */
        SELECT np.*, pg_sleep(2) FROM nba_player np ORDER BY np.birthdate DESC LIMIT :number for update
        """,
        nativeQuery = true
    )
    fun findByOrderByBirthdateDescNative(number: Int): List<NbaPlayer>
}