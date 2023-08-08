package main.java;

import edu.utulsa.unet.RReceiveUDPI;
import edu.utulsa.unet.UDPSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import org.junit.rules.Timeout;

import com.google.common.io.Files;

public class RReceiveUDP implements Closeable, RReceiveUDPI {
  // Vars
  private String fileName;
  private int lPort = 12987;
  private int mode = 0;
  private long param = 256;

  private static final int DEFAULT_TIMEOUT = 10_000;

  /**
   * Returns a byte-buffer representing an ACK for a frame whose sequence number is seq, where
   * seq starts at 0 for the first frame.
   * @param seq the sequence number of the frame
   * @return a byte-buffer for an ACK on sequence number seq
   */
  public static byte[] formatACK(int seq) {
    
    byte[] msg = "ACK".getBytes(); 
    ByteBuffer ackBuff = ByteBuffer.allocate(msg.length );
    ackBuff.put(msg);
    //ackBuff.putInt(seq);
    return ackBuff.array();




    //throw new UnsupportedOperationException("formatACK(int) not yet implemented");
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
   * Listens for incoming packets and saves the data to the pre-selected file.
   * @return {@code true} if the file is successfully received
   */
  public boolean receiveFile() {
    // TODO: implement receiveFile()
    try{
     
     if (getMode() == 1){
     //Sliding Window
      
     
          
           //LinkedList<String> rMess = new LinkedList<>();
           LinkedList<DatagramPacket> rPack = new LinkedList<>();
          //Seq #
          //int i = 0;
          DatagramSocket socket = connect();
    
          //get file
          String fileName = getFilename();
          setFilename(fileName);
          byte[] buffR = new byte[(socket.getSendBufferSize())];
            DatagramPacket f = new DatagramPacket(buffR, buffR.length);
         
    
          FileWriter writer = new FileWriter(fileName);
        int i = 0;
          while(true){
            
            
            f = receive(socket, buffR);
            //socket.receive(f);
            //InetAddress addr = f.getAddress();
            //int port = f.getPort();
            
            //byte[] data = f.getData();
            
            //String mess = new String(f.getData(), 4, f.getLength()-4).substring(4);

            //rMess.add(mess);

            rPack.add(f);

            byte[] buffS = formatACK(i);
            DatagramPacket packetS = new DatagramPacket(buffS, buffS.length, f.getAddress(), f.getPort());
 
 
 
            socket.send(packetS);
            Thread.sleep(250);
            
            byte[] seqNum =new byte[4];
            System.arraycopy(f.getData(), 0, seqNum, 0, 4);
            
            byte[] maxSize = new byte[4];
            System.arraycopy(f.getData(), 4, maxSize, 0, 4);


            //int seqNumInt = seqNum.intValue();
         //  int maxSizeInt = maxSize.intValue();
           
            
            //breakstatement
            //byte lastByte = data[data.length-1];
            
            

            //int frame = f.getData().getInt(1);
            // int seqNumInt = ByteBuffer.wrap(seqNum).getInt();
            // int maxSizeInt = ByteBuffer.wrap(maxSize).getInt();

            //if (seqNumInt)
            
            //int val = sizeCheck.getInt(1);
              
              
                   //DatagramPacket packet = new DatagramPacket (buffS, buffS.length);
                   
              
              //i ++;
              
                // for (int j =0; j< rPack.size()-1; j++){
                
                 // int j = seqNumInt;
                  //String mess = new String(rPack.get(j).getData(), 4, rPack.get(j).getData().length - 4).substring(4);

                 // writer.write(mess);

                 

                
                //  }
                //  break;
                //break;
              
            // }//end if

            //i = frame;
            i ++;
           //if(i >= maxSizeInt){
              break;
          //  }
          }//END WHILE

          //f = receive(socket, buffR);
         
         /* byte[] buffS = formatACK(i);
          DatagramPacket packetS = new DatagramPacket(buffS, buffS.length, f.getAddress(), f.getPort());
 
 
 
            socket.send(packetS);
            Thread.sleep(250);

            String mess2 = new String(f.getData(), 4, f.getLength()-4).substring(4);
            writer.write(mess2);*/


         // writer.close();
          
          

          
          return true;
        
          
            
     }else{
      //Stop & Wait*/
      
      //

      //frames counter
      //int nFrames = 0;
      
     // ByteArrayOutputStream os = new ByteArrayOutputStream();
     


      int i =0;
      //ong time;
      

      String fileName = getFilename();
      setFilename(fileName);
      //Path path = Paths.get(file);
       //setFilename(fileName);
       //File file = new File(fileName);

       


      //FileWriter myWriter = new FileWriter(path);

      //get contents of file
    
      //used byte instead of string bc string cut off chars
      //OutputStream os = new FileOutputStream(file);

      
    
      //time = System.currentTimeMillis();

      FileWriter writer = new FileWriter(fileName);
      //connect socket to client
      DatagramSocket socket = connect();
      System.out.println("Receiving " + getFilename() + " from " + getLocalPort()  + "with" + getModeParameter() + "Using Stop & Wait");
      
     while(true){
        
        
          
        byte[] buffR = new byte[(socket.getSendBufferSize())];
          
      //may change lenth to socket.getSendBufferSize
        DatagramPacket f = new DatagramPacket(buffR, socket.getSendBufferSize());
      
          
         
          f = receive(socket, buffR);
          //System.out.println("Connecting to " + f.getAddress() + "on port: " + f.getPort());

         // f = receive(socket, buffR);
          

          //receive(socket, buffR);
          if(f == null){
            writer.close();
            break;
          }
          

         
          byte[] data = f.getData();
        
         //byte[] pData = new byte[data.length-8];
         // System.arraycopy(data, 8,pData, 0, pData.length);
          //byte[] mess = pData;
         // String sM = new String(pData);
          String mess = new String(f.getData(), 4, f.getLength()-4).substring(4);
          
          
          


          writer.write(mess);
          
          
          ByteBuffer sizeCheck = ByteBuffer.wrap(data);
            int val = sizeCheck.getInt(1);
           // int a = sizeCheck.getInt(0);
          

          if (f.getData() != null){
            //socket.send("ACK");
            byte[] buffS = formatACK(i);
            //DatagramPacket packet = new DatagramPacket (buffS, buffS.length);
            
            DatagramPacket packetS = new DatagramPacket(buffS, buffS.length, f.getAddress(), f.getPort());



            socket.send(packetS);
            Thread.sleep(250);

            
          
            i++;
            //Break Statement
            System.out.println(i + "frame(s) received");
            
            if (i == val){
              break;
            }
           
          }
          

        }
          writer.close();
          
          return true;
     
    }//end else
    

  }catch(Exception e){
  System.out.println("ASS");
  return false;
    }
    

    //throw new UnsupportedOperationException("receiveFile() not yet implemented");
  
}//end receiveFile()

  /**
   * Sets the name of the file being sent.
   * @param fname the name of the file being sent
   */
  public void setFilename(String fname) {

    fileName = fname;
    // throw new UnsupportedOperationException("setFilename(String) not yet implemented");
  }

  /**
   * Sets the name of the file being sent.
   * @param fname the name of the file being sent
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
   * Returns an established socket connection.
   * @return an established DatagramSocket connection
   */
  private DatagramSocket connect() throws IOException {
    return new UDPSocket(this.getLocalPort());
  }

  /**
   * Writes data from the socket connection into the buffer.
   * @param socket an established DatagramSocket connection
   * @param buffer the buffer to write data into
   * @return the packet that was received or {@code null} if an error occurred
   */
  private DatagramPacket receive(DatagramSocket socket, byte[] buffer) {
    try {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      socket.setSoTimeout(DEFAULT_TIMEOUT);
      socket.receive(packet);
      return packet;
    } catch (IOException e) {
      // a TIMEOUT is an IOException
      System.out.println(e);
      return null;
    }
  }
}
