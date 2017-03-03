package ChatApp;

/**
* ChatApp/ChatCallbackHolder.java .
* Error reading Messages File.
* Error reading Messages File.
* Friday, March 3, 2017 at 9:22:06 AM Central European Standard Time
*/

public final class ChatCallbackHolder implements org.omg.CORBA.portable.Streamable
{
  public ChatApp.ChatCallback value = null;

  public ChatCallbackHolder ()
  {
  }

  public ChatCallbackHolder (ChatApp.ChatCallback initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ChatApp.ChatCallbackHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ChatApp.ChatCallbackHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ChatApp.ChatCallbackHelper.type ();
  }

}
