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
                "Preparação para Google Play: Android 16 (API 36) e fluxo separado para gerar App Bundle assinado.",
                "Seção Privacidade e permissões adicionada à tela Sobre, com esclarecimentos sobre acesso local e consultas externas opcionais.",
                "Módulo Rede continua removido; gerenciador de arquivos, aplicativos, visualizadores e demais ferramentas preservados.",
            )
        )
}
