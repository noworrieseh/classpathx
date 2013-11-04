import junit.framework.Test;
import junit.framework.TestSuite;
//import junit.swingui.TestRunner;
import junit.textui.TestRunner;

public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(SessionTest.suite());
		suite.addTest(TransportTest.suite());
		suite.addTest(StoreTest.suite());
		suite.addTest(FolderTest.suite());
		suite.addTest(NonFolderTest.suite());
		suite.addTest(MimeMessageTest.suite());
		return suite;
	}

	public static void main(String[] args)
	{
		//TestRunner.run(AllTests.class);
		TestRunner.run(suite());
	}
	
}
