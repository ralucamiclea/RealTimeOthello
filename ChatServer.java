import ChatApp.*;          // The package containing our stubs.
import org.omg.CosNaming.*; // HelloServer will use the naming service.
import org.omg.CosNaming.NamingContextPackage.*; // ..for exceptions.
import org.omg.CORBA.*;     // All CORBA applications need these classes.
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;
import java.util.*;

class OthelloGame {

    private int boardSize = 4;
    int xMarkers = 0, oMarkers = 0, totalMarkers = 0;
    private char area[][] = new char[boardSize][boardSize];
    private Hashtable<String, Character> allPlayers = new Hashtable<String, Character>();
    private HashSet<String> xPlayers = new HashSet<String>();
    private HashSet<String> oPlayers = new HashSet<String>();

    public OthelloGame(int boardSize){
        this.boardSize = boardSize;
    }

    public String getXplayers() {
      StringBuilder builder = new StringBuilder();
      for (String str : xPlayers) {
        builder.append(str).append(" ");
      }
      return builder.toString();
      //return xPlayers.toArray(new String[xPlayers.size()]);
    }

    public String getOplayers() {
      StringBuilder builder = new StringBuilder();
      for (String str : oPlayers) {
        builder.append(str).append(" ");
      }
      return builder.toString();
      //return oPlayers.toArray(new String[oPlayers.size()]);
    }

    public char[][] getBoard() {
      return area;
    }

    public void reset() {
      for (int row = 0; row < boardSize; row ++)
            for (int col = 0; col < boardSize; col++)
                area[row][col] = '_';
      totalMarkers = 0;
      xMarkers = 0;
      oMarkers = 0;
    }

    public boolean addPlayer(String name, char color){
        //check if user already exists
        if(allPlayers.get(name) == null && (color == 'o' || color == 'x')){
            allPlayers.put(name, color);
            if(color == 'o')
                oPlayers.add(name);
            if(color == 'x')
                xPlayers.add(name);
            return true;
        }
        return false;
    }

    public boolean getPlayer(String name){
        if(allPlayers.get(name) == null)
            return false;
        return true;
    }

    public void removePlayer(String name){
        if(allPlayers.get(name) == 'o')
            oPlayers.remove(name);
        if(allPlayers.get(name) == 'x')
            xPlayers.remove(name);
        allPlayers.remove(name);
    }

    private int checkState(){
      if(totalMarkers == boardSize * boardSize){ //if the board is full
          if(xMarkers > oMarkers) return 3;
          if(xMarkers < oMarkers) return 4;
          if(xMarkers == oMarkers) return 5;
      }
      return 2;
    }

    public int insertMove(String name, int x, int y){
        if(checkState() != 2){
            return checkState();
        }
        else {
          if(x > 0 && x <= boardSize && y > 0 && y <= boardSize) {
            if(area[x-1][y-1] != 'x' && area[x-1][y-1] != 'o') {
              area[x-1][y-1] = allPlayers.get(name);
              totalMarkers++;
              if(allPlayers.get(name) == 'x')
                  xMarkers++;
              else
                  oMarkers++;
              return checkState();
            }
            else
              return 0; //place already marked
          }
          else
              return 1; //out of board limits
        }
    }
}

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
    private OthelloGame game = new OthelloGame(4);

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
            post(callobj, users.get(lookupUser(callobj)).getName() + " left the chat!");
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

    private void updateGame(){
        String xpl = game.getXplayers();
        String opl = game.getOplayers();
        for(int i = 0; i < users.size(); i++) // all regisered users
          if(xpl.toLowerCase().contains(users.get(i).getName()) || opl.toLowerCase().contains(users.get(i).getName())) // if they are also part of the game, update
              users.get(i).getCallObj().printGameArea(game.getBoard(), game.getXplayers(), game.getOplayers());
    }

    public boolean othelloStart(ChatCallback callobj, char color) {
        if(lookupUser(callobj) == -1) {
            callobj.callback("\nJoin First!");
            return false;
        }
        else {
            if(game.addPlayer(users.get(lookupUser(callobj)).getName(), color) == false) {
                  callobj.callback("\nYou already joined the game!");
                  return false;
            }
            else {
              callobj.callback("\nWelcome to the GAME! \nCommands: \ninsert <x> <y> \nleaveGame");
              //callobj.callback("Xplayers: " + game.getXplayers() + "\nOplayers: " + game.getOplayers());
              updateGame();
              return true;
            }

        }
    }

    public void othelloInsert(ChatCallback callobj, int x, int y) {
        if(game.getPlayer(users.get(lookupUser(callobj)).getName()) == false)
            callobj.callback("\nJoin the game first!");
        else {
            int ok = game.insertMove(users.get(lookupUser(callobj)).getName(), x, y);
            if (ok == 0)
                callobj.callback("\nIllegal move! Place already marked!");
            if (ok == 1)
                callobj.callback("\nIllegal move! Out of board bounds! Board is 8x8.");
            if (ok > 1) {
                post(callobj, users.get(lookupUser(callobj)).getName() + " marked a place!");
                updateGame();
                if (ok == 3) {
                  callobj.callback("\nTeam X WINS the game! \nThe board has been reseted. \nContinue or leave the game.");
                  post(callobj, "\nTeam X WINS the game! \nThe board has been reseted. \nContinue or leave the game.");
                  game.reset();
                }
                if (ok == 4){
                  callobj.callback("\nTeam O WINS the game! \nThe board has been reseted. \nContinue or leave the game.");
                  post(callobj, "\nTeam O WINS the game! \nThe board has been reseted. \nContinue or leave the game.");
                  game.reset();
                }
                if (ok == 5){
                  callobj.callback("\nDRAWN game! \nThe board has been reseted. \nContinue or leave the game.");
                  post(callobj, "\nDRAWN game! \nThe board has been reseted. \nContinue or leave the game.");
                  game.reset();
                }
            }
        }
    }

    public void othelloLeave(ChatCallback callobj) {
        if(game.getPlayer(users.get(lookupUser(callobj)).getName()) == false)
            callobj.callback("\nYou never joined the game!");
        else {
            game.removePlayer(users.get(lookupUser(callobj)).getName());
            post(callobj, users.get(lookupUser(callobj)).getName() + " left the game!");
            callobj.callback("\nWe are sorry to see you go...");
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
