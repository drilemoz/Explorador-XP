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
                "Nome do aplicativo centralizado no resumo do APK, com aumento visual aproximado de 30% e mais destaque ao lado do ícone.",
                "Versão instalada permanece abaixo do nome, enquanto situação e versão do arquivo foram movidas para a direita, uma abaixo da outra, com vermelho para APK anterior.",
                "Mantida a ordem dos botões Abrir, Gerenciar, Desinstalar e Instalar/Atualizar, além das verificações de segurança do APK.",
            )
        )
}
