package com.ramyres.tripplannerbr.data

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(
    entities = [
        Origem::class,
        Destino::class,
        Contato::class,
        Passeio::class,
        Usuario::class
    ],
    version = 1
)
@ColumnTypeConverters(Converters::class)
abstract class BancoDeDados : RoomDatabase() {
    abstract fun contatoDao(): ContatoDao
    abstract fun destinoDao(): DestinoDao
    abstract fun origemDao(): OrigemDao
    abstract fun passeioDao(): PasseioDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: BancoDeDados? = null

        fun getDatabase(context: Context): BancoDeDados {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BancoDeDados::class.java,
                    "trip_planner_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}