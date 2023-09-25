package com.example.config

import com.github.ya0igoddess.dbsync.config.DBSyncModule
import org.koin.dsl.module

val PomaModule = module {
    includes(DBSyncModule, serviceModule, handlerModule, commandsModule)
}