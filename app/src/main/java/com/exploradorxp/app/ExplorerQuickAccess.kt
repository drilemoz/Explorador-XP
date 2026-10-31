package com.exploradorxp.app

import android.content.Context
import android.net.Uri
import android.content.Intent
import android.content.ClipData
import androidx.core.content.FileProvider
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.ensureActive
import java.io.FileInputStream
import androidx.activity.compose.BackHandler
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.io.File

/** Barra persistente para o explorador e todas as páginas internas. */
internal enum class ExplorerQuickDestination { PHOTOS, VIDEOS, HOME, DOWNLOADS, TOOLS }
internal enum class MediaShortcutKind { PHOTOS, VIDEOS }

@Composable
internal fun ExplorerQuickAccessBar(
    active: ExplorerQuickDestination?,
    onNavigate: (ExplorerQuickDestination) -> Unit,
) {
    val tabs = listOf(
        Triple(ExplorerQuickDestination.PHOTOS, "Fotos", R.drawable.xp_folder_image),
        Triple(ExplorerQuickDestination.VIDEOS, "Vídeos", R.drawable.xp_folder_video),
        Triple(ExplorerQuickDestination.HOME, "Início", R.drawable.xp_home),
        Triple(ExplorerQuickDestination.DOWNLOADS, "Downloads", R.drawable.folder_downloads),
        Triple(ExplorerQuickDestination.TOOLS, "Ferramentas", R.drawable.settings),
    )
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(XpChrome)
            .border(1.dp, XpChromeBorder)
            .height(72.dp)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEach { (destination, label, drawable) ->
            val selected = active == destination
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth()
                    .background(if (selected) XpSelection else Color.Transparent)
                    .clickable { onNavigate(destination) }
                    .padding(vertical = 3.dp, horizontal = 1.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(drawable),
                            contentDescription = label,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(label, fontSize = 10.sp, maxLines = 1,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) XpBlueDark else Color(0xFF253B56),
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

/** Índice de mídia paginado: a seleção total percorre apenas metadados, nunca thumbnails. */
internal data class IndexedMediaFile(
    val file: File,
    val modifiedSeconds: Long,
    val sizeBytes: Long,
    val width: Int = 0,
    val height: Int = 0,
    val durationMillis: Long = 0L,
)

internal data class MediaGalleryPage(val entries: List<IndexedMediaFile>, val hasMore: Boolean)
internal object MediaShortcutIndex {
    const val PAGE_SIZE = 96

    private fun mediaUri(kind: MediaShortcutKind): Uri = when (kind) {
        MediaShortcutKind.PHOTOS -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        MediaShortcutKind.VIDEOS -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    private fun sortClause(sort: MediaGallerySort): String {
        val column = when (sort) {
            MediaGallerySort.RECENT, MediaGallerySort.OLDEST -> MediaStore.MediaColumns.DATE_MODIFIED
            MediaGallerySort.LARGEST, MediaGallerySort.SMALLEST -> MediaStore.MediaColumns.SIZE
            MediaGallerySort.NAME -> MediaStore.MediaColumns.DISPLAY_NAME
        }
        val direction = when (sort) {
            MediaGallerySort.OLDEST, MediaGallerySort.SMALLEST, MediaGallerySort.NAME -> "ASC"
            else -> "DESC"
        }
        return "$column $direction, ${MediaStore.MediaColumns._ID} DESC"
    }

    private suspend fun queryMedia(
        context: Context, kind: MediaShortcutKind, sort: MediaGallerySort,
        consumer: (IndexedMediaFile) -> Boolean,
    ) {
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.SIZE, MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
        ) + if (kind == MediaShortcutKind.VIDEOS) arrayOf(MediaStore.Video.VideoColumns.DURATION)
            else emptyArray()
        context.contentResolver.query(mediaUri(kind), projection, null, null, sortClause(sort))?.use { cursor ->
            val pathIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            val dateIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED)
            val sizeIndex = cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)
            val widthIndex = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)
            val heightIndex = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT)
            val durationIndex = if (kind == MediaShortcutKind.VIDEOS)
                cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION) else -1
            if (pathIndex < 0) return
            while (cursor.moveToNext()) {
                kotlin.coroutines.coroutineContext.ensureActive()
                val path = cursor.getString(pathIndex)?.takeIf { it.isNotBlank() } ?: continue
                val file = File(path)
                if (!file.isFile || !file.canRead() || isPrivateExploradorCache(file)) continue
                fun longAt(index: Int): Long = if (index >= 0 && !cursor.isNull(index)) cursor.getLong(index) else 0L
                val media = IndexedMediaFile(file, longAt(dateIndex), longAt(sizeIndex).coerceAtLeast(0),
                    longAt(widthIndex).toInt().coerceAtLeast(0),
                    longAt(heightIndex).toInt().coerceAtLeast(0),
                    longAt(durationIndex).coerceAtLeast(0))
                if (!consumer(media)) break
            }
        }
    }

    suspend fun loadPage(
        context: Context, kind: MediaShortcutKind, page: Int,
        sort: MediaGallerySort, origin: MediaGalleryOrigin,
        searchQuery: String = "",
    ): MediaGalleryPage {
        require(page >= 0)
        val found = ArrayList<IndexedMediaFile>(PAGE_SIZE + 1)
        var skipped = 0L
        val offset = page.toLong() * PAGE_SIZE
        queryMedia(context, kind, sort) { media ->
            if (!MediaGalleryRules.matches(media.file.absolutePath, origin) ||
                !media.file.name.contains(searchQuery, ignoreCase = true)) return@queryMedia true
            if (skipped < offset) { skipped++; return@queryMedia true }
            found.add(media)
            found.size <= PAGE_SIZE
        }
        return MediaGalleryPage(found.take(PAGE_SIZE), found.size > PAGE_SIZE)
    }

    /** Os caminhos são coletados sem carregar imagens; suporta seleção além da primeira página. */
    suspend fun allMatchingPaths(
        context: Context, kind: MediaShortcutKind,
        sort: MediaGallerySort, origin: MediaGalleryOrigin,
        searchQuery: String = "",
    ): Set<String> {
        val paths = LinkedHashSet<String>()
        queryMedia(context, kind, sort) { media ->
            if (MediaGalleryRules.matches(media.file.absolutePath, origin) &&
                media.file.name.contains(searchQuery, ignoreCase = true)) {
                paths.add(media.file.absolutePath)
            }
            true
        }
        return paths
    }

    internal fun isPrivateExploradorCache(file: File): Boolean =
        file.toPath().any { segment ->
            segment.toString().equals(".ExploradorXP_Lixeira", ignoreCase = true)
        }
}

