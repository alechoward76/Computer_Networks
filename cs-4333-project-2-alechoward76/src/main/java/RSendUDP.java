package main.java;

import edu.utulsa.unet.RSendUDPI;
import edu.utulsa.unet.UDPSocket;
import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//
import java.net.*;
import java.io.*;

public class RSendUDP implements Closeable, RSendUDPI {

  // Vars
  private String fileName;
  private int lPort = 12987;
  private int mode = 0;
  private long param = 256;
  private InetSocketAddress rec = new InetSocketAddress("localhost", lPort);
  private long time = 1000;

  /**
   * Returns a byte-buffer representing the ith frame in a sequence of frames to send the input
   * msg. Each frame is assumed to be of size framesize.
   * @param msg the total message being sent
   * @param i the sequence number of this frame
   * @param frameSize the size of each frame
   * @return a byte-buffer for the ith frame in the sequence
   */
  public static byte[] formatPacket(String msg, int i, int frameSize) {
    
    
    byte[] wholeMess = msg.getBytes();
    
    //Ints are 4 each, so message space left after accounting for those
    int frameLeft = frameSize - 8;
    
    //length of whole message / available message space per frame gives # of frames
    int maxSize = wholeMess.length / frameLeft;

    ByteBuffer buff = ByteBuffer.allocate(frameSize);
    buff.putInt(i);
    buff.putInt(maxSize);
    buff.put(Arrays.copyOfRange(wholeMess, i*frameLeft, (i+1)*frameLeft));
    
    
    return buff.array();
    //throw new UnsupportedOperationException("format Packet(String, int, int) not yet implemented");
  }

  /** Releases any acquired system resources. */
  public void close() throws IOException {}

  /**
   * Returns the name of the file being sent.
   * @return the name of the file being sent
   */
  public String getFilename() {
    String fName = this.fileName;
    return fName;

    // throw new UnsupportedOperationException("getFilename() not yet implemented");
  }

  /**
   * Returns the port number of the receiver.
   * @return the port number of the receiver
   */
  public int getLocalPort() {
    Integer port = this.lPort;
    return port;
    // throw new UnsupportedOperationException("getLocalPort() not yet implemented");
  }

  /**
   * Returns the selected ARQ algorithm where {@code 0} is stop-and-wait and {@code 1} is
   * sliding-window.
   * @return the selected ARQ algorithm
   */
  public int getMode() {
    Integer aMode = this.mode;
    return aMode;

    // throw new UnsupportedOperationException("getMode() not yet implemented");
  }

  /**
   * Returns the size of the window in bytes when using the sliding-window algorithm.
   * @return the size of the window in bytes for the sliding-window algorithm
   */
  public long getModeParameter() {
    long mParam = this.param;
    return mParam;
    // throw new UnsupportedOperationException("getModeParameter() not yet implemented");
  }

  /**
   * Returns the address (hostname) of the receiver.
   * @return the address (hostname) of the receiver
   */
  public InetSocketAddress getReceiver() {
    InetSocketAddress receive = this.rec;
    return receive;

    // throw new UnsupportedOperationException("getReceiver() not yet implemented");
  }

  /**
   * Returns the ARQ timeout in milliseconds.
   * @return the ARQ timeout
   */
  public long getTimeout() {
    long timt = this.time;
    return timt;
    // throw new UnsupportedOperationException("getTimeout() not yet implemented");
  }

