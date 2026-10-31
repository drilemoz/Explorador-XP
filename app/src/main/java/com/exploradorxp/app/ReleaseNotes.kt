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
                "Correção do build no GitHub Actions: SDK usa setup-android v4, sem pedir o pacote antigo tools.",
                "Build de APK, AAB para Google Play e perfil de desempenho usam a mesma preparação de Android SDK 36.",
                "Metadados do projeto sincronizados com Android 16 (API 36) e verificação preventiva adicionada ao CI.",
            )
        )
}
