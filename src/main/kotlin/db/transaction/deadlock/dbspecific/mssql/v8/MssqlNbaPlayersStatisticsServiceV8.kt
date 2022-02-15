package db.transaction.deadlock.dbspecific.mssql.v8

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV8
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
class MssqlNbaPlayersStatisticsServiceV8(
    private val mssqlNbaPlayerRepository: MssqlNbaPlayerRepositoryV8,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV8 {

    @Transactional("mssqlJpaTransactionManager", isolation = Isolation.REPEATABLE_READ)
    override fun publishYoungestPlayers(number: Int) {
        val youngest = mssqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("mssqlJpaTransactionManager", isolation = Isolation.REPEATABLE_READ)
    override fun publishOldestPlayers(number: Int) {
        val oldest = mssqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(oldest)
    }
}
