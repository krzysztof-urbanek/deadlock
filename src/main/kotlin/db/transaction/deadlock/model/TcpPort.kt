package db.transaction.deadlock.model

import javax.persistence.*

@Entity
@Table
data class TcpPort(
    @Column @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val ordinalId: Long? = null,
    @Column val portNumber: Int,
    @Column val description: String,
)
