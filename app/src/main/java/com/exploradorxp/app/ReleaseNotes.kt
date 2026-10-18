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
                "Nome do aplicativo maior na identificação do APK, com o ícone ao lado e sem selo de versão na linha do título.",
                "Versão instalada, situação e versão do arquivo agora ficam em três selos na mesma linha, com cores preservadas e rolagem horizontal em telas pequenas.",
                "Mantida a ordem dos botões Abrir, Gerenciar, Desinstalar e Instalar/Atualizar, além das verificações de segurança do APK.",
            )
        )
}