internal fun galleryFileSize(bytes: Long): String = when {
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> "%.1f KB".format(bytes / 1024.0)
    bytes < 1024L * 1024 * 1024 -> "%.1f MB".format(bytes / (1024.0 * 1024))
    else -> "%.2f GB".format(bytes / (1024.0 * 1024 * 1024))
}

internal fun galleryDuration(milliseconds: Long): String {
    val seconds = (milliseconds / 1000).coerceAtLeast(0)
    return if (seconds >= 3600) "%d:%02d:%02d".format(seconds / 3600, (seconds / 60) % 60, seconds % 60)
        else "%02d:%02d".format(seconds / 60, seconds % 60)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MediaShortcutScreen(
    kind: MediaShortcutKind,
    accessGranted: Boolean,
    onRequestAccess: () -> Unit,
    onDismiss: () -> Unit,
    onOpenFile: (File) -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenTools: () -> Unit,
    onCopyFiles: (List<File>) -> Unit,
    onCutFiles: (List<File>) -> Unit,
    onMoveToTrash: (List<File>) -> Unit,
    onRenameFile: (File, String, () -> Unit) -> Unit,
    transferState: StateFlow<TransferState?>,
    onFavoriteFile: (File) -> Unit,
    onCompressFiles: (List<File>) -> Unit,
    onShowInstalledApps: () -> Unit,
    onShowSettings: () -> Unit,
    onShowDeviceInfo: () -> Unit,
    onShowManual: () -> Unit,
    onShowAbout: () -> Unit,
    onOpenTrash: () -> Unit,
    trashCount: Int? = null,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var entries by remember(kind, accessGranted) { mutableStateOf(emptyList<IndexedMediaFile>()) }
    var page by remember(kind, accessGranted) { mutableStateOf(0) }
    var hasMore by remember(kind, accessGranted) { mutableStateOf(true) }
    var loading by remember(kind, accessGranted) { mutableStateOf(accessGranted) }
    var copying by remember { mutableStateOf(false) }
    var selectingAll by remember { mutableStateOf(false) }
    var error by remember(kind, accessGranted) { mutableStateOf<String?>(null) }
    var gridView by remember(kind) { mutableStateOf(true) }
    var selectMode by remember(kind) { mutableStateOf(false) }
    var selected by remember(kind) { mutableStateOf<Set<String>>(emptySet()) }
    var notice by remember { mutableStateOf("") }
    var sort by remember(kind) { mutableStateOf(MediaGallerySort.RECENT) }
    var origin by remember(kind) { mutableStateOf(MediaGalleryOrigin.ALL) }
    var fileMenu by remember { mutableStateOf(false) }
    var editMenu by remember { mutableStateOf(false) }
    var viewMenu by remember { mutableStateOf(false) }
    var favoritesMenu by remember { mutableStateOf(false) }
    var toolsMenu by remember { mutableStateOf(false) }
    var helpMenu by remember { mutableStateOf(false) }
    var refreshKey by remember { mutableStateOf(0) }
    var searchMode by remember(kind) { mutableStateOf(false) }
    var searchQuery by remember(kind) { mutableStateOf("") }
    var renameTarget by remember { mutableStateOf<File?>(null) }
    var confirmTrash by remember { mutableStateOf<List<File>?>(null) }
    var moreMenu by remember { mutableStateOf(false) }
    fun backGallery() {
        if (selectMode) { selectMode = false; selected = emptySet() }
        else onDismiss()
    }
    BackHandler(onBack = { backGallery() })

    fun reloadGallery() {
        page = 0; entries = emptyList(); hasMore = true; error = null
        selected = emptySet(); refreshKey++
    }

    val activeTransfer by transferState.collectAsStateWithLifecycle()
    var sawTransfer by remember(kind) { mutableStateOf(false) }
    LaunchedEffect(activeTransfer) {
        if (activeTransfer != null) sawTransfer = true
        else if (sawTransfer) {
            sawTransfer = false
            reloadGallery()
        }
    }

    LaunchedEffect(kind, accessGranted, sort, origin, searchQuery, page, refreshKey) {
        if (accessGranted && (page == 0 || hasMore)) {
            loading = true
            try {
                val next = withContext(Dispatchers.IO) {
                    MediaShortcutIndex.loadPage(context, kind, page, sort, origin, searchQuery)
                }
                entries = if (page == 0) next.entries
                    else (entries + next.entries).distinctBy { it.file.absolutePath }
                hasMore = next.hasMore
                error = null
            } catch (exception: Exception) {
                error = exception.localizedMessage ?: "Não foi possível carregar a mídia."
            } finally { loading = false }
        }
    }

    val pickDestination = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        // selected pode conter arquivos ainda não carregados em tela após Selecionar tudo.
        val source = selected.map(::File)
        if (uri != null && source.isNotEmpty()) scope.launch {
            copying = true; notice = "Copiando ${source.size} arquivo(s)..."
            try {
                val result = withContext(Dispatchers.IO) {
                    val dest = DocumentFile.fromTreeUri(context, uri) ?: kotlin.error("Pasta de destino indisponível")
                    require(dest.canWrite()) { "Pasta sem permissão de escrita." }
                    var copied = 0; var skipped = 0
                    for (file in source) {
                        kotlinx.coroutines.currentCoroutineContext().ensureActive()
                        if (!file.isFile || !file.canRead() || dest.findFile(file.name) != null) {
                            skipped++; continue
                        }
                        val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension.lowercase())
                            ?: "application/octet-stream"
                        val document = dest.createFile(mime, file.name)
                            ?: kotlin.error("Não foi possível criar ${file.name}")
                        try {
                            FileInputStream(file).use { input ->
                                (context.contentResolver.openOutputStream(document.uri)
                                    ?: kotlin.error("Destino indisponível")).use { output ->
                                    input.copyTo(output, 64 * 1024)
                                }
                            }
                            copied++
                        } catch (e: Exception) { runCatching { document.delete() }; throw e }
                    }
                    "$copied copiado(s), $skipped ignorado(s). Arquivos existentes não foram substituídos."
                }
                notice = result; selected = emptySet(); selectMode = false
            } catch (e: Exception) { notice = "Falha na cópia: ${e.localizedMessage}" }
            finally { copying = false }
        }
    }
    fun shareSelected() {
        // Limite para evitar TransactionTooLargeException na intent com centenas de URIs.
        if (selected.size > 100) {
            notice = "Para enviar, selecione no máximo 100 arquivos por vez. Copiar aceita todos."
            return
        }
        val source = selected.map(::File).filter { it.isFile && it.canRead() }
        if (source.isEmpty()) return
        runCatching {
            val uris = ArrayList(source.map {
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
            })
            val intent = Intent(if (uris.size == 1) Intent.ACTION_SEND else Intent.ACTION_SEND_MULTIPLE).apply {
                type = if (kind == MediaShortcutKind.PHOTOS) "image/*" else "video/*"
                if (uris.size == 1) putExtra(Intent.EXTRA_STREAM, uris.first())
                else putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                clipData = ClipData.newUri(context.contentResolver, "Mídia selecionada", uris.first()).also { clip ->
                    uris.drop(1).forEach { clip.addItem(ClipData.Item(it)) }
                }
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Compartilhar ${source.size} arquivo(s)"))
        }.onFailure { notice = "Falha ao compartilhar: ${it.localizedMessage}" }
    }
    fun updateAllSelection(invert: Boolean) {
        scope.launch {
            selectingAll = true
            try {
                val paths = withContext(Dispatchers.IO) {
                    MediaShortcutIndex.allMatchingPaths(context, kind, sort, origin, searchQuery)
                }
                selected = if (invert) paths - selected else paths
                notice = "${selected.size} arquivo(s) selecionado(s) na biblioteca."
            } catch (e: Exception) {
                notice = "Falha ao atualizar seleção: ${e.localizedMessage}"
            } finally { selectingAll = false }
        }
    }
    fun selectedFiles(): List<File> = selected.map(::File).filter { it.isFile && it.canRead() }
    fun endSelection() { selectMode = false; selected = emptySet() }
    fun copySelectedToClipboard(move: Boolean) {
        val source = selectedFiles()
        if (source.isEmpty()) return
        if (move) onCutFiles(source) else onCopyFiles(source)
        notice = if (move) "${source.size} mídia(s) pronta(s) para mover. Abra a pasta de destino e toque em Colar."
            else "${source.size} mídia(s) pronta(s) para copiar. Abra a pasta de destino e toque em Colar."
        endSelection()
    }
    fun renameSelected() { renameTarget = selectedFiles().singleOrNull() }
    fun deleteSelected() { confirmTrash = selectedFiles().takeIf { it.isNotEmpty() } }
    fun compressSelected() {
        val files = selectedFiles()
        if (files.isNotEmpty()) onCompressFiles(files)
    }
    val title = if (kind == MediaShortcutKind.PHOTOS) "Fotos" else "Vídeos"
    val matchingEntries = entries // A pesquisa é aplicada no MediaStore antes da paginação.
    fun tap(item: IndexedMediaFile) {
        if (selectMode) {
            selected = if (item.file.absolutePath in selected) selected - item.file.absolutePath
                else selected + item.file.absolutePath
        } else onOpenFile(item.file)
    }

    renameTarget?.let { file ->
        RenameFileDialog(file, onDismiss = { renameTarget = null }) { newName ->
            renameTarget = null
            onRenameFile(file, newName) { reloadGallery() }
            endSelection()
        }
    }
    confirmTrash?.let { files ->
        AlertDialog(onDismissRequest = { confirmTrash = null },
            title = { Text("Mover para a Lixeira?") },
            text = { Text("${files.size} item(ns) selecionado(s). Os arquivos poderão ser restaurados pela Lixeira.") },
            confirmButton = { TextButton(onClick = {
                confirmTrash = null; onMoveToTrash(files); endSelection(); reloadGallery()
            }) { Text("Mover para a Lixeira") } },
            dismissButton = { TextButton(onClick = { confirmTrash = null }) { Text("Cancelar") } },
        )
    }

    Column(Modifier.fillMaxSize().background(XpBackground)) {
        Row(
            Modifier.fillMaxWidth().height(50.dp).background(XpBlueDark).padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { backGallery() }, modifier = Modifier.size(44.dp)) {
                Icon(painterResource(R.drawable.back), "Voltar", tint = Color.White, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(8.dp))
            Text(if (selectMode) "${selected.size} selecionado(s)" else "Explorador XP • $title",
                color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp,
                modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(if (loading && entries.isEmpty()) "Buscando..." else "${entries.size}${if (hasMore) "+" else ""} arquivos",
                color = Color.White.copy(alpha = .82f), fontSize = 11.sp,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        // A barra e os componentes dos menus agora são compartilhados com Arquivos.
        XpExplorerMenuBar {
            Box {
                XpMenuLabel("Arquivo", fileMenu) { fileMenu = true }
                XpPopupMenu(fileMenu, { fileMenu = false }) {
                    XpMenuItem("Atualizar", R.drawable.refresh) { fileMenu = false; reloadGallery() }
                    XpMenuDivider()
                    XpMenuItem("Lixeira", R.drawable.trash_empty) { fileMenu = false; onOpenTrash() }
                }
            }
            Box {
                XpMenuLabel("Editar", editMenu) { editMenu = true }
                XpPopupMenu(editMenu, { editMenu = false }) {
                    XpMenuItem("Selecionar tudo", R.drawable.select_all, enabled = !selectingAll) {
                        editMenu = false; selectMode = true; updateAllSelection(false)
                    }
                    XpMenuItem("Inverter seleção", enabled = selectMode && !selectingAll) {
                        editMenu = false; updateAllSelection(true)
                    }
                    XpMenuItem("Cancelar seleção", enabled = selectMode) {
                        editMenu = false; endSelection()
                    }
                    if (selected.isNotEmpty()) {
                        XpMenuDivider()
                        XpMenuItem("Copiar", R.drawable.copy) { editMenu = false; copySelectedToClipboard(false) }
                        XpMenuItem("Mover", R.drawable.move) { editMenu = false; copySelectedToClipboard(true) }
                        XpMenuItem("Renomear", R.drawable.rename, enabled = selected.size == 1) {
                            editMenu = false; renameSelected()
                        }
                        XpMenuItem("Excluir (Lixeira)", R.drawable.delete) { editMenu = false; deleteSelected() }
                    }
                }
            }
            Box {
                XpMenuLabel("Exibir", viewMenu) { viewMenu = true }
                XpPopupMenu(viewMenu, { viewMenu = false }) {
                    XpMenuItem("Exibir como ícones", R.drawable.view_grid, checked = gridView) {
                        viewMenu = false; gridView = true
                    }
                    XpMenuItem("Exibir como lista", R.drawable.view_list, checked = !gridView) {
                        viewMenu = false; gridView = false
                    }
                    XpMenuDivider()
                    MediaGallerySort.values().forEach { option ->
                        XpMenuItem("Classificar: ${option.label}", checked = sort == option) {
                            viewMenu = false; sort = option; reloadGallery()
                        }
                    }
                    XpMenuDivider()
                    MediaGalleryOrigin.values().forEach { option ->
                        XpMenuItem("Local: ${option.label}", checked = origin == option) {
                            viewMenu = false; origin = option; reloadGallery()
                        }
                    }
                    XpMenuDivider()
                    XpMenuItem("Atualizar", R.drawable.refresh) { viewMenu = false; reloadGallery() }
                }
            }
            Box {
                XpMenuLabel("Favoritos", favoritesMenu) { favoritesMenu = true }
                XpPopupMenu(favoritesMenu, { favoritesMenu = false }) {
                    XpMenuItem("Abrir Favoritos", R.drawable.favorites) { favoritesMenu = false; onOpenFavorites() }
                    XpMenuItem("Adicionar aos Favoritos", R.drawable.favorites, enabled = selected.size == 1) {
                        favoritesMenu = false; selectedFiles().singleOrNull()?.let(onFavoriteFile)
                    }
                }
            }
            XpExplorerToolsMenu(
                onApps = onShowInstalledApps,
                onSettings = onShowSettings,
                onDevice = onShowDeviceInfo,
            )
            XpExplorerHelpMenu(onHelp = onShowManual, onAbout = onShowAbout)

        }
        if (selected.isNotEmpty() && selectMode) {
            Row(Modifier.fillMaxWidth().height(56.dp).background(XpChrome).border(1.dp, XpChromeBorder),
                verticalAlignment = Alignment.CenterVertically) {
                XpClassicToolButton(R.drawable.copy, "Copiar", !copying, { copySelectedToClipboard(false) }, Modifier.weight(1f), compact = true)
                XpClassicToolButton(R.drawable.move, "Mover", !copying, { copySelectedToClipboard(true) }, Modifier.weight(1f), compact = true)
                XpClassicToolButton(R.drawable.rename, "Renomear", selected.size == 1 && !copying, { renameSelected() }, Modifier.weight(1f), compact = true)
                XpClassicToolButton(R.drawable.share, "Compart.", !copying, { shareSelected() }, Modifier.weight(1f), compact = true)
                XpClassicToolButton(R.drawable.folder_compressed, "ZIP", !copying, { compressSelected() }, Modifier.weight(1f), compact = true)
                XpClassicToolButton(R.drawable.delete, "Excluir", !copying, { deleteSelected() }, Modifier.weight(1f), compact = true)
                Box(Modifier.weight(1f).fillMaxHeight()) {
                    XpClassicToolButton(R.drawable.more, "Mais", true, { moreMenu = true }, Modifier.fillMaxWidth(), compact = true)
                    XpPopupMenu(moreMenu, { moreMenu = false }) {
                        XpMenuItem("Adicionar aos Favoritos", R.drawable.favorites, enabled = selected.size == 1) {
                            moreMenu = false; selectedFiles().singleOrNull()?.let(onFavoriteFile)
                        }
                        XpMenuItem("Cancelar seleção", R.drawable.close) { moreMenu = false; endSelection() }
                    }
                }
            }
        } else {
            XpExplorerNavigationBar(XpNavigationCommands(
                canBack = false, canForward = false, canUp = false,
                onBack = { backGallery() }, onForward = {}, onUp = {},
                onTrash = onOpenTrash, trashCount = trashCount,
                onSearch = { searchMode = !searchMode; if (!searchMode) { searchQuery = ""; reloadGallery() } },
                onNewFolder = {}, onNewFile = {}, canNew = false,
                onRefresh = { reloadGallery() },
                onLast = { onOpenFavorites() }, lastLabel = "Favoritos", lastIcon = R.drawable.favorites,
                canLast = true,
            ))
        }
        if (searchMode) {
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it; reloadGallery() },
                placeholder = { Text("Pesquisar fotos e vídeos em todo o dispositivo") },
                singleLine = true, modifier = Modifier.fillMaxWidth().padding(horizontal = 9.dp, vertical = 3.dp),
            )
        }
        Row(
            Modifier.fillMaxWidth().height(36.dp).background(Color.White)
                .border(1.dp, XpCardBorder).padding(horizontal = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Endereço", color = XpTextSecondary, fontSize = 11.sp)
            Spacer(Modifier.width(10.dp))
            Text("${title}  ›  Todas as mídias",
                color = XpBlueDark, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (origin != MediaGalleryOrigin.ALL && !selectMode) {
            Text("Filtro: ${origin.label}", Modifier.padding(start = 14.dp, bottom = 4.dp),
                color = XpBlueDark, fontSize = 11.sp)
        }
        if (loading || selectingAll || copying) {
            Text(when { copying -> "Copiando..."; selectingAll -> "Selecionando arquivos do dispositivo..."; else -> "Carregando..." },
                Modifier.padding(horizontal = 12.dp, vertical = 2.dp), fontSize = 11.sp, color = XpTextSecondary)
        }
        if (notice.isNotBlank()) Text(notice, Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            fontSize = 11.sp, color = XpBlueDark)
        when {
            !accessGranted -> Column(Modifier.padding(18.dp)) {
                Text("Autorize o acesso aos arquivos para listar $title.")
                Text("Conceder acesso", Modifier.clickable(onClick = onRequestAccess).padding(10.dp), color = XpBlueDark)
            }
            entries.isEmpty() && loading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            error != null && entries.isEmpty() ->
                Text(error ?: "", Modifier.padding(18.dp), color = Color(0xFFA42222))
            matchingEntries.isEmpty() ->
                Text("Nenhuma mídia encontrada no dispositivo com os filtros atuais.", Modifier.padding(18.dp))
            else -> if (gridView) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(108.dp), modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(7.dp), horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    gridItems(matchingEntries, key = { it.file.absolutePath }) { item ->
                        val chosen = item.file.absolutePath in selected
                        Column(Modifier.fillMaxWidth().combinedClickable(
                            onClick = { tap(item) },
                            onLongClick = { selectMode = true; selected = selected + item.file.absolutePath }
                        ), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.border(if (chosen) 2.dp else 0.dp,
                                if (chosen) XpBlueDark else Color.Transparent,
                                androidx.compose.foundation.shape.RoundedCornerShape(7.dp))) {
                                GalleryFileThumbnail(item.file, kind == MediaShortcutKind.VIDEOS, 104.dp)
                                if (kind == MediaShortcutKind.VIDEOS && item.durationMillis > 0L)
                                    Text(galleryDuration(item.durationMillis),
                                        Modifier.align(Alignment.BottomEnd)
                                            .background(Color(0xCC111B2B)).padding(horizontal = 4.dp, vertical = 2.dp),
                                        fontSize = 10.sp, color = Color.White)
                                if (selectMode)
                                    Icon(if (chosen) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                        contentDescription = if (chosen) "Selecionado" else "Não selecionado",
                                        tint = if (chosen) XpBlueDark else Color.White,
                                        modifier = Modifier.align(Alignment.TopEnd).padding(3.dp).size(24.dp))
                            }
                            Text(item.file.name, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(galleryFileSize(item.sizeBytes), fontSize = 10.sp, color = XpTextSecondary)
                        }
                    }
                    if (hasMore) item {
                        OutlinedButton(onClick = { page++ }, enabled = !loading && !copying && !selectingAll) {
                            Text(if (loading) "Carregando..." else "Mais arquivos")
                        }
                    }
                }
            } else {
                LazyColumn(Modifier.weight(1f)) {
                    items(matchingEntries, key = { it.file.absolutePath }) { item ->
                        val chosen = item.file.absolutePath in selected
                        Row(Modifier.fillMaxWidth().background(if (chosen) XpSelection else Color.Transparent)
                            .combinedClickable(onClick = { tap(item) }, onLongClick = {
                                selectMode = true; selected = selected + item.file.absolutePath
                            }).padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            if (selectMode) Checkbox(checked = chosen, onCheckedChange = { tap(item) })
                            Box {
                                GalleryFileThumbnail(item.file, kind == MediaShortcutKind.VIDEOS, 55.dp)
                                if (kind == MediaShortcutKind.VIDEOS && item.durationMillis > 0L)
                                    Text(galleryDuration(item.durationMillis),
                                        Modifier.align(Alignment.BottomEnd).background(Color(0xCC111B2B))
                                            .padding(horizontal = 5.dp), color = Color.White, fontSize = 9.sp)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.file.name, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                val dimensions = if (item.width > 0 && item.height > 0)
                                    " • ${item.width}×${item.height}" else ""
                                Text("${galleryFileSize(item.sizeBytes)}$dimensions • ${item.file.parentFile?.name ?: "Armazenamento"}",
                                    fontSize = 10.sp, color = XpTextSecondary,
                                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                    if (hasMore) item {
                        OutlinedButton(onClick = { page++ }, enabled = !loading && !copying && !selectingAll) {
                            Text(if (loading) "Carregando..." else "Carregar mais")
                        }
                    }
                }
            }
        }
    }
}
