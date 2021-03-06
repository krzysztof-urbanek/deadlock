package db.transaction.deadlock.dbspecific.mysql.v8

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType


interface MysqlNbaPlayerJpaRepositoryV8: JpaRepository<NbaPlayer, Long> {

    /**
     * Selecting only ordinalId to prevent caching
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW)
    @Query("SELECT ordinal_id FROM nba_player ORDER BY birthdate ASC LIMIT :number", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateAsc(number: Int): List<Long>

    /**
     * Selecting only ordinalId to prevent caching
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW)
    @Query("SELECT ordinal_id FROM nba_player ORDER BY birthdate DESC LIMIT :number", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateDesc(number: Int): List<Long>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrdinalId(ordinalId: Long): NbaPlayer
}
