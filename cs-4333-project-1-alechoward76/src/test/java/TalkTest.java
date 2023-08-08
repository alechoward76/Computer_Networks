package test.java;

/**
 * DO NOT DISTRIBUTE.
 *
 * This code is intended to support the education of students associated with the Tandy School of
 * Computer Science at the University of Tulsa. It is not intended for distribution and should
 * remain within private repositories that belong to Tandy faculty, students, and/or alumni.
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import main.java.BasicTalkInterface;
import main.java.Talk;
import main.java.TalkClient;
import main.java.TalkServer;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * This class provides a set of unit tests for the {@code Talk} classes
 * using the JUnit testing framework and the Java Reflection API.
 */
public class TalkTest {

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 12987;
  private static final InputStream STDIN = System.in;
  private static final PrintStream STDOUT = System.out;

  private final ByteArrayOutputStream stdoutCapture = new ByteArrayOutputStream();

  @Test
  public void testStartCallsAutoModeWithDefaultArgs() {
    final Eval eval = new Eval("\"Talk -a\" should call autoMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertEquals(TalkTest.DEFAULT_HOST, hostname);
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful", mock.start(new String[] {"-a"}));
    eval.done();
  }

  @Test
  public void testAutoModeCallsClientModeButNotServerModeWithDefaultArgs() {
    final Eval eval =
        new Eval("autoMode should only call clientMode when clientMode is successful");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals(TalkTest.DEFAULT_HOST, hostname);
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail();
            return false;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful", mock.start(new String[] {"-a"}));
    eval.done();
  }

  @Test
  public void testAutoModeCallsServerModeAfterClientModeFailsWithDefaultArgs() {
    final Eval eval = new Eval("autoMode should call serverMode when clientMode fails");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }

          @Override
          public boolean serverMode(int portnumber) {
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful", mock.start(new String[] {"-a"}));
    eval.done();
  }

  @Test
  public void testStartCallsAutoModeWithHostArg() {
    final Eval eval = new Eval("\"Talk -a test.edu\" should call autoMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertEquals("test.edu", hostname);
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.edu"}));
    eval.done();
  }

  @Test
  public void testAutoModeCallsClientModeButNotServerModeWithHostArg() {
    final Eval eval =
        new Eval("autoMode should only call clientMode when clientMode is successful");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals("test.edu", hostname);
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail();
            return false;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.edu"}));
    eval.done();
  }

  @Test
  public void testStartCallsAutoModeWithPortArg() {
    final Eval eval = new Eval("\"Talk -a -p 8080\" should call autoMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertEquals(TalkTest.DEFAULT_HOST, hostname);
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testAutoModeCallsClientModeButNotServerModeWithPortArg() {
    final Eval eval =
        new Eval("autoMode should only call clientMode when clientMode is successful");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals(TalkTest.DEFAULT_HOST, hostname);
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail();
            return false;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testAutoModeCallsServerModeAfterClientModeFailsWithPortArg() {
    final Eval eval = new Eval("autoMode should call serverMode when clientMode fails");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }

          @Override
          public boolean serverMode(int portnumber) {
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testStartCallsAutoModeWithHostAndPortArgs() {
    final Eval eval = new Eval("\"Talk -a test.edu -p 8080\" should call autoMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            assertEquals("test.edu", hostname);
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.edu", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testAutoModeCallsClientModeButNotServerModeWithHostAndPortArgs() {
    final Eval eval =
        new Eval("autoMode should only call clientMode when clientMode is successful");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals("test.edu", hostname);
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }

          @Override
          public boolean serverMode(int portnumber) {
            eval.fail();
            return false;
          }
        };
    assertTrue(
        "start should return true when autoMode is successful",
        mock.start(new String[] {"-a", "test.edu", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testAutoModeReturnsFalseWhenClientModeAndServerModeBothFail() {
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }

          @Override
          public boolean serverMode(int portnumber) {
            return false;
          }
        };
    assertFalse(
        "autoMode should return false when clientMode and serverMode fail",
        mock.autoMode("test.edu", 8080));
  }

  @Test
  public void testStartReturnsFalseWhenAutoModeFails() {
    Talk mock =
        new Talk() {
          @Override
          public boolean autoMode(String hostname, int portnumber) {
            return false;
          }
        };
    assertFalse("start should return false when autoMode fails", mock.start(new String[] {"-a"}));
  }

  @Test
  public void testStartCallsClientModeWithDefaultArgs() {
    final Eval eval = new Eval("\"Talk -h\" should call clientMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals(TalkTest.DEFAULT_HOST, hostname);
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when clientMode is successful", mock.start(new String[] {"-h"}));
    eval.done();
  }

  @Test
  public void testStartCallsClientModeWithHostArg() {
    final Eval eval = new Eval("\"Talk -h test.edu\" should call clientMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals("test.edu", hostname);
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when clientMode is successful",
        mock.start(new String[] {"-h", "test.edu"}));
    eval.done();
  }

  @Test
  public void testStartCallsClientModeWithPortArg() {
    final Eval eval = new Eval("\"Talk -h -p 8080\" should call clientMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals(TalkTest.DEFAULT_HOST, hostname);
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when clientMode is successful",
        mock.start(new String[] {"-h", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testStartCallsClientModeWithHostAndPortArgs() {
    final Eval eval = new Eval("\"Talk -h test.edu -p 8080\" should call clientMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            assertEquals("test.edu", hostname);
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when clientMode is successful",
        mock.start(new String[] {"-h", "test.edu", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testClientModeCallsAsyncIOAndNotSyncIO()
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
          NoSuchMethodException {
    final Eval eval = new Eval("clientMode should call asyncIO and not syncIO");
    Method clientMode = Talk.class.getDeclaredMethod("clientMode", BasicTalkInterface.class);
    clientMode.setAccessible(true);
    clientMode.invoke(
        new Talk(),
        new BasicTalkInterface() {
          public void asyncIO() throws IOException {
            eval.success();
            throw new IOException("test");
          }

          public void close() {}

          public String status() {
            return "";
          }

          public void syncIO() throws IOException {
            eval.fail();
            throw new IOException("test");
          }
        });
    eval.done();
  }

  @Test
  public void testStartReturnsFalseWhenClientModeFails() {
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return false;
          }
        };
    assertFalse("start should return false when clientMode fails", mock.start(new String[] {"-h"}));
  }

  @Test
  public void testStartReportsWhenClientIsUnableToCommunicateWithServer() {
    Talk mock =
        new Talk() {
          @Override
          public boolean clientMode(String hostname, int portnumber) {
            return super.clientMode("test", 8080);
          }
        };
    try {
      System.setOut(new PrintStream(this.stdoutCapture));
      mock.start(new String[] {"-h"});
      String log = this.stdoutCapture.toString().trim();
      assertTrue(log.contains("Client unable to communicate with server"));
    } finally {
      System.setOut(TalkTest.STDOUT);
    }
  }

  @Test
  public void testStartCallsHelpMode() {
    final Eval eval = new Eval("\"Talk -help\" should call helpMode");
    Talk mock =
        new Talk() {
          @Override
          public void helpMode() {
            eval.success();
          }
        };
    assertTrue(
        "start should return true after calling helpMode", mock.start(new String[] {"-help"}));
    eval.done();
  }

  @Test
  public void testHelpReportsInstructions() {
    try {
      System.setOut(new PrintStream(this.stdoutCapture));
      new Talk().start(new String[] {"-help"});
      String log = this.stdoutCapture.toString().trim();
      assertTrue(log.contains("Talk [-a | -h | -s] [hostname | IPaddress] [-p portnumber]"));
    } finally {
      System.setOut(TalkTest.STDOUT);
    }
  }

  @Test
  public void testStartCallsServerModeWithDefaultArg() {
    final Eval eval = new Eval("\"Talk -s\" should call serverMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            assertEquals(TalkTest.DEFAULT_PORT, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when serverMode is successful", mock.start(new String[] {"-s"}));
    eval.done();
  }

  @Test
  public void testStartCallsServerModeWithPortArg() {
    final Eval eval = new Eval("\"Talk -s -p 8080\" should call serverMode");
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            assertEquals(8080, portnumber);
            eval.success();
            return true;
          }
        };
    assertTrue(
        "start should return true when serverMode is successful",
        mock.start(new String[] {"-s", "-p", "8080"}));
    eval.done();
  }

  @Test
  public void testServerModeCallsAsyncIOAndNotSyncIO()
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
          NoSuchMethodException {
    final Eval eval = new Eval("serverMode should call asyncIO and not syncIO");
    Method serverMode = Talk.class.getDeclaredMethod("serverMode", BasicTalkInterface.class);
    serverMode.setAccessible(true);
    serverMode.invoke(
        new Talk(),
        new BasicTalkInterface() {
          public void asyncIO() throws IOException {
            eval.success();
            throw new IOException("test");
          }

          public void close() {}

          public String status() {
            return "";
          }

          public void syncIO() throws IOException {
            eval.fail();
            throw new IOException("test");
          }
        });
    eval.done();
  }

  @Test
  public void testStartReturnsFalseWhenServerModeFails() {
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            return false;
          }
        };
    assertFalse("start should return false when serverMode fails", mock.start(new String[] {"-s"}));
  }

  @Test
  public void testStartReportsWhenServerIsUnableToListenOnSpecifiedPort() {
    Talk mock =
        new Talk() {
          @Override
          public boolean serverMode(int portnumber) {
            try {
              ServerSocket server = new ServerSocket(portnumber);
              boolean flag = super.serverMode(portnumber);
              server.close();
              return flag;
            } catch (IOException e) {
              return false;
            }
          }
        };
    try {
      System.setOut(new PrintStream(this.stdoutCapture));
      mock.start(new String[] {"-s"});
      String log = this.stdoutCapture.toString().trim();
      assertTrue(log.contains("Server unable to listen on specified port"));
    } finally {
      System.setOut(TalkTest.STDOUT);
    }
  }

  @Test
  public void testTalkClientAsyncIOReportsStdInMessagesToServer()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    try {
      System.setIn(new ByteArrayInputStream("test".getBytes()));
      TalkClient client = constructor.newInstance(socket);
      client.asyncIO();
      assertEquals("test", msg.toString().trim());
    } finally {
      System.setIn(TalkTest.STDIN);
    }
  }

  @Test
  public void testTalkClientAsyncIOReportsServerMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "test\n";
              int cursor = 0;

              @Override
              public int available() {
                return 5;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    try {
      System.setOut(new PrintStream(this.stdoutCapture));
      TalkClient client = constructor.newInstance(socket);
      client.asyncIO();
      String log = this.stdoutCapture.toString();
      assertEquals("[remote] test\n", log);
    } finally {
      System.setOut(TalkTest.STDOUT);
    }
  }

  @Test
  public void testTalkClientSyncIOReportsStdInMessagesToServer()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              @Override
              public int available() {
                return 1;
              }

              public int read() {
                return '\n';
              }
            },
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    try {
      System.setIn(new ByteArrayInputStream("test".getBytes()));
      TalkClient client = constructor.newInstance(socket);
      client.syncIO();
      assertEquals("test", msg.toString().trim());
    } finally {
      System.setIn(TalkTest.STDIN);
    }
  }

  @Test
  public void testTalkClientSyncIOReportsServerMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "test\n";
              int cursor = 0;

              @Override
              public int available() {
                return 5;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    try {
      System.setIn(new ByteArrayInputStream("\n".getBytes()));
      System.setOut(new PrintStream(this.stdoutCapture));
      TalkClient client = constructor.newInstance(socket);
      client.syncIO();
      String log = this.stdoutCapture.toString();
      assertEquals("[remote] test\n", log);
    } finally {
      System.setIn(TalkTest.STDIN);
      System.setOut(TalkTest.STDOUT);
    }
  }

  @Test
  public void testTalkClientReportsStatus()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    MockSocket socket = new MockSocket();
    String status = constructor.newInstance(socket).status();
    assertTrue(
        "TalkClient should report address", status.contains(socket.getInetAddress().toString()));
    assertTrue(
        "TalkClient should report local address",
        status.contains(socket.getLocalAddress().toString()));
    assertTrue(
        "TalkClient should report port", status.contains(Integer.toString(socket.getPort())));
    assertTrue(
        "TalkClient should report local port",
        status.contains(Integer.toString(socket.getLocalPort())));
  }

  @Test
  public void testTalkClientClosesSocketOnClose()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkClient> constructor = TalkClient.class.getDeclaredConstructor(Socket.class);
    constructor.setAccessible(true);
    MockSocket socket = new MockSocket();
    TalkClient client = constructor.newInstance(socket);
    client.close();
    assertTrue("TalkClient should close socket upon close", socket.isClosed());
  }

  @Test
  public void testTalkServerAsyncIOReportsClientMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "test\n";
              int cursor = 0;

              @Override
              public int available() {
                return 5;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    try {
      System.setOut(new PrintStream(this.stdoutCapture));
      TalkServer server = constructor.newInstance(new MockServerSocket(socket));
      server.asyncIO();
      String log = this.stdoutCapture.toString();
      assertEquals("[remote] test\n", log);
    } finally {
      System.setOut(TalkTest.STDOUT);
    }
  }

  @Test
  public void testTalkServerAsyncIOReportsStdInMessagesToClient()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    try {
      System.setIn(new ByteArrayInputStream("test".getBytes()));
      TalkServer server = constructor.newInstance(new MockServerSocket(socket));
      server.asyncIO();
      assertEquals("test", msg.toString().trim());
    } finally {
      System.setIn(TalkTest.STDIN);
    }
  }

  @Test
  public void testTalkServerSyncIOReportsClientMessagesOnStdOut()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              String msg = "test\n";
              int cursor = 0;

              @Override
              public int available() {
                return 5;
              }

              public int read() {
                if (msg.length() <= cursor) return '\0';
                return msg.charAt(cursor++);
              }
            });
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    try {
      System.setIn(new ByteArrayInputStream("\n".getBytes()));
      System.setOut(new PrintStream(this.stdoutCapture));
      TalkServer server = constructor.newInstance(new MockServerSocket(socket));
      server.syncIO();
      String log = this.stdoutCapture.toString();
      assertEquals("[remote] test\n", log);
    } finally {
      System.setIn(TalkTest.STDIN);
      System.setOut(TalkTest.STDOUT);
    }
  }

  @Test
  public void testTalkServerSyncIOReportsStdInMessagesToClient()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    StringBuilder msg = new StringBuilder();
    MockSocket socket =
        new MockSocket(
            new InputStream() {
              @Override
              public int available() {
                return 1;
              }

              public int read() {
                return '\n';
              }
            },
            new OutputStream() {
              public void write(int b) {
                msg.append((char) b);
              }
            });
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    try {
      System.setIn(new ByteArrayInputStream("test".getBytes()));
      TalkServer server = constructor.newInstance(new MockServerSocket(socket));
      server.syncIO();
      assertEquals("test", msg.toString().trim());
    } finally {
      System.setIn(TalkTest.STDIN);
    }
  }

