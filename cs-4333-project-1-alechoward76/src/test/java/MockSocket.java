package test.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MockSocket extends Socket {

  private static final InputStream DEFAULT_INPUT_STREAM =
      new InputStream() {
        @Override
        public int available() {
          return 0;
        }

        public int read() {
          return (int) '\0';
        }
      };
  private static final OutputStream DEFAULT_OUTPUT_STREAM =
      new OutputStream() {
        public void write(int b) {}
      };

  public boolean closed;
  public InetAddress hostname;
  public InputStream in;
  public InetAddress localAddress;
  public int localPort;
  public OutputStream out;
  public int port;

  public MockSocket() throws IOException {
    this(MockSocket.DEFAULT_INPUT_STREAM, MockSocket.DEFAULT_OUTPUT_STREAM);
  }

  public MockSocket(InputStream in) throws IOException {
    this(in, MockSocket.DEFAULT_OUTPUT_STREAM);
  }

  public MockSocket(OutputStream out) throws IOException {
    this(MockSocket.DEFAULT_INPUT_STREAM, out);
  }

  public MockSocket(InputStream in, OutputStream out) throws IOException {
    this(InetAddress.getLocalHost(), 8080, InetAddress.getLocalHost(), 12987, in, out);
  }

  public MockSocket(
      InetAddress localAddress,
      int localPort,
      InetAddress hostname,
      int port,
      InputStream in,
      OutputStream out) {
    this.localAddress = localAddress;
    this.localPort = localPort;
    this.hostname = hostname;
    this.port = port;
    this.in = in;
    this.out = out;
    this.closed = false;
  }

  @Override
  public void close() {
    this.closed = true;
  }

  @Override
  public InetAddress getInetAddress() {
    return this.hostname;
  }

  @Override
  public InputStream getInputStream() {
    return this.in;
  }

  @Override
  public InetAddress getLocalAddress() {
    return this.localAddress;
  }

  @Override
  public int getLocalPort() {
    return this.localPort;
  }

  @Override
  public OutputStream getOutputStream() {
    return this.out;
  }

  @Override
  public int getPort() {
    return this.port;
  }

  @Override
  public boolean isClosed() {
    return this.closed;
  }

  @Override
  public boolean isConnected() {
    return !this.closed;
  }

  @Override
  public String toString() {
    return String.format(
        "Socket[addr=%s/%s,port=%d,localport=%d]",
        this.getInetAddress().toString(),
        this.getLocalAddress().toString(),
        this.getPort(),
        this.getLocalPort());
  }
}