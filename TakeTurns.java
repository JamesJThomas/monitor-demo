import java.util.concurrent.locks.*;
/**
This is designed such that three threads will take turns calling the take turn method, which will print out a simple messagge. This class demonstrates how a monitor can be implemented in java.
@author James Thomas
*/
class TurnMonitor
{
private int turn; //indicates whose turn it is to call the method.
private final Lock lock = new ReentrantLock(); //The monitor lock
private Condition myTurn = lock.newCondition(); //The condition variable on which threads shall wait when it is not their turn.
/**
Constructor
@param init the ID of the thread who will call the method first
*/
public TurnMonitor(int init)
{
turn = init;
}
public void takeTurn(int id) throws InterruptedException
{
lock.lock(); //Lock the lock to preserve the mutual exclusion property of the monitor
try //The code for a monitor procedure shall always be placed in a try block. This way, the lock can be unlocked in a finally block regardless of whether an exception is thrown
{
while(turn != id)
{
myTurn.await();
} //If it is not this thread's turn, then wait on the condition variable. Notice that I use a while loop instead of an if statement; this is done to guard against spurious wakeups. Essentially, a spurious wakeup occurs when a thread wakes up without the condition variable on which it was waiting being signalled. If this occurs and it is still not this thread's turn, it will simply loop back and wait again.
System.out.printf("I am thread number %d%n", id); //Make the thread print out its id just to show that something happened.
turn = (turn+1)%3; //it is now the next thread's turn, so update the turn variable to reflect this. The modul operator will cause turn to wrap back arround to the first thread after the last one is finished.
myTurn.signalAll(); //The signal method doesn't guarantee anything about which thread will be woken up next, so just signal them all in case more than one is waiting.
}
finally
{
lock.unlock();
}
}
}
/**
This class will implement the code that a given thread will run when it is started.
@author James thomas
*/
class TurnRunner implements Runnable 
{
private TurnMonitor tm; //The turn monitor object that the threads will use for synchronization.
private int id; //The id of the thread; this is what is passed to the take turn method.
/**
constructor
@param tm the turn monitor object to be used for synchronization
@param id the id of this thread.
*/
public TurnRunner(TurnMonitor tm, int id)
{
this.tm = tm;
this.id = id;
}
//I am implementing the runnable interface, so this run method is required; this is the method each thread will call when it starts.
public void run()
{
int i;
try
{
for(i = 0; i < 10; i++)
{
tm.takeTurn(id);
} //Each thread will take 10 turns. This is an arbitrary number that was chosen so the program would terminate at some point.
}
catch(InterruptedException ix) //To make the compiler happy.
{
ix.printStackTrace();
}
}
}
//Finally, the main class.
public class TakeTurns 
{
public static void main(String[] args)
{
TurnMonitor tm; //for synchronization
int i;
tm = new TurnMonitor(0); //I arbitrarily chose thread 0 to go first.
for(i = 0; i < 3; i++)
{
new Thread(new TurnRunner(tm, i)).start();
} //Start threads with id 0, 1, and 2. Since we don't really have globle variables in java, I will just pass the same turn monitor object into the constructor for each thread.
}
}
