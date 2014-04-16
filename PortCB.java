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
    /**
       Creates a new port. This constructor must have

	   super();

       as its first statement.

       @OSPProject Ports
    */
    public PortCB()
    {
        // your code goes here

    }

    /**
       This method is called once at the beginning of the
       simulation. Can be used to initialize static variables.

       @OSPProject Ports
    */
    public static void init()
    {
        // your code goes here

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

    }

    /** Destroys the specified port, and unblocks all threads suspended 
        on this port. Delete all messages. Removes the port from 
        the owners port list.
        @OSPProject Ports
    */
    public void do_destroy()
    {
        // your code goes here

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
