package gnu.mail.providers.imap;


import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;


public class TeeOutputStream
extends FilterOutputStream
{

  OutputStream a;

  OutputStream b;
  
  public TeeOutputStream(OutputStream o1,OutputStream o2)
  {
    super(o1);
    a=o1;
    b=o2;
  }

  public void write(int byt)
  throws IOException
  {
    a.write(byt);
    b.write(byt);
  }

  public void write(byte[] buf,int start,int len)
  throws IOException
  {
    a.write(buf,start,len);
    b.write(buf,start,len);
  }

  public void write(byte[] buf)
  throws IOException
  {
    a.write(buf);
    b.write(buf);
  }

  public void flush()
  throws IOException
  {
    a.flush();
    b.flush();
  }

  public void close()
  throws IOException
  {
    a.close();
    b.close();
  }
}
