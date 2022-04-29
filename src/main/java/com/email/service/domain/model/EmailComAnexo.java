package com.email.service.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * Entidade com dados do anexo do email
 * </p>
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class EmailComAnexo extends Email {

	private Arquivo file;

}
