import ChatApp.*;          // The package containing our stubs
import org.omg.CosNaming.*; // HelloClient will use the naming service.
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;     // All CORBA applications need these classes.
import org.omg.PortableServer.*;   
import org.omg.PortableServer.POA;
import java.util.*;

 
class ChatCallbackImpl extends ChatCallbackPOA
{
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    public void callback(String notification)
    {
        System.out.println(notification);
    }

    public void printGameArea(char gameArea[][], String xTeam, String oTeam) {
        
        //print game
    }
}

public class ChatClient
{
    static Chat chatImpl;
    String myName;
    
    public static void main(String args[])
    {
	try {
	    // create and initialize the ORB
	    ORB orb = ORB.init(args, null);

	    // create servant (impl) and register it with the ORB
	    ChatCallbackImpl chatCallbackImpl = new ChatCallbackImpl();
	    chatCallbackImpl.setORB(orb);

	    // get reference to RootPOA and activate the POAManager
	    POA rootpoa = 
		POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
	    
	    // get the root naming context 
	    org.omg.CORBA.Object objRef = 
		orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    
	    // resolve the object reference in naming
	    String name = "Chat";
	    chatImpl = ChatHelper.narrow(ncRef.resolve_str(name));
	    
	    // obtain callback reference for registration w/ server
	    org.omg.CORBA.Object ref = 
		rootpoa.servant_to_reference(chatCallbackImpl);
	    ChatCallback cref = ChatCallbackHelper.narrow(ref);
	    
	    // Application code goes below
        Scanner sc = new Scanner(System.in);
        String username = "";
        boolean ok = true;
	    System.out.println("\nWelcome to Othello! \nCommands: \njoin <your name> \nleave \nlist \npost <your message>");
        
        while(ok){
    
            String command = sc.next();
            switch (command) {
                case "join":
                    String user = sc.next();
                    if(chatImpl.join(cref, user))
                        username = name;
                    break;

                case "leave":
                    ok = false;
                    chatImpl.leave(cref);
                    break;

                case "list":
                    chatImpl.list(cref);
                    break;

                case "post":
                    chatImpl.say(cref, sc.nextLine());
                    break;

                default:
                    throw new IllegalArgumentException("Invalid command!");
            }
        }

	} catch(Exception e){
	    System.out.println("ERROR : " + e);
	    e.printStackTrace(System.out);
	}
    }
}
