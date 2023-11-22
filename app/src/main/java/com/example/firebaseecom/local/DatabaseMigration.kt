package com.example.firebaseecom.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
 object DatabaseMigration {
     val MIGRATIONS: Array<Migration>
         get() = arrayOf<Migration>(
             migration1to2()
         )

     private fun migration1to2(): Migration = object : Migration(1,2){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE ProductTable RENAME COLUMN productTitle TO productTitleEn")
            db.execSQL("ALTER TABLE ProductTable ADD COLUMN productTitleMl String")
        }
    }
}