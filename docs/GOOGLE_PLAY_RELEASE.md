# Preparação Google Play | Explorador XP 0.1.0-alpha.140

**Estado:** código preparado para testes e submissão, **não publicado e não aprovado**.

## Build e identificadores

- Pacote estável: `com.exploradorxp.app`. Não alterar após criar o aplicativo na Play Console.
- `compileSdk/targetSdk 36` (Android 16), `minSdk 26`.
- AGP `8.13.2`, Gradle `8.13`, JDK `17`, Kotlin `2.0.21`. Verificar funcionamento das dependências reais.
- `versionName 0.1.0-alpha.140`, `versionCode 140`. Este mesmo código não pode ser reutilizado depois de enviado ao Play.
- O Android App Bundle é gerado no **Actions > Gerar AAB para Google Play > Run workflow**; o `.aab` assinado e `.sha256` ficam em **Artifacts**. Publicação **não é automática**.
- Use Play App Signing. O arquivo gerado é assinado com a **chave de upload** fornecida pelos Secrets existentes: `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`. A **mesma chave existente** deve ser mantida se for desejada atualização direta da distribuição APK atual; a assinatura entregue pelo Play aos usuários poderá ser diferente.
- **Nunca** salvar a chave JKS/P12, senhas nem Secrets no repositório, ZIP público ou Drive compartilhado.

## Declarações na Play Console (revisão obrigatória)

1. **MANAGE_EXTERNAL_STORAGE**: finalidade central é gerenciamento de arquivos e pastas no armazenamento compartilhado: navegar, copiar, mover, excluir, compactar, pesquisar. Permissão especial foi mantida porque o fluxo atual depende de acesso amplo e ainda não possui modo SAF equivalente para todas as operações. A aprovação da Play **não é garantida**. Apresentar vídeo/capturas e justificativa quando solicitado.
2. **QUERY_ALL_PACKAGES**: o módulo **Aplicativos** lista e gerencia o conjunto instalado, pesquisa e compara APKs com versões instaladas. A necessidade de visibilidade ampla deve ser explicada e demonstrada; alternativamente, redesenhar o módulo para visibilidade restrita se a Play negar.
3. **REQUEST_INSTALL_PACKAGES**: necessário para instalação **iniciada pelo usuário** de APKs encontrados no gerenciador. Não usar para autoatualização clandestina ou silenciosa, nem distribuição de bundles embutidos. Confirmar formulário e classificação.
4. **PACKAGE_USAGE_STATS**: acesso opcional via Configurações do Android para estatísticas de armazenamento de aplicativos. Confirmar coleta/uso real e declaração se a Play Console exigir.
5. **READ_BASIC_PHONE_STATE**, **POST_NOTIFICATIONS**, **FOREGROUND_SERVICE_MEDIA_PLAYBACK**: validar justificativas, telas de autorização e funcionalidades relacionadas.
6. **INTERNET / ACCESS_NETWORK_STATE**: consultas externas opcionais de identificação de aparelho (`storage.googleapis.com/play_public/supported_devices.csv`, Wikidata, Wikimedia Commons). Não declarar funcionamento 100% offline. IP e parâmetros de consulta podem ficar visíveis aos provedores dessas consultas.
7. **REQUEST_DELETE_PACKAGES**: exclusão de aplicativos ocorre mediante confirmação/sistema Android; verificar política aplicável.

**Importante:** não afirmar que a Google Play aprovou permissões nem preencher questionários automaticamente com respostas não verificadas.

## Privacidade e conteúdo da loja

- Revisar e publicar a minuta `docs/PRIVACIDADE_RASCUNHO.md` como **página HTTPS pública**, sem autenticação, que não seja PDF nem editável. Inserir o link real na ficha da Play. A seção in-app de privacidade é informativa e não substitui a URL pública.
- Verificar formulário **Segurança dos dados (Data safety)** por comportamento real (inclusive bibliotecas e provedores), não marcar automaticamente «nenhum dado coletado».
- Completar categoria, contato de suporte, classificação indicativa, público-alvo, conteúdo, declaração de anúncios e ficha da loja.
- Revisar rascunho de descrição em `docs/GOOGLE_PLAY_FICHA.md`.
- Preparar ícone 512×512, banner 1024×500 e screenshots **reais** do aplicativo em execução, com textos e recursos atuais (sem Rede).
- Conta pessoal criada após 13/11/2023: verificar requisito de teste fechado de **12 pessoas por 14 dias consecutivos** e solicitação de acesso a produção.

## Roteiro de verificação técnica antes de publicar

- [ ] `:app:testDebugUnitTest`, `:app:lintRelease` e `:app:bundleRelease` concluídos no GitHub Actions.
- [ ] Validar presença de `base/manifest/AndroidManifest.xml` e integridade do AAB; registrar SHA-256 e certificado/chave de upload.
- [ ] Instalar o APK de teste em Android 13, 15 e **16**; verificar permissões, navegação de pastas e SAF, atualização de APK, apps instalados e extração ZIP.
- [ ] Testar barra de navegação e **edge-to-edge** em Android 16 (não há mais opt-out), além de **voltar preditivo**, rotação e tablets.
- [ ] Confirmar o comportamento de `MANAGE_EXTERNAL_STORAGE` na distribuição Play e se a Play aprova o uso.
- [ ] Conferir se dependências contêm `.so` nativo; se houver, testar alinhamento/compatibilidade com páginas de 16 KB.
- [ ] Subir AAB para **teste interno** no Play Console e resolver relatórios de pré-lançamento antes de produção.
- [ ] Garantir que o `versionCode` do próximo envio seja maior que 140.

## Fontes oficiais (consultadas em 31/10/2026)

- https://support.google.com/googleplay/android-developer/answer/11926878?hl=pt-br
- https://support.google.com/googleplay/android-developer/answer/10467955?hl=pt-br
- https://support.google.com/googleplay/android-developer/answer/18265346
- https://support.google.com/googleplay/android-developer/answer/12085295?hl=pt-BR
- https://support.google.com/googleplay/android-developer/answer/9859455?hl=en
- https://support.google.com/googleplay/android-developer/answer/14151465?hl=pt-BR
- https://developer.android.com/about/versions/16/behavior-changes-16
- https://developer.android.com/build/releases/agp-8-13-0-release-notes
