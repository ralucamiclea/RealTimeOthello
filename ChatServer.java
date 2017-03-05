import ChatApp.*;          // The package containing our stubs. 
import org.omg.CosNaming.*; // HelloServer will use the naming service. 
import org.omg.CosNaming.NamingContextPackage.*; // ..for exceptions. 
import org.omg.CORBA.*;     // All CORBA applications need these classes. 
import org.omg.PortableServer.*;   
import org.omg.PortableServer.POA;
import java.util.*;

class User {
    
    private String name;
    private ChatCallback obj;
        
    public User(String name, ChatCallback obj) {
    
        this.name = name;
        this.obj = obj;
    }

    public String getName(){ 
        return name; 
    }
    public ChatCallback getCallObj() {
        return obj; 
    }
    public boolean checkName(String username){
        if(username.equals(this.name))
            return true;
    return false;
    }
}
 
class ChatImpl extends ChatPOA
{
    private ORB orb;
    //vector of user objects 
    private Vector<User> users = new Vector<User>();

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }

    private int lookupUser(ChatCallback callobj){
        for(int i = 0; i < users.size(); i++)
            if(users.get(i).getCallObj().equals(callobj))
                return i;
        return -1;
    }

    public void say(ChatCallback callobj, String msg)
    {

        if(lookupUser(callobj) == -1) {
            callobj.callback("\nJoin first!");
        }
        else {
        //post to sender
        callobj.callback(users.get(lookupUser(callobj)).getName() + " said: " + msg);      
        //post to other users
        post(callobj, users.get(lookupUser(callobj)).getName() + " said: " + msg);  
        }
    }

    public void post(ChatCallback callobj, String msg) {
        int index = lookupUser(callobj);
        for(int i = 0; i < users.size(); i++)
            if(index != i) //if other user than sender
                users.get(i).getCallObj().callback(msg);
    } 

    public boolean join(ChatCallback callobj, String name) {
        if(lookupUser(callobj) != -1) {
            callobj.callback("\nYou have already joined!");
            return false;
        }

        for(int i = 0; i < users.size(); i++)
            if(users.get(i).checkName(name)) {
            callobj.callback("\nUsername already taken!");
            return false;
        }

        users.add(new User(name, callobj));
        callobj.callback("\nWelcome, " + name + "!");
        post(callobj, "\n" + name + " joined!");
        return true;
        
    }

    public void leave(ChatCallback callobj) {
        if(lookupUser(callobj) == -1) {
            callobj.callback("\nYou never joined!");
        }
        else {
            post(callobj, users.get(lookupUser(callobj)).getName() + " left!");
            callobj.callback("\nWe are sorry to see you go...");
            users.remove(lookupUser(callobj));
        }
    }

    public void list(ChatCallback callobj) {
        if(users.size() != 0) {
            callobj.callback("\nActive users: ");
            
            for(int i = 0; i < users.size(); i++)
                callobj.callback(users.get(i).getName() + "\n");
        }
        else {
            callobj.callback("\nNo active users!");
        }
    }
}

public class ChatServer 
{
    public static void main(String args[]) 
    {
	try { 
	    // create and initialize the ORB
	    ORB orb = ORB.init(args, null); 

	    // create servant (impl) and register it with the ORB
	    ChatImpl chatImpl = new ChatImpl();
	    chatImpl.setORB(orb); 

	    // get reference to rootpoa & activate the POAManager
	    POA rootpoa = 
		POAHelper.narrow(orb.resolve_initial_references("RootPOA"));  
	    rootpoa.the_POAManager().activate(); 

	    // get the root naming context
	    org.omg.CORBA.Object objRef = 
		           orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	    // obtain object reference from the servant (impl)
	    org.omg.CORBA.Object ref = 
		rootpoa.servant_to_reference(chatImpl);
	    Chat cref = ChatHelper.narrow(ref);

	    // bind the object reference in naming
	    String name = "Chat";
	    NameComponent path[] = ncRef.to_name(name);
	    ncRef.rebind(path, cref);

	    // Application code goes below
	    System.out.println("ChatServer ready and waiting ...");
	    
	    // wait for invocations from clients
	    orb.run();
	}
	    
	catch(Exception e) {
	    System.err.println("ERROR : " + e);
	    e.printStackTrace(System.out);
	}

	System.out.println("ChatServer Exiting ...");
    }

}
