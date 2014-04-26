package akosha.demo.signin;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

public class NopageException extends Exception {

	private String s;

	public NopageException(String s) {
		
		this.s = s;
	}
	
	
}
