package latproject.com.myfinance.core.room

import android.arch.persistence.room.*

@Dao
interface BudgetDao {
    @Insert
    fun createBudget(budget: Budget)

    @Query("select * from Budget")
    fun getAllBudgets(): List<Budget>

    @Delete
    fun deleteBudget(budget: Budget)

    @Query("select * from Budget WHERE bank = :bankName")
    fun getBudgetForABank(bankName: String): List<Budget>

    @Update
    fun updateBudget(budget: Budget)
}