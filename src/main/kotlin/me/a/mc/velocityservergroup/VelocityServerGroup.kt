package me.a.mc.velocityservergroup

import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object VelocityServerGroup : Plugin() {

    @Config("config.toml", autoReload = true)
    lateinit var config: Configuration
        private set
}
