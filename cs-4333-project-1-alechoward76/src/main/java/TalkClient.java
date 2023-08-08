package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A client implementation of the talk interface that prints messages from STDIN to the talk server
 * and prints messages from the talk server on STDOUT.
 */
public class TalkClient implements BasicTalkInterface {

  private Socket socket;
  private BufferedReader sockin;
  private PrintWriter sockout;
  private BufferedReader stdin;

  /**
   * Constructs a socket that connects to the specified host on the specified port.
   * @param hostname the host to connect to
   * @param portnumber the port to connect to or listen on
   */
  public TalkClient(String hostname, int portnumber) throws IOException {
    this(new Socket(hostname, portnumber));
  }

  /**
   * Constructs a talk client from the specified socket.
   * @param socket a connected socket to use for the client connection
   */
  private TalkClient(Socket socket) throws IOException {
    this.socket = socket;
    this.sockin = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    this.sockout = new PrintWriter(this.socket.getOutputStream(), true);
    this.stdin = new BufferedReader(new InputStreamReader(System.in));
  }

  /**
   * Performs asynchronous IO using polling. Should print messages from STDIN to the server
   * and print messages from the server on STDOUT. Messages printed on STDOUT should be prepended
   * with "[remote] ".
   */
  public void asyncIO() throws IOException {
    // TODO: complete asyncIO
    
    if(this.stdin.ready()){
      String uInput = this.stdin.readLine();
     if(uInput.equals("STATUS")){
      System.out.println(status());
     }

    else{
      this.sockout.println(uInput);
      //this.sockout.flush(); not needed; sockout flushes automatically
    }
  }
  if(this.sockin.ready()){
    System.out.printf("[remote] %s\n", this.sockin.readLine());
    
  }
}

  /** Closes the socket and frees its resources. */
  public void close() throws IOException {
    this.stdin.close();
    this.sockout.close();
    this.sockin.close();
    this.socket.close();
  }

  /**
   * Returns the status of the current socket connection as a String. Must include IP addresses
   * and ports. Each IP address and port should be combined as {@code <IPaddress>:<port>}.
   */
  public String status() {
    // TODO: complete status
    
   // System.out.println(socket.toString());
    return socket.toString();
  }

  /**
   * Performs synchronous IO by blocking on input. Should print messages from STDIN to the server
   * and print messages from the server on STDOUT. Messages printed on STDOUT should be prepended
   * with "[remote] ".
   */
  public void syncIO() throws IOException {
    while (!this.stdin.ready()) {} // blocking with busy waiting
    this.sockout.println(this.stdin.readLine()); // readLine() also blocks
    // TODO: print messages from the server on STDOUT with blocking
    System.out.printf("[remote] %s\n", this.sockin.readLine());
  }
}


  
