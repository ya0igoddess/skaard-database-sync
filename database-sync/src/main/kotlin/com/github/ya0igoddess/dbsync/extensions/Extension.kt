package com.github.ya0igoddess.dbsync.extensions

import com.github.ya0igoddess.dbsync.config.DBSyncModule
import com.github.ya0igoddess.dbsync.config.settings.KordDBSettings
import com.github.ya0igoddess.dbsync.migration.loadLiquibase
import com.github.ya0igoddess.dbsync.repositories.IDiscordGuildRepoService
import com.github.ya0igoddess.dbsync.repositories.IDiscordMemberRepoService
import com.github.ya0igoddess.dbsync.repositories.IDiscordUserRepoService
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.guild.MemberJoinEvent
import org.koin.core.component.inject


class DBSyncExtension: Extension() {

    companion object {
        const val code = "kord_db_sync"
    }

    override val name: String = code

    override suspend fun setup() {
        getKoin().loadModules(listOf(DBSyncModule))

        val userService: IDiscordUserRepoService by inject()
        val guildService: IDiscordGuildRepoService by inject()
        val memberService: IDiscordMemberRepoService by inject()
        val settings: KordDBSettings by inject()

        loadLiquibase(settings.jdbc!!, name, "db/changelog/kord-db-sync/main-changelog.xml")

        event<MemberJoinEvent> {
            action {
                memberService.createFromExternalEntity(event.member)
            }
        }

        event<GuildCreateEvent> {
            action {
                guildService.getOrCreateFromExternal(event.guild)
                event.guild.members.collect(memberService::getOrCreateFromExternal)
            }
        }
    }
}