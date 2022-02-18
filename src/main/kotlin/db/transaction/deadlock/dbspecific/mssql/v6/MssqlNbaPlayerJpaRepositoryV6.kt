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

    @Query("""
        SELECT TOP (:number) np.*, dbo.forced_timeout(2)
        FROM nba_player np WITH(updlock, rowlock, INDEX(birthdate))
        ORDER BY np.birthdate ASC
        """,
        nativeQuery = true,
    )
    fun findByOrderByBirthdateAscNative(number: Int): List<NbaPlayer>

    @Query("""
        SELECT TOP (:number) np.*, dbo.forced_timeout(2)
        FROM nba_player np WITH(updlock, rowlock, INDEX(birthdate))
        ORDER BY np.birthdate DESC
        """,
        nativeQuery = true,
    )
    fun findByOrderByBirthdateDescNative(number: Int): List<NbaPlayer>
}