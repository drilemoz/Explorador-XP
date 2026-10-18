# alpha.135: nome maior e três selos na mesma linha

## Interface do visualizador de APK

- Aumenta o nome do aplicativo de **16 sp para 21 sp** mantendo até duas linhas com reticências se necessário.
- Mantém o ícone ao lado do nome e remove o selo do APK da linha do título.
- Exibe abaixo três selos, em ordem, **versão instalada (azul)**, **situação** e **versão do arquivo APK**.
- A situação e a versão do arquivo continuam com cor baseada em `apkVersionTone`: verde (novo/atualização), azul (igual) ou vermelho (anterior).
- Sem aplicativo instalado, apresenta apenas situação e versão do APK, sem inventar versão instalada.
- A linha dos selos permite rolagem horizontal se a tela for estreita ou a fonte estiver aumentada, sem quebrar os selos em duas linhas.
- Sem mudança nos controles do APK: **Abrir, Gerenciar, Desinstalar, Instalar/Atualizar**; mesma validação de assinatura, compatibilidade e downgrade.

## Validação

- `python tools/validate_source.py` e verificações Kotlin/JSON/XML executadas localmente, conforme disponibilidade.
- Build completo Android e verificação visual em aparelho físico ainda pendentes.
