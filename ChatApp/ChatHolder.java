package ChatApp;

/**
* ChatApp/ChatHolder.java .
* Error reading Messages File.
* Error reading Messages File.
* Sunday, March 5, 2017 at 9:22:38 PM Central European Standard Time
*/

public final class ChatHolder implements org.omg.CORBA.portable.Streamable
{
  public ChatApp.Chat value = null;

  public ChatHolder ()
  {
  }

  public ChatHolder (ChatApp.Chat initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ChatApp.ChatHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ChatApp.ChatHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ChatApp.ChatHelper.type ();
  }

}
