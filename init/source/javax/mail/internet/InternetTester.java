/* InternetTester.java */

/* Liscense goes here. */


/**
 * This class is to test the classes in the javax.mail.internet package
 * As each class in the package nears completion, the test method for it 
 * ought to be fleshed out.
 *
 * @author Joey Lesh (joey@gnu.org)
 */
public class InternetTester {

	/* A value that is set to make it easy to do time/memory comparisons on
	   n items */
	int iterations
	
	public static void main(String[] argv) {
	
		System.out.print("MimePart test:");
		if (InternetTester.testMimePart()) 
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("ContentType test:");
		if (testContentType()) 
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("HeaderTokenizer test:");
		if (testHeaderTokenizer())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("HeaderTokenizer.Token test:");
		if (testHeaderTokenizerToken())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("InternetAddress test:");
		if (testInternetAddress())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		
		System.out.print("InternetHeaders test:");
		if (testInternetHeaders())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("MimeBodyPart test:");
		if (testMimeBodyPart())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("MimeMessage test:");
		if (testMimeMessage()) 
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("MimeMessage.RecipientType test:");
		if (testMimeMessageRecipientType())
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("MimeMultipart test:");
		if (testMimeMultipart())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("MimePartDataSource test:");
		if (testMimePartDataSource())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("MimeUtility test:");
		if (testMimeUtility())  
			System.out.println(" succeeded!");
		else System.out.println(" failed :(");
		
		System.out.print("NewsAddress test:");
		if (testNewsAddress())  
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("ParameterList test:");
		if (testParameterList()) 
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("AddressException test:");
		if (testAddressException()) 
			System.out.println(" succeeded!") ;
		else System.out.println(" failed :(");
		
		System.out.print("ParseException test:");
		if	(testParseException()) 
			System.out.println(" succeeded!");
		else System.out.println(" failed :(");
	}
	
	public static boolean testAddressException() {
		return true;
	}
	
	public static boolean testAsciiOutputStream() {
		return true;
	}

	public static boolean testContentType() {
		return true;
	}

	public static boolean testInternetAddress() {
		String personal = "Joey Lesh";
		String email = "joey@gnu.org";
		try {
			InternetAddress address = new InternetAddress();
			//NYI address = new InternetAddress("Parseme");
			InternetAddress address = new InternetAddress("joey@gnu.org", "\"Joey Lesh\"");
			if (!address.equals(new InternetAddress("joey@gnu.org", "Joey Lesh")))
				return false;
			if (!email.equals(address.getAddress()))
				return false;
			if (!personal.equals(address.getPersonal()))
				return false;
			if (!address.getType().equals("rfc822"))
				return false;
			if (address.hashCode() != 0) // this will be changed.
				return false;
			// try some stuff.
			address.setAddress("jtuttle@gnu.org");
			
			// Do some stress testing.
			String tmpAddresses;
			for (int i = 0 ; i < this.iterations;i++) {
				tmpAddresses.concat("Funky Flash" + i + " flash." + i + "@freesoftware.org");
			}
			/*
			  The parse stuff isn't done yet.
			  InternetAddress[] addresses = InternetAddress.parse(tmpAddresses);
			  for (i = 0; i < this.iterations; i++) {
			  addresses[i].toString();
			  }
			*/
		   
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean testInternetHeaders() {
		return true;
	}

	public static boolean testMimeBodyPart() {
		return true;
	}

	public static boolean testMimeMessage() {
		return true;
	}

	public static boolean testMimeMultipart() {
		return true;
	}

	public static boolean testMimePart() {
		return true;
	}

	public static boolean testMimePartDataSource() {
		return true;
	}

	public static boolean testMimeUtility() {
		return true;
	}

	public static boolean testNewsAddress() {
		return true;
	}

	public static boolean testParameterList() {
		return true;
	}

	public static boolean testParseException() {
		return true;
	}

	public static boolean testUniqueValue() {
		return true;
	}

	public static boolean testHeaderTokenizer() {
		return true;
	}
	
	public static boolean testHeaderTokenizerToken() {
		return true;
	}
	
	public static boolean testMimeMessageRecipientType() {
		return true;
	}

	public static boolean testhdr() {
		return true;
	}

	public static boolean testmatchEnum() {
		return true;
	}

	public static boolean testContentDisposition() {
		return true;
	}

}// InternetTester


