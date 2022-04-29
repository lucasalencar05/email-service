package com.email.service.controller;

import com.email.service.domain.exception.EmailException;
import com.email.service.domain.model.Email;
import com.email.service.domain.model.EmailComAnexo;
import com.email.service.domain.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class EmailController {

	@Autowired
	private EmailService service;

	/**
	 * <p>
	 * Envia email no formato HTML. Permite anexar um arquivo (MultipartFile)
	 * </p>
	 * @throws IOException 
	 * @throws EmailException 
	 * @throws Exception
	 */
	@PostMapping(value = "/form-data/html", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void enviarlHtmlFormData(Email email, MultipartFile file) throws MailException, EmailException, IOException {

		service.enviarEmailFormData(email, file, true);

	}

	/**
	 * <p>
	 * Envia email no formato TEXT. Permite anexar um arquivo (MultipartFile)
	 * </p>
	 * @throws Exception
	 */
	@PostMapping(value = "/form-data/text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void enviarTextFormData(Email email, MultipartFile file) throws MailException, EmailException, IOException {

		service.enviarEmailFormData(email, file, false);

	}

	/**
	 * <p>
	 * Envia email no formato HTML. Permite anexar um arquivo (Bytes)
	 * </p>
	 * @throws IOException 
	 * @throws EmailException 
	 * @throws MessagingException 
	 * @throws Exception
	 */
	@PostMapping(value = "/json/html")
	public void enviarHtmlJson(@RequestBody EmailComAnexo email) throws MailException, MessagingException, EmailException, IOException {

		service.enviarEmailJson(email, true);

	}

	/**
	 * <p>
	 * Envia email no formato TEXT. Permite anexar um arquivo (Bytes)
	 * </p>
	 * @throws IOException 
	 * @throws EmailException 
	 * @throws MessagingException 
	 * @throws Exception
	 */
	@PostMapping(value = "/json/text")
	public void enviarTextJson(@RequestBody EmailComAnexo email) throws MailException, MessagingException, EmailException, IOException {

		service.enviarEmailJson(email, false);

	}

}
