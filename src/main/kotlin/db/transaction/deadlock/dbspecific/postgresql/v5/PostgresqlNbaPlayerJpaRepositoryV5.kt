package db.transaction.deadlock.dbspecific.postgresql.v5

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType


interface PostgresqlNbaPlayerJpaRepositoryV5: JpaRepository<NbaPlayer, Long> {

    @Transactional("postgresqlJpaTransactionManager")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateAsc(pageable: Pageable): List<NbaPlayer>

    @Transactional("postgresqlJpaTransactionManager")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateDesc(pageable: Pageable): List<NbaPlayer>
}