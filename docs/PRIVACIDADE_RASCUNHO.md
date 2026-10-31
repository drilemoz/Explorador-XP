# Política de privacidade do Explorador XP (minuta para revisão antes da publicação)

**Situação: minuta, ainda NÃO publicada em URL pública.**
**Última atualização proposta: 31/10/2026.**

O Explorador XP (`com.exploradorxp.app`) é um aplicativo Android para gerenciamento local de arquivos, visualização de mídia e gerenciamento/inspeção de aplicativos instalados. O responsável pela publicação deve confirmar o conteúdo desta política e fornecer uma forma de contato de suporte antes da publicação.

## Acesso ao armazenamento

Para listar, copiar, mover, renomear, compactar, compartilhar, extrair e excluir arquivos solicitados pelo usuário, o app pode requerer **Acesso a todos os arquivos** nas configurações do Android. As operações são realizadas no dispositivo, salvo quando a pessoa decide compartilhar um arquivo com outro aplicativo.

## Aplicativos instalados e permissões

O módulo Aplicativos usa informações sobre os apps instalados para exibir, pesquisar, comparar versões e executar ações iniciadas pelo usuário. Instalação e desinstalação dependem das confirmações e permissões do sistema Android. Estatísticas de uso e armazenamento dependem de autorização adicional quando disponível.

## Consultas externas opcionais

As ferramentas de identificação de dispositivos e imagens podem acessar catálogos públicos do Google, Wikidata e Wikimedia Commons quando ativadas nas configurações. Os provedores podem receber informações da conexão, incluindo o endereço IP e os parâmetros das consultas, de acordo com suas políticas. Sem essas consultas opcionais, os recursos principais de gerenciamento de arquivos continuam disponíveis localmente.

## Dados armazenados e compartilhamento

Verificar antes da publicação a persistência efetiva de preferências, cache, histórico, imagens e informações do dispositivo. O usuário pode compartilhar arquivos com aplicativos escolhidos por meio do mecanismo do Android; o Explorador XP não pode controlar o tratamento posterior realizado pelo aplicativo destinatário.

## Segurança e direitos

As permissões podem ser revogadas nas configurações do Android. É possível remover o aplicativo para excluir dados privados dele, observando que arquivos já presentes no armazenamento compartilhado e alterados intencionalmente não são revertidos automaticamente.

## Contato

**PENDENTE:** informar endereço de e-mail ou página pública de suporte que o publicador realmente controle.

## Verificação antes de usar esta minuta

- Confirmar empiricamente o fluxo completo de dados, inclusive dependências, logs de erro, cache e conexões HTTP(S).
- Confirmar se existe qualquer análise, publicidade, telemetria ou compartilhamento não listado.
- Não afirmar «não coletamos dados» sem auditar a implementação e os provedores externos.
- Publicar a versão final numa página HTTPS aberta, sem login, e adicionar a URL à Play Console e ao app quando apropriado.
