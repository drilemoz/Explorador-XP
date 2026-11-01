package com.exploradorxp.app

data class ReleaseInfo(
    val versionName: String,
    val versionCode: Int,
    val title: String,
    val changes: List<String>,
)

/**
 * Fonte única das novidades exibidas após uma atualização e em Sobre > Novidades desta versão.
 * Atualize a lista abaixo junto com versionName/versionCode em toda nova versão do aplicativo.
 */
object ReleaseNotes {
    val current: ReleaseInfo
        get() = ReleaseInfo(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            title = "O Explorador XP foi atualizado",
            changes = listOf(
                "A barra de seleção agora mostra Selecionar todos diretamente no explorador, sem depender do menu Mais.",
                "Fotos e Vídeos receberam o mesmo atalho Selecionar todos na barra de seleção das galerias.",
                "Ferramentas continua em painel compacto e as galerias seguem abrindo diretamente os arquivos de mídia do dispositivo.",
            )
        )
}
