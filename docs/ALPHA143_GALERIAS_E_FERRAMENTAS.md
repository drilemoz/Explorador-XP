# Alpha.143: mídia direta e painel compacto

## Fotos e Vídeos

- As abas Fotos e Vídeos consultam os arquivos de mídia indexados e acessíveis pelo MediaStore, sem abrir primeiro as pastas.
- Fotos e Vídeos iniciam na visualização em miniaturas. A opção lista permanece no menu Exibir.
- Mantidos classificação e filtros por origem, além de seleção múltipla, cópia, mover, renomear, compartilhamento, Lixeira e favoritos.
- Busca por nome aplicada no índice antes da paginação de 96 itens, evitando resultados limitados à primeira página.
- Sem pasta específica como destino, criar arquivos, criar pastas e colar não são exibidos na galeria; use a aba Início ou Downloads para essas operações.
- Acesso não inclui dados privados, ocultos, não indexados ou sem permissão. MediaStore não é um explorador universal de arquivos privados.

## Ferramentas

- Os sete atalhos aparecem agora em um painel modal compacto com duas linhas (4 + 3).
- O explorador de arquivos permanece visível por trás do painel, em vez de uma página branca vazia.
- Fechar ou Voltar descarta o diálogo; tocar em uma ferramenta mantém a navegação e o retorno ao painel.

## Testes

- Validação estrutural, conferência de metadados e integridade do ZIP.
- Compilação Gradle, testes Android 16 e interação real permanecem pendentes.
