package db.transaction.deadlock.dbspecific.mysql.v6

import db.transaction.deadlock.model.NbaPlayer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface MysqlNbaPlayerJpaRepositoryV6: JpaRepository<NbaPlayer, Long> {

    @Query("""
        SELECT np.*, sleep(2) FROM nba_player np FORCE INDEX (birthdate) 
        ORDER BY np.birthdate ASC LIMIT :number for update
        """,
        nativeQuery = true,
    )
    fun findByOrderByBirthdateAscNative(number: Int): List<NbaPlayer>

    @Query("""
        SELECT np.*, sleep(2) FROM nba_player np FORCE INDEX (birthdate) 
        ORDER BY np.birthdate DESC LIMIT :number for update
        """,
        nativeQuery = true,
    )
    fun findByOrderByBirthdateDescNative(number: Int): List<NbaPlayer>
}
