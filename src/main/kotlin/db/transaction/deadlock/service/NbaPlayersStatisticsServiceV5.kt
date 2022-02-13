package db.transaction.deadlock.service


interface NbaPlayersStatisticsServiceV5 {

    fun publishYoungestPlayers(number: Int)

    fun publishOldestPlayers(number: Int)
}