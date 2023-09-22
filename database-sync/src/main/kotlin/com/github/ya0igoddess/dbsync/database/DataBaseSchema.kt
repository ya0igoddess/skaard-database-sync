package com.github.ya0igoddess.dbsync.database

import com.github.ya0igoddess.dbsync.extensions.DBSyncExtension
import com.github.ya0igoddess.dbsync.model.discord.AbstractDsMember
import com.github.ya0igoddess.dbsync.model.discord.DsUser
import com.github.ya0igoddess.dbsync.model.discord.DsGuild
import com.github.ya0igoddess.dbsync.model.discord.DsMember
import org.ufoss.kotysa.IndexType
import org.ufoss.kotysa.postgresql.PostgresqlTable
import org.ufoss.kotysa.tables

object Users: PostgresqlTable<DsUser>("${DBSyncExtension.code}.discord_user") {
    val id = bigInt(DsUser::id)
        .primaryKey()
    val name = varchar(DsUser::name)
}

object Guilds: PostgresqlTable<DsGuild>("${DBSyncExtension.code}.discord_guild") {
    val id = bigInt(DsGuild::id)
        .primaryKey()
    val name = varchar(DsGuild::name)
}

object Members: PostgresqlTable<DsMember>("${DBSyncExtension.code}.discord_member") {
    val id = bigInt(DsMember::id)
        .primaryKey()
    val name = varchar(DsMember::name)
    val guildId = bigInt(DsMember::guildId, "guild_id")
        .foreignKey(Guilds.id)
    val userId = bigInt(DsMember::userId, "user_id")
        .foreignKey(Users.id)

    init {
        index(setOf(guildId, userId), type = IndexType.UNIQUE ,indexName =  "guild_user_index")
    }
}

/**
 * Temporary table until bigSerial on non-nullable Long is not presented in KOTYSA
 */
object AbstractMembers: PostgresqlTable<AbstractDsMember>("${DBSyncExtension.code}.discord_member") {
    val id = bigSerial(AbstractDsMember::id)
        .primaryKey()
    val name = varchar(AbstractDsMember::name)
    val guildId = bigInt(AbstractDsMember::guildId, "guild_id")
        .foreignKey(Guilds.id)
    val userId = bigInt(AbstractDsMember::userId, "user_id")
        .foreignKey(Users.id)

    init {
        index(setOf(guildId, userId), type = IndexType.UNIQUE, indexName =  "guild_user_index")
    }
}

val tables = tables().postgresql(Users, Guilds, Members, AbstractMembers)