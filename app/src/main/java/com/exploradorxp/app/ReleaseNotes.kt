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
                "Nome do aplicativo centralizado no resumo do APK, ao lado do ícone, mantendo maior destaque visual.",
                "Versão instalada, situação e versão do arquivo agora ficam empilhadas verticalmente à direita; a versão mais antiga fica vermelha em atualizações ou downgrades.",
                "Mantida a ordem dos botões Abrir, Gerenciar, Desinstalar e Instalar/Atualizar, além das verificações de segurança do APK.",
            )
        )
}
