/*  CSCE311 Project4 Ports
 *  University of South Carolina
 *  author: Yixing Cheng
 *  date: 4/17/2014
 *  PortCB.java
 *
*/

package osp.Ports;

import java.util.*;
import osp.IFLModules.*;
import osp.Threads.*;
import osp.Tasks.*;
import osp.Memory.*;
import osp.Utilities.*;

/**
   The studends module for dealing with ports. The methods 
   that have to be implemented are do_create(), 
   do_destroy(), do_send(Message msg), do_receive(). 


   @OSPProject Ports
*/

public class PortCB extends IflPortCB
{
    private int bufferIn;
    private int bufferOut;
    /**
       Creates a new port. This constructor must have

	   super();

       as its first statement.

       @OSPProject Ports
    */
    public PortCB()
    {
        // your code goes here
       super();
    }

    /**
       This method is called once at the beginning of the
       simulation. Can be used to initialize static variables.

       @OSPProject Ports
    */
    public static void init()
    {
        // your code goes here
        System.out.println("I am doing OSP2 Ports Project");
    }

    /** 
        Sets the properties of a new port, passed as an argument. 
        Creates new message buffer, sets up the owner and adds the port to 
        the task's port list. The owner is not allowed to have more 
        than the maximum number of ports, MaxPortsPerTask.

        @OSPProject Ports
    */
    public static PortCB do_create()
    {
        // your code goes here
        PortCB newPort = new PortCB();

        TaskCB currentTask = null;                                                                 //get the requesting thread

        try {
             currentTask = MMU.getPTBR().getTask();                                            
         } catch (NullPointerException e){        
         }
        
        int currentPortNum = currentTask.getPortCount();
        if(currentPortNum == MaxPortsPerTask)
               return null;

        if( currentTask.addPort(newPort) == FAILURE){
               return null;
           }
        
        newPort.setTask(currentTask);
        newPort.setStatus(PortLive);
        
        newPort.bufferIn = 0;
        newPort.bufferOut = 0;

        return newPort; 
    }

    /** Destroys the specified port, and unblocks all threads suspended 
        on this port. Delete all messages. Removes the port from 
        the owners port list.
        @OSPProject Ports
    */
    public void do_destroy()
    {
        // your code goes here
        this.setStatus(PortDestroyed);
        
        this.notifyThreads();
        
        this.getTask().removePort(this);

        this.setTask(null);
    }

    /**
       Sends the message to the specified port. If the message doesn't fit,
       keep suspending the current thread until the message fits, or the
       port is killed. If the message fits, add it to the buffer. If 
       receiving threads are blocked on this port, resume them all.

       @param msg the message to send.

       @OSPProject Ports
    */
    public int do_send(Message msg)
    {
        // your code goes here
       if( msg == null || (msg.getLength() > PortBufferLength)){
               return FAILURE;
          }
       
       SystemEvent newEvent = new SystemEvent("send_msg_suspension");
              
       TaskCB currentTask = null;                                                                 //get the requesting thread
       ThreadCB currentThread = null;

       try {
          currentTask = MMU.getPTBR().getTask();                                            
          currentThread = currentTask.getCurrentThread();
         }catch (NullPointerException e){        
         }       
       
       currentThread.suspend(newEvent);
       
       int bufferRoom;
       boolean suspendMsg = true;
       while(suspendMsg)   {
            
            if( this.bufferIn < this.bufferOut){
                   bufferRoom = this.bufferOut - this.bufferIn;
              }
            else if( this.bufferIn == this.bufferOut){
                   if(this.isEmpty()){
                         bufferRoom = PortBufferLength;
                      }
                   else{
                         bufferRoom = 0;
                      }
              }
            else{
                   bufferRoom = PortBufferLength + this.bufferOut - this.bufferIn;
              }
            
            if( msg.getLength() <= bufferRoom){
                   suspendMsg = false;
              }
            else{
                   currentThread.suspend(this);
              }
            
            if( currentThread.getStatus() == ThreadKill){
                   this.removeThread(currentThread);
                   return FAILURE;
              }

            if( this.getStatus() != PortLive){
                  newEvent.notifyThreads();
                  return FAILURE;
              }
 
          }
       
       this.appendMessage(msg);
       this.notifyThreads();

       this.bufferIn = (this.bufferIn + msg.getLength()) % PortBufferLength;
       newEvent.notifyThreads();
       return SUCCESS;
    }

    /** Receive a message from the port. Only the owner is allowed to do this.
        If there is no message in the buffer, keep suspending the current 
	thread until there is a message, or the port is killed. If there
	is a message in the buffer, remove it from the buffer. If 
	sending threads are blocked on this port, resume them all.
	Returning null means FAILURE.

        @OSPProject Ports
    */
    public Message do_receive() 
    {
        // your code goes here
       TaskCB currentTask = null;                                                                 //get the requesting thread
       ThreadCB currentThread = null;

       try {
          currentTask = MMU.getPTBR().getTask();                                            
          currentThread = currentTask.getCurrentThread();
         }catch (NullPointerException e){        
         }

       if(this.getTask() != currentTask){
              return null;
         }       
 
       SystemEvent newEvent = new SystemEvent("receive_msg_suspension");
       currentThread.suspend(newEvent);
       
       boolean suspendMsg = true;
       while(suspendMsg){
           if(this.isEmpty()){
                 currentThread.suspend(this);
             }
           else{
                 suspendMsg = false;
             }

            if( currentThread.getStatus() == ThreadKill){
                   this.removeThread(currentThread);
                   newEvent.notifyThreads();
                   return null;
              }

            if( this.getStatus() != PortLive){
                  newEvent.notifyThreads();
                  return null;
              }   
          }
       
       Message currentMsg = this.removeMessage();
       this.bufferOut = (this.bufferOut + currentMsg.getLength()) % PortBufferLength; 
       this.notifyThreads();
       newEvent.notifyThreads();

       return currentMsg;       
    }

    /** Called by OSP after printing an error message. The student can
	insert code here to print various tables and data structures
	in their state just after the error happened.  The body can be
	left empty, if this feature is not used.
	
	@OSPProject Ports
    */
    public static void atError()
    {
        // your code goes here
      
    }

    /** Called by OSP after printing a warning message. The student
	can insert code here to print various tables and data
	structures in their state just after the warning happened.
	The body can be left empty, if this feature is not used.
     
	@OSPProject Ports
    */
    public static void atWarning()
    {
        // your code goes here

    }


    /*
       Feel free to add methods/fields to improve the readability of your code
    */

}

/*
      Feel free to add local classes to improve the readability of your code
*/
