package db.transaction.deadlock.dbspecific.mysql.v5

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType


interface MysqlNbaPlayerJpaRepositoryV5: JpaRepository<NbaPlayer, Long> {

    @Transactional("mysqlJpaTransactionManager")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateAsc(pageable: Pageable): List<NbaPlayer>

    @Transactional("mysqlJpaTransactionManager")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateDesc(pageable: Pageable): List<NbaPlayer>
}
