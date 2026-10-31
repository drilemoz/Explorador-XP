# alpha.142: navegação e análise de Fotos/Vídeos

## Implementado

- Barra inferior com cinco atalhos, em ordem: Fotos, Vídeos, Início, Downloads e Ferramentas.
- Início removido do cabeçalho; Lixeira ocupa a última posição da barra superior, com contador visual para itens encontrados.
- Ações Voltar, Avançar e Subir mantidas no cabeçalho das galerias e do gerenciador de arquivos.
- A Lixeira permanece acessível pelos menus e painel Ferramentas.

## Análise (não implementada)

Hoje `MediaShortcutScreen` carrega `MediaShortcutIndex.loadFolders()` na raiz e lista arquivos só depois de selecionar uma pasta (`loadPage(..., folderPath)`). O índice MediaStore já suporta listar arquivos de todas as pastas por `loadPage(..., folderPath = null)`, usando paginação de 96 itens e filtros por origem.

Proposta para versão futura:

1. Abrir Fotos e Vídeos diretamente com todos os arquivos de mídia encontrados, inicialmente do mais recente para o mais antigo.
2. Manter alternância lista/grade, filtros de origem e ordenação.
3. Oferecer opção de mostrar pastas sem obrigar a navegar nelas; preservar caminho de origem na lista/detalhes.
4. Adaptar pesquisa, seleção total, recarregamento, ação Subir e criação de arquivos, atualmente dependentes de `folderPath`.
5. Evitar varredura direta recursiva na thread principal; reutilizar MediaStore e a paginação atual, respeitar permissões e mídia não indexada.

Nenhum desses cinco itens foi incorporado à alpha.142.
