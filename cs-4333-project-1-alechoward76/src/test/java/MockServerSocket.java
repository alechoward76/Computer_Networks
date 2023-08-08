package test.java;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MockServerSocket extends ServerSocket {

  public InetAddress address;
  public MockSocket client;
  public boolean closed;
  public int port;

  public MockServerSocket() throws IOException {
    this(new MockSocket());
  }

  public MockServerSocket(MockSocket client) throws IOException {
    this(InetAddress.getLocalHost(), 12987, client);
  }

  public MockServerSocket(InetAddress address, int port, MockSocket client) throws IOException {
    this.address = address;
    this.port = port;
    this.client = client;
    this.closed = false;
  }

  @Override
  public Socket accept() {
    return this.client;
  }

  @Override
  public void close() {
    this.closed = true;
  }

  @Override
  public InetAddress getInetAddress() {
    return this.address;
  }

  @Override
  public int getLocalPort() {
    return this.port;
  }

  @Override
  public boolean isClosed() {
    return this.closed;
  }

  @Override
  public String toString() {
    return String.format(
        "ServerSocket[addr=%s/%s,localport=%d]",
        this.getInetAddress().toString(), this.getInetAddress().toString(), this.getLocalPort());
  }
}