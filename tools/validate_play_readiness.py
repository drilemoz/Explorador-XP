#!/usr/bin/env python3
"""Verificações locais mínimas do projeto preparado para o Google Play.
NÃO substitui testes reais do Android, auditoria de dados ou aprovação da Play.
"""
from pathlib import Path
import re
import xml.etree.ElementTree as ET
root=Path(__file__).resolve().parents[1]
gradle=(root/'app/build.gradle.kts').read_text()
bench=(root/'baselineprofile/build.gradle.kts').read_text()
main=(root/'build.gradle.kts').read_text()
manifest=(root/'app/src/main/AndroidManifest.xml').read_text()
workflow=(root/'.github/workflows/google-play-aab.yml').read_text()
about=(root/'app/src/main/java/com/exploradorxp/app/ExplorerScreen.kt').read_text()
assert 'compileSdk = 36' in gradle and 'targetSdk = 36' in gradle
assert 'compileSdk = 36' in bench and 'targetSdk = 36' in bench
assert 'minSdk = 26' in gradle
assert 'version "8.13.2"' in main
assert 'isMinifyEnabled = true' in gradle and 'isShrinkResources = true' in gradle
assert 'signingConfig = signingConfigs.getByName("permanentRelease")' in gradle
assert 'ANDROID_KEYSTORE_PATH' in gradle and 'ANDROID_KEYSTORE_BASE64' in workflow
assert ':app:bundleRelease' in workflow and ':app:lintRelease' in workflow and ':app:testDebugUnitTest' in workflow
assert 'actions/upload-artifact@v4' in workflow and 'workflow_dispatch:' in workflow
assert 'google-play-aab.yml' not in (root/'.github/workflows/gerar-apk.yml').read_text()
assert 'on:\n  workflow_dispatch:' in workflow and 'contents: read' in workflow
assert 'Privacidade e permissões' in about and 'Wikimedia Commons' in about
for required in ('MANAGE_EXTERNAL_STORAGE','QUERY_ALL_PACKAGES','REQUEST_INSTALL_PACKAGES'):
    assert required in manifest
assert 'android:usesCleartextTraffic="false"' in manifest
ET.fromstring(manifest)
assert (root/'docs/GOOGLE_PLAY_RELEASE.md').exists()
assert (root/'docs/PRIVACIDADE_RASCUNHO.md').exists()
assert 'NÃO publicada' in (root/'docs/PRIVACIDADE_RASCUNHO.md').read_text()
assert re.search(r'^0\.1\.0-alpha\.140$',(root/'VERSION').read_text().strip())
print('OK: pré-checagem Play (API36, assinatura, bundle, docs, manifesto). Compilação e aprovação não verificadas.')
