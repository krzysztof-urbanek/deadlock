package db.transaction.deadlock.dbspecific.mysql.v5

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV4
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service


@Service
class MysqlNbaPlayersStatisticsServiceV5(
    private val mysqlNbaPlayerRepository: MysqlNbaPlayerRepositoryV5,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV4 {

    override fun publishYoungestPlayers(number: Int) {
        val youngest = mysqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mysqlNbaPlayerRepository.saveAll(youngest)
    }

    override fun publishOldestPlayers(number: Int) {
        val oldest = mysqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mysqlNbaPlayerRepository.saveAll(oldest)
    }
}
