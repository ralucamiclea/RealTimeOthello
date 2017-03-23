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

    public void printGameArea(char gameArea[][], String teamX, String teamO) {

        for(int i = 0; i < gameArea.length; i++){ //rows
            for(int j = 0; j < gameArea[0].length; j++) //cols
                if(gameArea[i][j] == 'o' || gameArea[i][j] == 'x')
                    System.out.print(gameArea[i][j]);
                else
                    System.out.print("_ ");
             System.out.print("\n");
        }
        System.out.println("Team X: " + teamX + "\nTeam O: " + teamO);
    }
}

public class ChatClient
{
    static Chat chatImpl;

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
        boolean game = false;
	    System.out.println("\nWelcome to Othello Chat!! \nCommands: \njoin <your name> \nleave \nlist \npost <your message> \nquit \nothello <color>");

        while(ok){

            System.out.print("% ");
            String command = sc.next();
            switch (command) {
                case "join":
                    String user = sc.next();
                    if(chatImpl.join(cref, user))
                        username = name;
                    break;

                case "leave":
                    chatImpl.leave(cref);
                    break;

                case "quit":
                    ok = false;
                    chatImpl.leave(cref);
                    break;

                case "list":
                    chatImpl.list(cref);
                    break;

                case "post":
                    chatImpl.say(cref, sc.nextLine());
                    break;

                case "othello":
                    if(sc.hasNext()){
                      char a = sc.next().charAt(0);
                      if (a != 'o' && a != 'x'){
                          System.out.println("Invalid command! Color can be 'o' or 'x'.");
                      }
                      else {
                        System.out.printf("your team: %c", a);
                        game = chatImpl.othelloStart(cref, a);

                        while(game){

                            System.out.print("%%% ");
                            String gamecommand = sc.next();

                            switch(gamecommand){
                                case "leaveGame":
                                    game = false;
                                    chatImpl.othelloLeave(cref);
                                    break;
                                case "insert":
                                    if(sc.hasNextInt() == false){
                                      System.out.println("Invalid command! Coordonates needed.");
                                    }
                                    else {
                                      int x  = sc.nextInt();
                                      if(sc.hasNextInt()){
                                        int y  = sc.nextInt();
                                            chatImpl.othelloInsert(cref, x, y);
                                      }
                                      else
                                        System.out.println("Invalid command! Coordonates needed.");
                                    }
                                    break;
                                default:
                                    System.out.println("Invalid command!");
                            }
                        }
                      }
                    }
                    else
                      System.out.println("Invalid command!");
                    break;

                default:
                    System.out.println("Invalid command!");
            }
        }

	} catch(Exception e){
	    System.out.println("ERROR : " + e);
	    e.printStackTrace(System.out);
	}
    }
}
