package main.java;
//testing git, Hello World! part 2
import java.io.IOException;

/** A simple socket interface for supporting direct messages between sockets. */
public interface BasicTalkInterface {

  /** Performs asynchronous IO using polling. */
  public void asyncIO() throws IOException;

  /** Closes the socket and frees its resources. */
  public void close() throws IOException;

  /**
   * Returns the status of the current socket connection as a String. Must include IP addresses
   * and ports.
   */
  public String status();

  /** Performs synchronous IO by blocking on input. */
  public void syncIO() throws IOException;
}
