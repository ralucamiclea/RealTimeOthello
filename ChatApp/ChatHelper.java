package ChatApp;


/**
* ChatApp/ChatHelper.java .
* Error reading Messages File.
* Error reading Messages File.
* Friday, March 3, 2017 at 9:22:06 AM Central European Standard Time
*/

abstract public class ChatHelper
{
  private static String  _id = "IDL:ChatApp/Chat:1.0";

  public static void insert (org.omg.CORBA.Any a, ChatApp.Chat that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static ChatApp.Chat extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (ChatApp.ChatHelper.id (), "Chat");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static ChatApp.Chat read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_ChatStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, ChatApp.Chat value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static ChatApp.Chat narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof ChatApp.Chat)
      return (ChatApp.Chat)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      ChatApp._ChatStub stub = new ChatApp._ChatStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static ChatApp.Chat unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof ChatApp.Chat)
      return (ChatApp.Chat)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      ChatApp._ChatStub stub = new ChatApp._ChatStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
