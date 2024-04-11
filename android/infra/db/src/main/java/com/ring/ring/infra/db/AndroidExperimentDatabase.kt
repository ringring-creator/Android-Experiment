package com.ring.ring.infra.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
)
abstract class AndroidExperimentDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