  @Test
  public void testTalkServerReportsStatus()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    MockServerSocket server = new MockServerSocket();
    String status = constructor.newInstance(server).status();
    assertTrue(
        "TalkServer should report address", status.contains(server.getInetAddress().toString()));
    assertTrue(
        "TalkServer should report local port",
        status.contains(Integer.toString(server.getLocalPort())));
  }

  @Test
  public void testTalkServerClosesSocketOnClose()
      throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException,
          NoSuchMethodException {
    Constructor<TalkServer> constructor =
        TalkServer.class.getDeclaredConstructor(ServerSocket.class);
    constructor.setAccessible(true);
    MockSocket socket = new MockSocket();
    MockServerSocket serverSocket = new MockServerSocket(socket);
    TalkServer server = constructor.newInstance(serverSocket);
    server.close();
    assertTrue("TalkServer should close client socket upon close", socket.isClosed());
    assertTrue("TalkServer should close server socket upon close", serverSocket.isClosed());
  }

  /** An Eval object to support assertions in enclosed scopes and async methods. */
  public class Eval {

    String message;
    boolean status;

    public Eval(String message) {
      this.message = message;
      this.status = false;
    }

    public void done() throws AssertionError {
      assertTrue(this.message, this.status);
    }

    public void fail() throws AssertionError {
      throw new AssertionError(this.message);
    }

    public void success() {
      this.status = true;
    }
  }

  public static void main(String[] args) {
    System.out.println("Running tests...\n");

    JUnitCore junitCore = new JUnitCore();
    junitCore.addListener(new CustomRunListener());
    Result result;

    long startTime = System.currentTimeMillis();
    if (args.length == 0 || args[0].toLowerCase().equals("all")) {
      result = junitCore.run(TalkTest.class);
    } else {
      Request request = Request.method(TalkTest.class, args[0]);
      result = junitCore.run(request);
    }
    long testTime = System.currentTimeMillis() - startTime;
    int passingCount = result.getRunCount() - result.getFailureCount();

    System.out.println(String.format("%d passing (%dms)", passingCount, testTime));
    if (result.wasSuccessful()) {
      System.out.println("\u001B[32mALL TESTS PASSED!\u001B[0m\n");
    } else {
      System.out.println("\u001B[31m" + result.getFailureCount() + " TEST FAILURES\u001B[0m\n");
    }

    System.exit(result.wasSuccessful() ? 0 : 1);
  }

  private static class CustomRunListener extends RunListener {

    HashSet<String> failures = new HashSet<>();

    @Override
    public void testFailure(Failure failure) throws Exception {
      this.failures.add(failure.getDescription().getMethodName());
      System.out.println("\u001B[31mFAILED: " + failure.toString() + "\u001B[0m");
    }

    @Override
    public void testFinished(Description description) throws Exception {
      if (!this.failures.contains(description.getMethodName())) {
        System.out.println("\u2714 \u001B[32mPASSED: " + description.getMethodName() + "\u001B[0m");
      }
      System.out.println();
    }

    @Override
    public void testStarted(Description description) throws Exception {
      System.out.println("#" + description.getMethodName());
    }
  }
}