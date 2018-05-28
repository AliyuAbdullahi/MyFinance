package latproject.com.roomtutorial

import android.arch.persistence.room.*

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
}