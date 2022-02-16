package db.transaction.deadlock.dbspecific.mysql.v6

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType


interface MysqlNbaPlayerJpaRepositoryV6: JpaRepository<NbaPlayer, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateAsc(pageable: Pageable): List<NbaPlayer>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateDesc(pageable: Pageable): List<NbaPlayer>

    @Query("SELECT * FROM nba_player FORCE INDEX (birthdate) ORDER BY birthdate ASC LIMIT :number", nativeQuery = true)
    fun findByOrderByBirthdateAscNative(number: Int): List<NbaPlayer>

    @Query("SELECT * FROM nba_player FORCE INDEX (birthdate) ORDER BY birthdate DESC LIMIT :number", nativeQuery = true)
    fun findByOrderByBirthdateDescNative(number: Int): List<NbaPlayer>
}
