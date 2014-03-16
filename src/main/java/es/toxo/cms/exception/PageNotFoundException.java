package es.toxo.cms.exception;

public class PageNotFoundException extends Exception implements UserInfoCapableException{
	private static final long serialVersionUID = 1L;
	
	public PageNotFoundException(String message) {
		super(message);
	}

}
