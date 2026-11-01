# alpha.145 — correção do Gerar APK 135

## Evidência do erro

O log do workflow Gerar APK 135 apresentou `Unresolved reference 'selectAll'` em `ExplorerQuickAccess.kt:576` durante `:app:compileDebugKotlin`.

## Correção

O botão direto **Sel. todos** agora chama `updateAllSelection(false)`, existente em `MediaShortcutScreen`. A função consulta `MediaShortcutIndex.allMatchingPaths(context, kind, sort, origin, searchQuery)` fora da thread principal e contempla todos os arquivos indexados com os filtros atuais, além da paginação de 96 arquivos.

O validador `tools/validate_source.py` verifica essa chamada para antecipar regressões no CI.

## Limites da validação

As validações executadas localmente são estruturais. Um novo build no GitHub Actions e testes reais no Android são necessários para confirmar compilação e interface.
