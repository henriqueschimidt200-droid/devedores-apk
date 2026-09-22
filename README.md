# Devedores — Android nativo

Aplicativo Android nativo para cadastro de clientes, empréstimos, juros, parcelas, pagamentos, documentos, anotações, calendário e lembretes.

## Alarmes
- Usa AlarmManager do Android.
- Reagencia alarmes após reinicialização do aparelho.
- No Android 12+, solicita a permissão especial "Alarmes e lembretes" para alarmes exatos.
- No Android 13+, solicita permissão de notificações.

## Persistência
Os dados de clientes, empréstimos, pagamentos, lembretes e URIs de documentos são salvos automaticamente em armazenamento privado do aplicativo.

## Gerar APK
A forma mais simples neste ambiente é enviar este projeto para GitHub e executar o workflow `.github/workflows/android.yml`. O workflow gera `app-debug.apk` como artefato.
