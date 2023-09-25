package com.example.extension

import com.example.commands.CreatePomaAccountCommand
import com.example.commands.PomaManagerCommand
import com.github.ya0igoddess.dbsync.config.settings.KordDBSettings
import com.github.ya0igoddess.dbsync.database.SkaardModuleDatabase
import com.github.ya0igoddess.dbsync.migration.loadLiquibase
import com.example.config.PomaModule
import com.example.database.tables
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import org.koin.core.component.inject

class PomaManagerExt: Extension() {

    companion object {
        const val code = "poma_module"
    }

    override val name: String
        get() = code

    override suspend fun setup() {
        getKoin().loadModules(listOf(PomaModule))

        val databaseModule: SkaardModuleDatabase by inject()
        databaseModule.addTables(tables)
        val settings: KordDBSettings by inject()
        loadLiquibase(settings.jdbc!!, name, "changelog/poma-manager/main-changelog.xml")

        ephemeralSlashCommand(inject<CreatePomaAccountCommand>().value.builder)
        ephemeralSlashCommand(inject<PomaManagerCommand>().value.builder)
    }

}