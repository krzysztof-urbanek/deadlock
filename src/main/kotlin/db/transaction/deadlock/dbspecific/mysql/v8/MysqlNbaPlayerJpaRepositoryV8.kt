package db.transaction.deadlock.dbspecific.mysql.v8

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository


interface MysqlNbaPlayerJpaRepositoryV8: JpaRepository<NbaPlayer, Long> {

    fun findByOrderByBirthdateAsc(pageable: Pageable): List<NbaPlayer>

    fun findByOrderByBirthdateDesc(pageable: Pageable): List<NbaPlayer>
}
