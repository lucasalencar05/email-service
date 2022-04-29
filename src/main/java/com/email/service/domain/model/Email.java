package com.email.service.domain.model;

import lombok.Data;

/**
 * <p>
 * Entidade com dados do email 
 * </p>
 */

@Data
public class Email {

	private String from;

	private String to;

	private String cc;

	private String subject;

	private String message;

}
