package latproject.com.roomtutorial

import android.arch.persistence.room.*
import latproject.com.myfinance.core.room.BankTransaction

@Dao
interface BankTransactionDao {
    @Insert
    fun addTransaction(bankTransaction: BankTransaction)

    @Query("select * from BankTransactions")
    fun getTransactions(): List<BankTransaction>

    @Delete
    fun deleteTransaction(bankTransaction: BankTransaction)

    @Update
    fun updateTransaction(bankTransaction: BankTransaction)

    @Query("select * from BankTransactions where bank = :bankName")
    fun getTransactionsForBank(bankName: String): List<BankTransaction>

}