package db.transaction.deadlock.service


interface NbaPlayersStatisticsServiceV7 {

    fun publishYoungestPlayers(number: Int)

    fun publishOldestPlayers(number: Int)
}