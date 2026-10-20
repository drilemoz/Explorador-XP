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
                "Três selos de versão e situação no cabeçalho do APK agora têm a mesma largura e altura, com cantos retos.",
                "Textos dos selos centralizados, inclusive quando curtos; versões longas mantêm a descrição integral para acessibilidade.",
                "Mantidas as cores de versão instalada, atualização ou downgrade e os botões Abrir, Gerenciar, Desinstalar e Instalar/Atualizar.",
            )
        )
}
