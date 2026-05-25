package me.a.mc.velocityservergroup.api

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.server.RegisteredServer
import me.a.mc.velocityservergroup.VelocityServerGroup.config
import me.a.mc.velocityservergroup.api.group.ServerGroup
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.warning
import taboolib.platform.VelocityPlugin

interface VelocityServerGroupAPI {

    /**
     * 按名取组
     *
     * @param name 组名
     * @return 找到的组 不存在则 null
     */
    fun getGroup(name: String): ServerGroup?

    /**
     * 所有已加载的组
     *
     * @return 组集合 顺序与配置一致
     */
    fun getGroups(): Collection<ServerGroup>

    /**
     * 子服所属的组
     *
     * @param serverName 子服名
     * @return 所在组 不属于任何组则 null
     */
    fun getGroupOf(serverName: String): ServerGroup?

    /**
     * 组内当前在线玩家
     *
     * @param groupName 组名
     * @return 玩家集合 组不存在返回空集
     */
    fun getGroupPlayers(groupName: String): Set<Player>

    companion object : VelocityServerGroupAPI {

        private data class DefaultServerGroup(
            override val name: String,
            override val servers: Set<RegisteredServer>
        ) : ServerGroup

        @Volatile
        private var groups: Map<String, ServerGroup> = emptyMap()

        @Volatile
        private var serverIndex: Map<String, ServerGroup> = emptyMap()

        @Awake(LifeCycle.ENABLE)
        internal fun load() {
            val proxy = VelocityPlugin.getInstance().server
            val names = config.getConfigurationSection("groups")?.getKeys(false) ?: emptySet()
            groups = names.associateWith { name ->
                val servers = config.getStringList("groups.$name.servers").mapNotNull { s ->
                    proxy.getServer(s).orElse(null).also {
                        if (it == null) {
                            warning("组 '$name' 引用了未注册的子服 '$s'")
                        }
                    }
                }.toSet()
                DefaultServerGroup(name, servers)
            }.also {
                serverIndex = buildMap {
                    it.values.forEach { g -> g.servers.forEach { s -> put(s.serverInfo.name, g) } }
                }
            }
        }

        override fun getGroup(name: String): ServerGroup? = groups[name]

        override fun getGroups(): Collection<ServerGroup> = groups.values

        override fun getGroupOf(serverName: String): ServerGroup? = serverIndex[serverName]

        override fun getGroupPlayers(groupName: String): Set<Player> =
            groups[groupName]?.servers?.flatMapTo(mutableSetOf()) { it.playersConnected } ?: emptySet()
    }
}
