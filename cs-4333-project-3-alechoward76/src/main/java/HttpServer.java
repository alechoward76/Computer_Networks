package main.java;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.hamcrest.Matcher;

/** An implementation of an HTTP server. */
public class HttpServer implements Closeable {

private int port = 80;

  

  /**
   * Returns the content type associated with the given filename.
   * @param uri the path identifier for the requested resource
   * @return the content type associated with the given URI,
   *   or {@code null} if the file type is not supported.
   */
  public static String getContentType(String uri) {
    

      String[] allowed = {"htm", "html", "gif", "jpg", "jpeg", "pdf"};
      String pattern = "\\.(?i)(" + String.join("|", allowed) + ")$";

      Pattern p = Pattern.compile(pattern);
      java.util.regex.Matcher m = p.matcher(uri);

      if (m.find()){
        //return "text/html";
        //String ret = "";
        if(m.group(1).equals("htm") || m.group(1).equals("html") ){
          return("text/html");
        }else if(m.group(1).equals("gif") ){
          return "image/gif";
        }else if(m.group(1).equals("pdf") ){
          return "application/pdf";
        }else if(m.group(1).equals("jpg") || m.group(1).equals("jpeg") ){
          return("image/jpeg");
        }
        
        else{
          return null;
        }
        //return ret;
        }
        
      else{
        return null;
      }  
    //throw new UnsupportedOperationException("getContentType(String) not yet implemented");
  }

  /**
   * Returns the HTTP server response entity headers.
   * @param serverName the name of the HTTP server
   * @param version the version of the HTTP server
   * @return the HTTP server response entity headers
   */
  public static String[] getEntityHeaders(String serverName, String version) {
    return HttpServer.getEntityHeaders(serverName, version, 0, null);
  }

  /**
   * Returns the HTTP server response entity headers.
   * @param serverName the name of the HTTP server
   * @param version the version of the HTTP server
   * @param length the content length of the requested resource,
   *   or {@code 0} if the resource does not exist.
   * @param contentType the content type of the requested file,
   *   or {@code null} if the content type is unsupported.
   * @return the HTTP server response entity headers
   */
  public static String[] getEntityHeaders(
      String serverName, String version, long length, String contentType) {
    
       /*Server: ServerName/ServerVersion
Content-Length: lengthOfResource
Content-Type: typeOfResource */
        
        if (Objects.isNull(length)){
          length = 0;
        }
        String len = Objects.toString(length);
        ArrayList<String> head = new ArrayList<String>();
        //String[] head = "Server_Name: " + serverName;
       // Map<String,String>head = new HashMap<>();
        head.add("Server: " + serverName + "/"+ version);
       // head.add(version);
        head.add("Content-Length: "+ len);
        head.add("Content-Type: " + contentType);
        //head.add("\r\n");
        String[] str = new String[head.size()];
        for(int i = 0; i< head.size(); i++){
          str[i] = head.get(i);
        }//end for

        

        return str;


        }

    /*throw new UnsupportedOperationException(
        "getEntityHeaders(String, String, long, String) not yet implemented");*/
  //}

  /**
   * Returns the reason phrase associated with the input status code.
   * @param statusCode the status code of an HTTP request
   * @return the reason phrase associated with the input status code
   */
  public static String getReasonPhrase(int statusCode) {
    

    String reason = "";
    if (statusCode == 200){
      reason =  "OK";
    }
    else if (statusCode == 400){
      reason =  "Bad Request";
    }
    else if (statusCode == 404){
      reason =  "Not Found";
    }
    else if (statusCode == 501){
      reason = "Not Implemented";
    }
    return reason;

    //throw new UnsupportedOperationException("getReasonPhrase(int) not yet implemented");
  }

  /**
   * Returns the HTTP server response header.
   * @param serverName the name of the HTTP server
   * @param version the version of the HTTP server
   * @param statusCode the status code of the request
   * @return the HTTP server response header
   */
  public static String getResponseHeader(String serverName, String version, int statusCode) {
    return HttpServer.getResponseHeader(serverName, version, statusCode, null);
  }

  /**
   * Returns the HTTP server response header.
   * @param serverName the name of the HTTP server
   * @param version the version of the HTTP server
   * @param statusCode the status code of the request
   * @param uri the path identifier for the requested resource,
   *   or {@code null} if the URI was not provided
   * @return the HTTP server response header
   */
  public static String getResponseHeader(String serverName, String version, int statusCode, String uri) {
       
  
    
    File file = new File("public_html/"+uri);
    long fLen = file.length();

    String test =  "HTTP/1.1" + " " + Integer.toString(statusCode) + " " + getReasonPhrase(statusCode) + "\r\n";
    

    //if(statusCode == 200){
      for(String s:getEntityHeaders(serverName, version, fLen, getContentType(uri))){
        test += (s + "\r\n");
      //}
    }/*else{
      for(String s:getEntityHeaders(serverName, version)){
        test += (s + "\r\n");
      }
    }*/
    
    test = test.trim();
    test += "\r\n\r\n";
    
    
  
    return test;
    
      
       
        
}

