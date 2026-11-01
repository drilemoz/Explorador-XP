# Explorador XP 0.1.0-alpha.145

**Versão:** `0.1.0-alpha.145`  
**versionCode:** `145`

## Correção do build Gerar APK 135

- Corrige `Unresolved reference 'selectAll'` em `ExplorerQuickAccess.kt:576`, apontado pelo compilador Kotlin no workflow `Gerar APK 135`.
- Botão **Sel. todos** de Fotos e Vídeos passa a executar `updateAllSelection(false)`, a função já implementada para selecionar todas as mídias compatíveis com os filtros, inclusive além da página atual.
- Adiciona checagem de regressão em `tools/validate_source.py`, usada no workflow, sem alterar a lógica existente do MediaStore e do explorador principal.
- Preserva os cinco atalhos inferiores, o painel Ferramentas compacto, SDK 36 e as preparações para Google Play.

## Validação

- Validadores estáticos e integridade do ZIP executados localmente.
- Compilação completa pelo Gradle/Android e teste em aparelho ainda precisam ser feitos após atualizar o repositório.
