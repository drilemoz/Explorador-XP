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
                "Lixeira foi movida para a extremidade direita da barra superior, com contador de itens.",
                "Início ocupa agora a terceira posição da barra inferior; Downloads passou para a quarta posição.",
                "Barra inferior tem cinco atalhos: Fotos, Vídeos, Início, Downloads e Ferramentas, sem duplicar Início no topo.",
            )
        )
}
