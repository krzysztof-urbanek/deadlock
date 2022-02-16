package db.transaction.deadlock.dbspecific.postgresql.v7

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import javax.persistence.LockModeType


interface PostgresqlNbaPlayerJpaRepositoryV7: JpaRepository<NbaPlayer, Long> {

    fun findByOrderByBirthdateAsc(pageable: Pageable): List<NbaPlayer>

    fun findByOrderByBirthdateDesc(pageable: Pageable): List<NbaPlayer>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrdinalId(ordinalId: Long): NbaPlayer
}