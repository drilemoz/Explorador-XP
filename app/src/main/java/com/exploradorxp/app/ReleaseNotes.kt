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
                "Fotos e Vídeos agora exibem diretamente os arquivos de mídia do dispositivo, sem abrir uma lista inicial de pastas.",
                "Galerias com miniaturas, pesquisa por nome em todas as mídias encontradas, filtros por origem e seleção múltipla.",
                "Ferramentas agora abre em um painel compacto de duas linhas, eliminando a página quase toda vazia.",
            )
        )
}
