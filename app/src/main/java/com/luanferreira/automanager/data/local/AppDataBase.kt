package com.luanferreira.automanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luanferreira.automanager.data.local.Dao.VeiculoDao
import com.luanferreira.automanager.data.model.Veiculo

@Database(entities = [Veiculo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun veiculoDao(): VeiculoDao
}