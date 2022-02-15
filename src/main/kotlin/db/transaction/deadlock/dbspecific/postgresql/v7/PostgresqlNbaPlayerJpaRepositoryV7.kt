package db.transaction.deadlock.dbspecific.postgresql.v7

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType


interface PostgresqlNbaPlayerJpaRepositoryV7: JpaRepository<NbaPlayer, Long> {

    /**
     * Selecting only ordinalId to prevent caching
     */
    @Query("SELECT ordinal_id FROM nba_player ORDER BY birthdate ASC LIMIT :number", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateAsc(number: Int): List<Long>

    /**
     * Selecting only ordinalId to prevent caching
     */
    @Query("SELECT ordinal_id FROM nba_player ORDER BY birthdate DESC LIMIT :number", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateDesc(number: Int): List<Long>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrdinalId(ordinalId: Long): NbaPlayer
}