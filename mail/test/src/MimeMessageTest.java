import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MimeMessageTest
  extends TestCase
{
	
	private Session session;
  private URLName url;
  private Store store;
  private Folder folder;
	private int msgnum;
	private MimeMessage message;

	public MimeMessageTest(String name, String url, int msgnum)
	{
    super(name);
    this.url = new URLName(url);
		this.msgnum = msgnum;
	}

	protected void setUp()
	{
		session = Session.getInstance(System.getProperties());
    try
    {
      store = session.getStore(url);
      assertNotNull(store);
      folder = store.getFolder(url.getFile());
      assertNotNull(folder);
			store.connect();
			folder.open(Folder.READ_ONLY);
			message = (MimeMessage)folder.getMessage(msgnum);
    }
    catch (MessagingException e)
    {
      fail(e.getMessage());
    }
	}

	protected void tearDown()
	{
    if (folder!=null)
    {
      try
      {
        folder.close(false);
      }
      catch (MessagingException e)
      {
      }
    }
    if (store!=null)
    {
      try
      {
        store.close();
      }
      catch (MessagingException e)
      {
      }
    }
    url = null;
		message = null;
    folder = null;
    store = null;
		session = null;
	}

	public void testReadMessage()
	{
    try
    {
			assertNotNull(message.getFrom());
			assertNotNull(message.getAllRecipients());
			testContent(message.getContent());
    }
    catch (MessagingException e)
    {
      fail(e.getMessage());
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }
	}

	void testContent(Object content)
		throws MessagingException, IOException
	{
		assertNotNull(content);
		if (content instanceof String)
		{
			System.out.println((String)content);
		}
		else if (content instanceof MimeMultipart)
		{
			MimeMultipart mp = (MimeMultipart)content;
			int count = mp.getCount();
			assertTrue(count>0);
			for (int i=0; i<count; i++)
			{
				MimeBodyPart bp = (MimeBodyPart)mp.getBodyPart(i);
				assertNotNull(bp);
				testContent(bp.getContent());
			}
		}
		else
			System.out.println("content="+content);
	}

  static Test suite(String url, int msgnum)
  {
    TestSuite suite = new TestSuite();
    suite.addTest(new MimeMessageTest("testReadMessage", url, msgnum));
    return suite;
  }

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
    try
    {
      BufferedReader r = new BufferedReader(new FileReader("message-urls"));
      for (String line = r.readLine(); line!=null; line = r.readLine())
			{
				int si = line.indexOf(' ');
				String url = line.substring(0, si);
				int msgnum = Integer.parseInt(line.substring(si+1));
        suite.addTest(suite(url, msgnum));
			}
      r.close();
    }
    catch (IOException e)
    {
      System.err.println("No folder URLs");
    }
    return suite;
	}
	
}