    // TODO: implement getResponseHeader(String, String, int, String)
    /*throw new UnsupportedOperationException(
        "getResponseHeader(String, String, int, String) not yet implemented");
  }*/

  /**
   * Returns the status code for the input HTTP request. The request must include a HOST field
   * in its header. Valid hosts are 'localhost', '127.0.0.1', and the machine's domain name,
   * and it must be appended with the HTTP server's designated port unless the port is 80.
   * @param domain the domain name of the HTTP server
   * @param port the port of the HTTP server
   * @param request the HTTP request
   * @return the status code for the input HTTP request
   */
  public static int getStatusCode(String domain, int port, String request) {
    

    //String[] allowed = {"HEAD", "GET"};
      String pattern = "^(GET|HEAD|OPTIONS|POST|DELETE|PUT|TRACE|CONNECT) (\\S+) HTTP/(\\d\\.?\\d?)\r\nHost: ([\\w\\.]+):?(\\d+)?\r\n\r\n" ;

      Pattern p = Pattern.compile(pattern);
      java.util.regex.Matcher m = p.matcher(request);

      

    //501
    if (m.find() ){
      
      File tmpDir = new File("public_html/"+ m.group(2));
      
      if ((!(m.group(1).equals("HEAD") ) && !(m.group(1).equals("GET"))) || getContentType(m.group(2)) == null || !m.group(3).equals("1.1")){
        
        return 501; //Unsupported/ nonimplemented
      } else if((!m.group(4).equals(domain) && !m.group(4).equals("localhost") && !m.group(4).equals("127.0.0.1")) || (m.group(5)==null && port !=80) || ( m.group(5) != null && !m.group(5).equals(Integer.toString(port))  ) ){
        return 400;
      }else if(!tmpDir.exists()){
        return 404;
      }
      
      else if (getContentType(m.group(2)) != null){
        return 200;
      }
      
        
        
        else{
          return 400; 
        }
    }




      else{
        return 400;
     }




    
  
    /*throw new UnsupportedOperationException(
        "getStatusCode(String, int, String) not yet implemented");*/
  }

  /**
   * Returns the HTTP server response status line.
   * @param statusCode the status code of the request
   * @return the HTTP server response status line
   */
  public static String getStatusLine(int statusCode) {
    
    //HTTP/1.1 SP StatusCode SP ReasonPhrase CRLF
    String sLine = "HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode) + "\r\n";
    return sLine;


    //throw new UnsupportedOperationException("getStatusLine(int) not yet implemented");
  }

  /**
   * Constructs an HTTP server that listens for HTTP requests
   * on the input port.
   * @param port the port to listen for HTTP requests
   */
  public HttpServer(int port) {
    // TODO: implement HttpServer(int)

   


    throw new UnsupportedOperationException("HttpServer(int) not yet implemented");
  }

  /** Frees all system resources acquired by this instance. */
  public void close() throws IOException {}

  /**
   * Returns the port acquired by this HTTP server.
   * @return the port acquired by this HTTP server
   */
  public int getPort() {
    // TODO: implement getPort()
    Integer port = this.port;
    return port;


    //throw new UnsupportedOperationException("getPort() not yet implemented");
  }

  /**
   * Returns the HTTP response for the input HTTP request.
   * @param request the HTTP request being responded to
   * @return the HTTP response for the input HTTP request
   */
  public String getResponse(String request) {
    // TODO: implement getResponse(String)
   
   // getStatusCode(domain, port, request)
    //return getResponseHeader(serverName, version, getStatusCode(domain, getPort(), request));
    

    throw new UnsupportedOperationException("getResponse(String) not yet implemented");
  }

  /*public String getFile(String fName){

    File f = new File("public_html" + fName);
    try(BufferedReader br = new BufferedReader(new FileReader(f))) {
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
  
      while (line != null) {
          sb.append(line);
          sb.append(System.lineSeparator());
          line = br.readLine();
      }
      String everything = sb.toString();
      return everything;
    }catch (IOException e){
     return null;
    }
    
  }*/

  public static void main(String[] args) throws IOException{

    
  }//end main
  
}
