package db.transaction.deadlock.dbspecific.mssql.v2

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import javax.persistence.LockModeType


interface MssqlNbaPlayerJpaRepositoryV2: JpaRepository<NbaPlayer, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    fun findByOrderByBirthdateAsc(pageable: Pageable): List<NbaPlayer>

    @Lock(LockModeType.PESSIMISTIC_READ)
    fun findByOrderByBirthdateDesc(pageable: Pageable): List<NbaPlayer>
}
