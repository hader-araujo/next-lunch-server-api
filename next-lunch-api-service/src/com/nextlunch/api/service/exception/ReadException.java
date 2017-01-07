package com.nextlunch.api.service.exception;

import com.nextlunch.api.service.exception.enums.ReadExceptionMessageEnum;

public class ReadException extends Exception {

	private static final long serialVersionUID = 7152995438364969899L;

	public ReadException(ReadExceptionMessageEnum readExceptionMessage) {
		super(readExceptionMessage.name());
	}
	
	public ReadException(ReadExceptionMessageEnum readExceptionMessage, Exception e) {
		super(readExceptionMessage.name(), e);
	}

}
