package db.transaction.deadlock.dbspecific.mysql.v4

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType


interface MysqlNbaPlayerJpaRepositoryV4: JpaRepository<NbaPlayer, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateAsc(pageable: Pageable): List<NbaPlayer>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByOrderByBirthdateDesc(pageable: Pageable): List<NbaPlayer>
}
