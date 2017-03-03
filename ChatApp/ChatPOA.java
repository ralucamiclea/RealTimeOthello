package ChatApp;


/**
* ChatApp/ChatPOA.java .
* Error reading Messages File.
* Error reading Messages File.
* Friday, March 3, 2017 at 9:22:06 AM Central European Standard Time
*/

public abstract class ChatPOA extends org.omg.PortableServer.Servant
 implements ChatApp.ChatOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("say", new java.lang.Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // ChatApp/Chat/say
       {
         ChatApp.ChatCallback objref = ChatApp.ChatCallbackHelper.read (in);
         String message = in.read_string ();
         String $result = null;
         $result = this.say (objref, message);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ChatApp/Chat:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Chat _this() 
  {
    return ChatHelper.narrow(
    super._this_object());
  }

  public Chat _this(org.omg.CORBA.ORB orb) 
  {
    return ChatHelper.narrow(
    super._this_object(orb));
  }


} // class ChatPOA
