package ChatApp;


/**
* ChatApp/ChatOperations.java .
* Error reading Messages File.
* Error reading Messages File.
* Sunday, March 5, 2017 at 9:22:38 PM Central European Standard Time
*/

public interface ChatOperations 
{
  boolean join (ChatApp.ChatCallback objref, String name);
  void leave (ChatApp.ChatCallback objref);
  void say (ChatApp.ChatCallback objref, String message);
  void list (ChatApp.ChatCallback objref);
} // interface ChatOperations
