package latproject.com.myfinance.core.model.modelparser

import latproject.com.myfinance.core.model.Bank
import latproject.com.myfinance.core.model.RealmBank

class ModelMapper {
    companion object {

        fun map(bank: Bank) : RealmBank {
            val realmBank = RealmBank()
            realmBank.name = bank.name
            realmBank.backgroundColor = bank.backgroundColor
            realmBank.logo = bank.logo
            realmBank.textColor = bank.textColor

            return realmBank
        }

        fun map(banks: List<Bank>): List<RealmBank> {
            val realmBanks = arrayListOf<RealmBank>()
            banks.forEach {
                realmBanks.add(map(it))
            }

            return realmBanks
        }
    }
}