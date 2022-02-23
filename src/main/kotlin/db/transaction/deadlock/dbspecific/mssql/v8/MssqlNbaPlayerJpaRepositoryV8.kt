package db.transaction.deadlock.dbspecific.mssql.v8

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType


interface MssqlNbaPlayerJpaRepositoryV8: JpaRepository<NbaPlayer, Long> {

    /**
     * Selecting only ordinalId to prevent caching
     */
    @Query("SELECT TOP (:number) ordinal_id FROM nba_player with(nolock) ORDER BY birthdate ASC", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateAsc(number: Int): List<Long>

    /**
     * Selecting only ordinalId to prevent caching
     */
    @Query("SELECT TOP (:number) ordinal_id FROM nba_player with(nolock) ORDER BY birthdate DESC", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateDesc(number: Int): List<Long>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrdinalId(ordinalId: Long): NbaPlayer
}
