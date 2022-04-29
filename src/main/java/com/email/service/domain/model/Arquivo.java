package com.email.service.domain.model;

import lombok.Data;

/**
 * <p>
 * Entidade para dados do anexo de email
 * </p>
 * 
 */

@Data
public class Arquivo {

	byte[] bytes;

	String contentType;

	String originalFilename;

}
