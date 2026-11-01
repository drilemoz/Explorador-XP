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
                "Corrigida a falha de compilação do botão Sel. todos nas galerias de Fotos e Vídeos.",
                "Selecionar todos continua abrangendo todas as mídias compatíveis, além das páginas já carregadas.",
                "Preservados o botão de seleção direta no explorador, as galerias unificadas e o painel Ferramentas compacto.",
            )
        )
}