  /**
   * Sends the pre-selected file to the receiver.
   * @return {@code true} if the file is successfully sent
   */
  public boolean sendFile() {
    // TODO: implement sendFile()
   try{
    
    if (getMode() == 1){
      //Sliding Window

      //List of packets that have been sent, but not ACKed
      Queue<DatagramPacket> sentPackets = new LinkedList<>();
      //Seq #
      int i = 0;
      DatagramSocket socket = connect();

      //get file
      String file = getFilename();
      Path path = Paths.get(file);

      //get contents of file
    
      //used byte instead of string bc string cut off chars
      byte [] wholeMess = Files.readAllBytes(path);
      
      
     send(socket, formatPacket(new String(wholeMess), 0, socket.getSendBufferSize()));
     //Datagram to add to list
     DatagramPacket packS = new DatagramPacket(wholeMess, wholeMess.length);
     sentPackets.add(packS);
     i++;

      //Send packs while list is not empty
      while (!sentPackets.isEmpty()){
        send(socket, formatPacket(new String(wholeMess), i, socket.getSendBufferSize()));
        //i++;
        // if list is greater thatn window size, wait for an ack before sending more
        if(sentPackets.size() >= getModeParameter()){
          byte[] buffR = new byte[3];
          DatagramPacket packetR = new DatagramPacket(buffR, buffR.length);
          // socket.setSoTimeout(DEFAULT_TIMEOUT);
          socket.receive(packetR);

            //Parse Packet
          int pLen = packetR.getLength();
          byte[] pData = packetR.getData();
          String ack = new String(pData, 0, pLen);
        
            //hmmm
            //ack.equals("ACK"
          if (ack.equals("ACK")){
            sentPackets.remove();
            System.out.println("ACK RECEIVED");
            
            //return true;
          }//end if
          else{
            System.out.println("Ack Failed");
            //sendFile();
            }//end else
        }//end IF
        //packS.setSeq
        i++;
      }

      System.out.println("Sliding Window Send");
      return true;
      //return false;
    }else{
      //Stop & Wait
      //String ack;
      long time;
      //System.out.println("Sending " + getFilename() + " from " + getLocalPort() + " to " + getReceiver() + "with" + getModeParameter() + "Using Stop & Wait");

      //counter used to run loop while it is < messN
      int i = 0;
      
      //Connect new socket
      DatagramSocket socket = connect();

      //get file
      String file = getFilename();
      Path path = Paths.get(file);

      //get contents of file
    
      //used byte instead of string bc string cut off chars
      byte [] wholeMess = Files.readAllBytes(path);
      //byte [] wholeMess2 = wholeMess;
      
      //Ints are 4 each, so message space left after accounting for those
      int frameLeft = (socket.getSendBufferSize() - 8);
      
      //length of whole message / available message space per frame gives # of frames
      int maxSize = wholeMess.length / frameLeft;
     //buffer
      
      while (i <= (maxSize )){

        send(socket, formatPacket(new String(wholeMess), i, socket.getSendBufferSize()));
      
        //Set time
        time = System.currentTimeMillis();

        //while socket is busy if exceeds timeout, break
        while(!socket.isClosed()){
          if((System.currentTimeMillis() - time) < getTimeout()){
            
            break;
          
          }//end if
          }//end while

          //Figure out Timeouts
        //if socket not busy, and there is an ACK, increment
        //if (socket.isClosed()){
            
          byte[] buffR = new byte[3];
          DatagramPacket packetR = new DatagramPacket(buffR, buffR.length);
          // socket.setSoTimeout(DEFAULT_TIMEOUT);
          socket.receive(packetR);

            //Parse Packet
          int pLen = packetR.getLength();
          byte[] pData = packetR.getData();
          String ack = new String(pData, 0, pLen);
        
            //hmmm
            //ack.equals("ACK"
          if (ack.equals("ACK")){
            System.out.println("ACK RECEIVED");
            i++;
            //return true;
          }//end if
          else{
            System.out.println("Ack Failed");
            //sendFile();
            }//end else
          //}//end if
        }//end WHILE
        return true;
     
      }
   }catch(Exception e){
    System.out.println("ASS");
    return false;
    
   }//end catch
    //throw new UnsupportedOperationException("sendFile() not yet implemented");
  }//end sendFile()

  /**
   * Sets the name of the file being sent.
   * @param fname the name of the file being sent
   */
  public void setFilename(String fname) {

    fileName = fname;
    // throw new UnsupportedOperationException("setFilename(String) not yet implemented");
  }

  /**
   * Sets the port number of the receiver.
   * @param port the port number of the receiver
   * @return {@code true} if the intended port of the receiver is set to the input port
   */
  public boolean setLocalPort(int port) {
    if (port < 0 || port > 65535) {
      return false;
    }
    lPort = port;

    return lPort == port;

    // throw new UnsupportedOperationException("setLocalPort(int) not yet implemented");
  }

  /**
   * Sets the selected ARQ algorithm where {@code 0} is stop-and-wait and {@code 1} is
   * sliding-window.
   * @param mode the selected ARQ algorithm
   * @return {@code true} if the ARQ algorithm is set to the input mode
   */
  public boolean setMode(int mode) {
    if (mode < 0 || mode > 1) {
      return false;
    }
    this.mode = mode;
    return this.mode == mode;
    // throw new UnsupportedOperationException("setMode(int) not yet implemented");
  }

  /**
   * Sets the size of the window in bytes when using the sliding-window algorithm.
   * @param n the size of the window in bytes for the sliding-window algorithm
   * @return {@code true} if the window size is set to the input n
   */
  public boolean setModeParameter(long n) {
    if (n <= 0) {
      return false;
    }
    param = n;
    return param == n;

    // throw new UnsupportedOperationException("setModeParameter(long) not yet implemented");
  }

  /**
   * Sets the address (hostname) of the receiver.
   * @param receiver the address (hostname) of the receiver
   * @return {@code true} if the intended address of the receiver is set to the input receiver
   */
  public boolean setReceiver(InetSocketAddress receiver) {
    rec = receiver;
    return rec == receiver;

    // throw new UnsupportedOperationException("setReceiver(InetSocketAddress) not yet
    // implemented");
  }

  /**
   * Sets the ARQ timeout in milliseconds.
   * @param timeout the ARQ timeout
   * @return {@code true} if the ARQ timeout is set to the input timeout
   */
  public boolean setTimeout(long timeout) {
    time = timeout;
    if (time <= 0){
      return false;
    }
    return time == timeout;

    // throw new UnsupportedOperationException("setTimeout(long) not yet implemented");
  }

  /**
   * Returns an established socket connection.
   * @return an established DatagramSocket connection
   */
  private DatagramSocket connect() throws IOException {
    return new UDPSocket(this.getLocalPort());
  }

  /**
   * Sends buffer over socket connection.
   * @param socket an established DatagramSocket connection
   * @param buffer the buffer to send over the socket
   * @return {@code true} if the buffer is successfully sent over the socket
   */
  private boolean send(DatagramSocket socket, byte[] buffer) {
    try {
      DatagramPacket packet =
          new DatagramPacket(
              buffer, buffer.length, this.getReceiver().getAddress(), this.getReceiver().getPort());
      socket.send(packet);
      Thread.sleep(250);
      return true;
    } catch (InterruptedException | IOException e) {
      System.out.println(e);
      return false;
    }
  }
}

