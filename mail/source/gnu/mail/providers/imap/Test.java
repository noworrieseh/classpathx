package gnu.mail.providers.imap;

import java.lang.reflect.*;
import java.io.*;
import java.util.Properties;
import javax.activation.*;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;


/** this is a very basic testing class.
 * It does some stuff that should let you confirm it's working okay.
 * Run it from the command line.
 *
 * @author Nic Ferrier - Tapsell-Ferrier Limited
 */
public class Test
{

  //test specifiers

  static boolean authenticatedState=false;

  static boolean listAllFolders=true;

  static boolean selectedState=true;
  
  static boolean listUnderFolder=true;

  static boolean printRawStream=false;

  static boolean printContent=false;

  static boolean printSampleHeader=false;

  //the code

  static Folder toList=null;

  public static void main(String[] argv)
  {
    try
    {
      //create the store
      Properties props=new Properties();
      props.put("mail.imap.host","muddy-patch");
      props.put("mail.imap.port","143");

      //set the provider programattically (without javamail.providers)
      //We meed this during development - can't be arsed to create a jar
      //every time I compile!
      props.put("mail.imap.class","oje.mail.imap.IMAPStore");
      props.put("mail.store.protocol","imap");

      Session mailTest=Session.getInstance(props,null);
      Store store=mailTest.getStore("imap");
      store.connect("muddy-patch",143,"nferrier","car0linejane");

      //get the authenticated state
      if(authenticatedState)
      {
	OUT(0,"getting the authenticated state folder");
	Folder auth=store.getDefaultFolder();
	OUT(1,"testing open(READ_WRITE)");
	auth.open(Folder.READ_WRITE);
	OUT(1,"current mode: "+((auth.getMode()==Folder.READ_ONLY)?"READ_ONLY":"READ_WRITE"));
	try
	{
	  OUT(1,"testing open when already open");
	  auth.open(Folder.READ_ONLY);
	}
	catch(Exception e)
	{
	  e.printStackTrace();
	}
	OUT(1,"close(false) and open(READ_ONLY)");
	auth.close(false);
	auth.open(Folder.READ_ONLY);
	OUT(1,"and close again");
	auth.close(false);
	//get the complete list of all folders
	if(listAllFolders)
	{
	  OUT(2,"list all folders under the top folder");
	  doListAllFolders(auth);
	}
	OUT(0,"authenticated state folder tests done.");
      }

      //test a subfolder
      if(selectedState)
      {
	OUT(0,"getting the INBOX");
	Folder inbox=store.getFolder("INBOX");
	try
	{
	  OUT(1,"testing open(READ_WRITE)");
	  inbox.open(Folder.READ_WRITE);
	}
	catch(Exception e)
	{
	  e.printStackTrace();
	  OUT(1,"testing open(READ_ONLY)");
	  inbox.open(Folder.READ_ONLY);
	}
	OUT(1,"current mode: "+((inbox.getMode()==Folder.READ_ONLY)?"READ_ONLY":"READ_WRITE"));
	OUT(1,"testing getMessageCount(): "+inbox.getMessageCount());
	OUT(1,"testing getNewMessageCount(): "+inbox.getNewMessageCount());
	try
	{
	  OUT(1,"testing open when already open");
	  inbox.open(Folder.READ_ONLY);
	}
	catch(Exception e)
	{
	  e.printStackTrace();
	}
	OUT(1,"close(false) and open(READ_ONLY)");
	inbox.close(false);
	inbox.open(Folder.READ_ONLY);
	OUT(1,"and close again");
	inbox.close(false);
	//get the list of folders under another folder
	if(listUnderFolder)
	{
	  OUT(2,"list all the folders under the INBOX");
	  doListAllFolders(inbox);
	}

	//message handling stuff
	if(printSampleHeader || printContent || printRawStream)
	{
	  OUT(2,"open "+inbox.getName()+" for reading again");
	  inbox.open(Folder.READ_ONLY);
	  int lastMsg=inbox.getMessageCount();
	  OUT(2,"message handling tests in "+inbox.getName()+" using message:" +lastMsg);

	  OUT(2,"getMessage("+lastMsg+")");
	  MimeMessage mimemsg=(MimeMessage)inbox.getMessage(lastMsg);
	  //cache less message reading
	  if(printRawStream)
	  {
	    OUT(2,"getRawInputStream() [no caching]");
	    OUT(3,"the raw stream is: ");
	    printStream(mimemsg.getRawInputStream());
	  }
	  //headers
	  if(printSampleHeader)
	  {
	    OUT(2,"header handling");
	    Object from=mimemsg.getFrom();
	    OUT(3,"the message is from: "+from);
	  }
	  //get the content
	  if(printContent)
	  {
	    OUT(2,"content handling");
	    Object o=mimemsg.getContent();
	    OUT(3,"the content is: \n"+o);
	  }

	}
	OUT(0,"INBOX tests done.");
      }

    }
    catch(Throwable t)
    {
      t.printStackTrace(System.err);
    }
  }

  /** a test of IMAP:
   * <pre>
   *     LIST "" "*"
   * </pre>
   * All the folders that exist in the specified folder
   * are printed to the out stream.
   *
   * @param listIn the folder to do the list in 
   */
  public static void doListAllFolders(Folder listIn)
  throws MessagingException
  {
    Folder[] list=listIn.list();
    for(int i=0; i<list.length; i++)
    {
      for(int j=0; j<lastLevel; j++)
      System.out.print("\t");
      //print the folder info
      System.out.print("  a folder: "+list[i]);
      if(list[i].exists())
      System.out.println(" does exist");
      else
      System.out.println(" does not exist");
    }
  }

  /** read a file until EOF.
   * The stream is read in blocks of 5Kb.
   *
   * @param in the stream to read
   */
  public static void printStream(InputStream in)
  {
    int red=0;
    try
    {
      byte[] buf=new byte[200];
      red=in.read(buf,0,200);
      while(red>-1)
      {
	System.out.write(buf,0,red);
	red=in.read(buf,0,200);
      }
    }
    catch(Throwable e)
    {
      e.printStackTrace(System.err);
    }
  }


  //this is the debug message presentation stuff

  static int lastLevel=-1;

  public static void OUT(int level,String msg)
  {
    if(level>lastLevel)
    {
      System.out.print(">");
    }
    else if(level<lastLevel)
    {
      System.out.print("\n>");
    }
    else
    {
      System.out.print(">");
    }
    for(int i=0; i<level; i++)
    System.out.print("\t");
    lastLevel=level;
    System.out.println(msg);
  }
}
