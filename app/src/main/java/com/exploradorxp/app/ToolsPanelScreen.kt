package com.exploradorxp.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/** Apenas módulos existentes: o painel não cria serviços ou permissões adicionais. */
internal enum class ExplorerToolAction { APPS, STORAGE, DEVICE, SETTINGS, TRASH, HELP, ABOUT }

private data class ToolShortcut(val action: ExplorerToolAction, val label: String, val icon: Int, val description: String)

private val panelShortcuts = listOf(
    ToolShortcut(ExplorerToolAction.APPS, "Aplicativos", R.drawable.file_apk, "Instalados e uso"),
    ToolShortcut(ExplorerToolAction.STORAGE, "Armazenamento", R.drawable.drive_storage, "Espaço e arquivos"),
    ToolShortcut(ExplorerToolAction.DEVICE, "Dispositivo", R.drawable.device_mobile, "Dados do aparelho"),
    ToolShortcut(ExplorerToolAction.TRASH, "Lixeira", R.drawable.trash_empty, "Itens excluídos"),
    ToolShortcut(ExplorerToolAction.SETTINGS, "Configurações", R.drawable.settings, "Preferências do XP"),
    ToolShortcut(ExplorerToolAction.HELP, "Ajuda", R.drawable.help, "Dúvidas e uso"),
    ToolShortcut(ExplorerToolAction.ABOUT, "Sobre", R.drawable.info, "Versão e novidades"),
)

/** Painel compacto exibido sobre o explorador: sem página branca vazia. */
@Composable
internal fun ToolsPanelScreen(
    onOpenTool: (ExplorerToolAction) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(XpBackground)
                .border(1.dp, XpCardBorder, RoundedCornerShape(14.dp)),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp)
                    .background(XpBlueDark).padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(painterResource(R.drawable.settings), contentDescription = null,
                    modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Ferramentas", color = Color.White, fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f),
                )
                Text(
                    "Fechar", color = Color.White, fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = onDismiss).padding(8.dp),
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                panelShortcuts.chunked(4).forEach { shortcuts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                    ) {
                        shortcuts.forEach { shortcut ->
                            ToolShortcutTile(
                                shortcut = shortcut,
                                modifier = Modifier.weight(1f),
                                onClick = { onOpenTool(shortcut.action) },
                            )
                        }
                        repeat(4 - shortcuts.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ToolShortcutTile(
    shortcut: ToolShortcut,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier.height(101.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(Color.White)
            .border(1.dp, XpCardBorder, RoundedCornerShape(9.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(39.dp)
                .background(Color(0xFFEAF3FF), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painterResource(shortcut.icon), contentDescription = null,
                modifier = Modifier.size(27.dp),
            )
        }
        Spacer(Modifier.height(3.dp))
        Text(
            shortcut.label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
            lineHeight = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center, color = Color(0xFF253B56),
        )
        Spacer(Modifier.height(1.dp))
        Text(
            shortcut.description, fontSize = 8.sp, lineHeight = 10.sp,
            maxLines = 2, overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center, color = XpTextSecondary,
        )
    }
}
