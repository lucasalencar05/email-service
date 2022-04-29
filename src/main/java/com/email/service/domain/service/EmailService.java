package com.email.service.domain.service;

import com.email.service.domain.exception.EmailException;
import com.email.service.domain.model.Arquivo;
import com.email.service.domain.model.Email;
import com.email.service.domain.model.EmailComAnexo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * <p>
	 * Executa o serviço de envio de email, caso tenha anexo converte em bytes
	 * </p>
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Boolean enviarEmailFormData(Email email, MultipartFile file, Boolean isHtml)
			throws EmailException, IOException {

		log.debug("Recebendo os dados para o envio do email....");
		
		EmailComAnexo emailComAnexo = new EmailComAnexo();
		emailComAnexo.setCc(email.getCc());
		emailComAnexo.setFrom(email.getFrom());
		emailComAnexo.setMessage(email.getMessage());
		emailComAnexo.setSubject(email.getSubject());
		emailComAnexo.setTo(email.getTo());

		if (file != null && file.getSize() > 0) {
			log.debug("Convertendo o arquivo MultipartFile para bytes....");
			Arquivo arquivo = new Arquivo();
			arquivo.setBytes(file.getBytes());
			arquivo.setContentType(file.getContentType());
			arquivo.setOriginalFilename(file.getOriginalFilename());
			emailComAnexo.setFile(arquivo);
		}

		try {
			this.enviarEmailJson(emailComAnexo, isHtml);
		}
		catch(Exception e) {
			throw new EmailException(e.getMessage());
		}
		return true;

	}

	/**
	 * <p>
	 * Envia um email conforme os parametros recebidos 
	 * </p>
	 * @throws MessagingException 
	 * @throws EmailException 
	 * @throws IOException 
	 * @throws Exception 
	 */
	public Boolean enviarEmailJson(EmailComAnexo email, boolean isHtml) throws MessagingException, EmailException, IOException
			 {

		log.debug("Criando objetos para envio de email....");

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		log.debug("Validando as informações obrigatórias para envio de email....");

		validaDados(email);
		 
		try {
			helper.setTo(InternetAddress.parse(email.getTo()));
			if (email.getCc() != null && !email.getCc().trim().isEmpty()) {
					helper.setCc(InternetAddress.parse(email.getCc()));
				}
		} catch (AddressException ex) {
			throw new EmailException(
					"Endereços de email (TO ou CC) estão incorretos. Exemplo de formato correto: test@test.br,test@test.br - "
							+ ex.getMessage());
		}

		helper.setReplyTo(email.getFrom());
		helper.setFrom(email.getFrom());
		helper.setSubject(email.getSubject());
		helper.setText(email.getMessage(), isHtml);

		MimeMultipart multipart = new MimeMultipart();

		BodyPart messageBodyPart = new MimeBodyPart();

		if (isHtml) {
			messageBodyPart.setHeader("Content-Type", "text/html; charset=UTF-8");
			messageBodyPart.setContent(email.getMessage(), "text/html; charset=utf-8");
		} else {
			messageBodyPart.setHeader("Content-Type", "text/plain; charset=UTF-8");
			messageBodyPart.setContent(email.getMessage(), "text/plain; charset=utf-8");
		}

		multipart.addBodyPart(messageBodyPart);

		if (email.getFile() != null && email.getFile().getBytes().length > 0) {

			log.debug("Anexando o arquivo ao objeto para ser enviado como anexo....");
			
			messageBodyPart = new MimeBodyPart();

			InputStream inputStream = new ByteArrayInputStream(email.getFile().getBytes());

			DataSource ds = new ByteArrayDataSource(inputStream, email.getFile().getContentType());
			messageBodyPart.setDataHandler(new DataHandler(ds));
			messageBodyPart.setDescription(email.getFile().getOriginalFilename());
			messageBodyPart.setFileName(email.getFile().getOriginalFilename());
			messageBodyPart.setDisposition(javax.mail.Part.ATTACHMENT);

			multipart.addBodyPart(messageBodyPart);

		}

		message.setContent(multipart);

		try {
			log.debug("Enviando o email....");

			mailSender.send(message);
		} catch (MailSendException ex) {
			throw new EmailException("Erro ao enviar email from: [" + email.getFrom() + "] " + "to: [" + email.getTo()
					+ "] " + (email.getCc() != null ? "cc: [" + email.getCc() + "]" : "") + " - " + ex.getMessage());
		}

		return true;

	}

	
	private void validaDados(EmailComAnexo email) throws EmailException {
		
		if (email.getTo() == null || email.getTo().trim().isEmpty()) 
			throw new EmailException("Destinatário do email (to) não informado.");
		if (email.getFrom() == null || email.getFrom().trim().isEmpty())
			throw new EmailException("Origem do email (from) não informada.");
		if (email.getSubject() == null)
			throw new EmailException("Assunto do email (subject) não informado.");
		if (email.getMessage() == null)
			throw new EmailException("Mensagem do email (message) não informado.");
		if(!isValidEmailAddress(email.getTo()))
			throw new EmailException("Endereço do destinatário do email inválido [" + email.getTo() + "]");
		if(!isValidEmailAddress(email.getFrom()))
			throw new EmailException("Endereço de origem do email inválido [" + email.getFrom() + "]");
		
	}
	
	private static boolean isValidEmailAddress(String email) {
	    List<String> list = new ArrayList<>(Arrays.asList(email.split(",")));
	    for (String itemEmail : list) {
		    try {
		    	InternetAddress emailAddr = new InternetAddress(itemEmail);
		        emailAddr.validate();
		    } catch (AddressException ex) {
		        return false;
		    }
		}
	    return true;
	}	
}
