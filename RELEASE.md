# Explorador XP 0.1.0-alpha.143

**Versão:** `0.1.0-alpha.143`  
**versionCode:** `143`

## Alterações

- Fotos: abre diretamente os arquivos de imagem acessíveis no aparelho, sem lista inicial de pastas.
- Vídeos: abre diretamente os arquivos de vídeo acessíveis, com miniaturas em grade por padrão e possibilidade de alternar para lista.
- Busca por nome agora filtra o índice completo antes da paginação de 96 arquivos; filtros Câmera/Capturas/Downloads/Outros e seleção total operam sobre todos os resultados encontrados.
- Menu da galeria não permite criar pasta/colar sem um destino selecionado; essas ações permanecem disponíveis no explorador de arquivos.
- Ferramentas: substituída a página branca quase vazia por um diálogo compacto com os sete atalhos organizados em duas linhas, sobre a navegação existente.
- Barra inferior preservada: Fotos, Vídeos, Início, Downloads e Ferramentas.
- Preparação API 36, Play Store, assinatura AAB e outros módulos preservados.

## Validação e limites

- Validação estrutural e de integridade do código e dos metadados executada.
- MediaStore não dá acesso a arquivos privados, ocultos ou não indexados pelo Android.
- Build Gradle/Android, confirmação de interação e teste visual em aparelho seguem pendentes.
