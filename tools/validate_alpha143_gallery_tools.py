#!/usr/bin/env python3
"""Guardas estruturais da navegação de mídia e do painel de Ferramentas.
Não substituem build Gradle nem testes físicos de interface.
"""
from pathlib import Path

root = Path(__file__).resolve().parents[1]
sources = root / 'app/src/main/java/com/exploradorxp/app'
gallery = (sources / 'ExplorerQuickAccess.kt').read_text(encoding='utf-8')
main = (sources / 'ExplorerScreen.kt').read_text(encoding='utf-8')
tools = (sources / 'ToolsPanelScreen.kt').read_text(encoding='utf-8')
assert 'MediaStore.Images.Media.EXTERNAL_CONTENT_URI' in gallery
assert 'MediaStore.Video.Media.EXTERNAL_CONTENT_URI' in gallery
assert 'const val PAGE_SIZE = 96' in gallery
assert 'MediaShortcutIndex.loadPage(context, kind, page, sort, origin, searchQuery)' in gallery
assert 'MediaShortcutIndex.allMatchingPaths(context, kind, sort, origin, searchQuery)' in gallery
assert 'media.file.name.contains(searchQuery, ignoreCase = true)' in gallery
assert 'LaunchedEffect(kind, accessGranted, sort, origin, searchQuery, page, refreshKey)' in gallery
assert 'var gridView by remember(kind) { mutableStateOf(true) }' in gallery
assert 'Todas as mídias' in gallery
assert 'Nenhuma mídia encontrada no dispositivo' in gallery
assert 'loadFolders(' not in gallery and 'matchingFolders' not in gallery
assert 'folderPath' not in gallery and 'switchFolder' not in gallery
assert 'Pesquisar pastas' not in gallery
assert 'Selecionar tudo' in gallery and 'Galeria' not in gallery[:150]
assert 'Dialog(' in tools and 'DialogProperties(usePlatformDefaultWidth = false)' in tools
assert 'panelShortcuts.chunked(4)' in tools
assert 'modifier = modifier.height(101.dp)' in tools
assert 'LazyVerticalGrid' not in tools and 'Modifier.fillMaxSize()' not in tools
assert 'showSettings || showTrash || showAbout' in main
assert 'showTrash || showToolsPanel' not in main
assert 'if (showToolsPanel) {' in main and 'ToolsPanelScreen(' in main
assert 'active = if (showToolsPanel) ExplorerQuickDestination.TOOLS' in main
print('OK alpha.143: galeria global, filtros e seleção paginados, painel Ferramentas modal compacto')
