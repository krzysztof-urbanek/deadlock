package db.transaction.deadlock.dbspecific.mssql.v6

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType


interface MssqlNbaPlayerJpaRepositoryV6: JpaRepository<NbaPlayer, Long> {

    @Query("SELECT TOP (:number) ordinal_id FROM nba_player with(nolock) ORDER BY birthdate ASC", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateAsc(number: Int): List<Long>

    @Query("SELECT TOP (:number) ordinal_id FROM nba_player with(nolock) ORDER BY birthdate DESC", nativeQuery = true)
    fun findOrdinalIdByOrderByBirthdateDesc(number: Int): List<Long>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrdinalId(ordinalId: Long): NbaPlayer
}
