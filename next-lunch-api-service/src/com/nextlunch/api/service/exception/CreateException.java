package com.nextlunch.api.service.exception;

import com.nextlunch.api.service.exception.enums.CreateExceptionMessageEnum;

public class CreateException extends Exception {

	private static final long serialVersionUID = 7495016393091978867L;

	public CreateException(CreateExceptionMessageEnum  createUpdateExceptionMessage) {
		super(createUpdateExceptionMessage.name());
	}
	
	public CreateException(CreateExceptionMessageEnum  createUpdateExceptionMessage, Exception e) {
		super(createUpdateExceptionMessage.name(), e);
	}
}
