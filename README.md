# email-service

## 1 - Descrição do Projeto 
Serviço API REST utilizado para envio de emails.

## 2 - Objetivo do Projeto 

Manter um padrão utilização dos serviços de envio de email.

### 3 - - Variáveis de ambiente 
```yaml

....

  spring.mail.host: ${SERVIDOR_EMAIL:smtpapp.meu.smtp.br}
  spring.mail.port : ${PORTA_SERVIDOR_EMAIL:25}
  spring.mail.protocol : ${PROTOCOLO_SERVIDOR_EMAIL:smtp}
  defaultEncoding: ${ENCODE_SERVIDOR_EMAIL:UTF-8}

....
```
## 5 - Observações importantes
Caso as variáveis acima sejam definidas no ambiente, os valores indicados nessas variáveis serão utilizados.
