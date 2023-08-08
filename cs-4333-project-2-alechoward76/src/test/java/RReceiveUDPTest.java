package test.java;

/**
 * DO NOT DISTRIBUTE.
 *
 * This code is intended to support the education of students associated with the Tandy School of
 * Computer Science at the University of Tulsa. It is not intended for distribution and should
 * remain within private repositories that belong to Tandy faculty, students, and/or alumni.
 */
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.junit.MatcherAssume.assumeThat;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import main.java.RReceiveUDP;
import main.java.RSendUDP;
import org.junit.After;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test.java.TUGrader.Deps;
import test.java.TUGrader.DisplayName;
import test.java.TUGrader.TestGroup;

public class RReceiveUDPTest {

  private static final String MONSTER_MASH =
      "I was working in the lab, late one night"
          + "When my eyes beheld an eerie sight"
          + "For my monster from his slab, began to rise"
          + "And suddenly to my surprise"
          + "He did the monster mash"
          + "(The monster mash) It was a graveyard smash"
          + "(He did the mash) It caught on in a flash"
          + "(He did the mash) He did the monster mash"
          + "From my laboratory in the castle east"
          + "To the master bedroom where the vampires feast"
          + "The ghouls all came from their humble abodes"
          + "To get a jolt from my electrodes"
          + "They did the monster mash"
          + "(The monster mash) It was a graveyard smash"
          + "(They did the mash) It caught on in a flash"
          + "(They did the mash) They did the monster mash"
          + "The zombies were having fun (Wa hoo, tennis shoe)"
          + "The party had just begun (Wa hoo, tennis shoe)"
          + "The guests included Wolfman, Dracula and his son"
          + "The scene was rockin', all were digging the sounds"
          + "Igor on chains, backed by his baying hounds"
          + "The coffin-bangers were about to arrive"
          + "With their vocal group, 'The Crypt-Kicker Five'"
          + "They played the monster mash"
          + "(The monster mash) It was a graveyard smash"
          + "(They played the mash) It caught on in a flash"
          + "(They played the mash) They played the monster mash"
          + "Out from his coffin, Drac's voice did ring"
          + "Seems he was troubled by just one thing"
          + "He opened the lid and shook his fist and said"
          + "Whatever happened to my Transylvania Twist?"
          + "It's now the monster mash"
          + "(The monster mash) And it's a graveyard smash"
          + "(It's now the mash) It's caught on in a flash"
          + "(It's now the mash) It's now the monster mash"
          + "Now everything's cool, Drac's a part of the band"
          + "And my Monster Mash is the hit of the land"
          + "For you, the living, this mash was meant too"
          + "When you get to my door, tell them Boris sent you"
          + "Then you can monster mash"
          + "(The monster mash) And do my graveyard smash"
          + "(Then you can mash) You'll catch on in a flash"
          + "(Then you can mash) Then you can monster mash"
          + "Easy Igor, you impetuous young boy (Wa hoo, monster mash)"
          + "(Wa hoo, monster mash)"
          + "(Wa hoo, monster mash)"
          + "(Wa hoo, monster mash)"
          + "(Wa hoo, monster mash)";
  private static final InputStream STDIN = System.in;
  private static final PrintStream STDOUT = System.out;

  private static String diff(String s, String t) {
    int j = 0;
    int maxK = 0;
    while (j < t.length()) {
      while (j < t.length() && s.charAt(0) != t.charAt(j)) {
        j++;
      }
      if (j == t.length()) {
        continue;
      }
      int k = 0;
      while (k < s.length() && (j + k) < t.length() && s.charAt(k) == t.charAt(j + k)) {
        k++;
      }
      maxK = Math.max(maxK, k);
      j++;
    }
    return s.substring(maxK);
  }

  private static boolean isEmptyBuffer(byte[] buffer) {
    if (buffer == null || buffer.length == 0) {
      return true;
    }
    for (int i = 0; i < buffer.length; i++) {
      if (buffer[i] > 0) {
        return false;
      }
    }
    return true;
  }

  private static String safeClose(Closeable closeable) {
    try {
      closeable.close();
      return null;
    } catch (IOException e) {
      return e.getMessage();
    }
  }

  @Rule public TemporaryFolder folder = new TemporaryFolder();

  private File file = null;
  private RReceiveUDP receiver = null;
  private ByteArrayOutputStream stdoutCapture = new ByteArrayOutputStream();

  @Before
  public void init() {
    try {
      this.file = this.folder.newFile("__test__.txt");
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    this.receiver = new RReceiveUDP();
    System.setOut(new PrintStream(this.stdoutCapture));
  }

  @After
  public void close() {
    System.setIn(RReceiveUDPTest.STDIN);
    System.setOut(RReceiveUDPTest.STDOUT);
    boolean ok = this.file.delete();
    String err = RReceiveUDPTest.safeClose(this.receiver);
    if (!ok) {
      throw new AssumptionViolatedException("File could not be deleted");
    }
    if (err != null) {
      throw new AssumptionViolatedException(err);
    }
  }

  @Test
  @TestGroup("format")
  @DisplayName("formatACK(int) should return an ACK")
  public void testFormatACKDoesNotReturnAnEmptyBuffer() {
    assertThat(RReceiveUDPTest.isEmptyBuffer(RReceiveUDP.formatACK(1)), is(false));
  }

  @Test
  @TestGroup("filename")
  @DisplayName("should be able to get and set filename")
  @Deps({"getFilename()", "setFilename(String)"})
  public void testFilename() {
    receiver.setFilename("test.txt");
    assertThat(receiver.getFilename(), is(equalTo("test.txt")));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("should be able to get and set local port")
  @Deps({"getLocalPort()", "setLocalPort(int)"})
  public void testLocalPort() {
    this.receiver.setLocalPort(32456);
    assertThat(this.receiver.getLocalPort(), is(equalTo(32456)));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName(
      "getLocalPort() should return 12987 as the default port if the port has not been set")
  public void testGetLocalPortReturnsDefaultPort() {
    assertThat(this.receiver.getLocalPort(), is(equalTo(12987)));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should return true after setting a valid port")
  public void testSetLocalPortReturnsTrue() {
    assertThat(this.receiver.setLocalPort(23456), is(true));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should return false if the input port is negative")
  public void testSetLocalPortReturnsFalseIfPortIsNegative() {
    assertThat(this.receiver.setLocalPort(-1), is(false));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should return false if the input port is greater than 65,535")
  public void testSetLocalPortReturnsFalseIfPortIsTooHigh() {
    assertThat(this.receiver.setLocalPort(66_000), is(false));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should not alter the port if the input port is invalid")
  @Deps({"getLocalPort()", "setLocalPort(int)"})
  public void testSetLocalPortDoesNotChangePortIfInputIsInvalid() {
    assumeThat(this.receiver.getLocalPort(), is(equalTo(12987)));
    assumeThat(this.receiver.setLocalPort(-1), is(false));
    assertThat(this.receiver.getLocalPort(), is(equalTo(12987)));
    assumeThat(this.receiver.setLocalPort(66_000), is(false));
    assertThat(this.receiver.getLocalPort(), is(equalTo(12987)));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("should be able to get and set mode")
  @Deps({"getMode()", "setMode(int)"})
  public void testMode() {
    this.receiver.setMode(1);
    assertThat(this.receiver.getMode(), is(equalTo(1)));
  }

  @Test
  @TestGroup("mode")
  @DisplayName(
      "getMode() should return stop-and-wait (0) as the default mode if the mode has not been set")
  public void testGetModeReturnsDefaultMode() {
    assertThat(this.receiver.getMode(), is(equalTo(0)));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("setMode(int) should return true after setting the mode to 0 or 1")
  public void testSetModeReturnsTrue() {
    assertThat(this.receiver.setMode(0), is(true));
    assertThat(this.receiver.setMode(1), is(true));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("setMode(int) should return false if the input mode is not 0 or 1")
  public void testSetModeReturnsFalseOnInvalidMode() {
    assertThat(this.receiver.setMode(-1), is(false));
    assertThat(this.receiver.setMode(2), is(false));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("setMode(int) should not alter the mode if the input mode is not 0 or 1")
  @Deps({"getMode()", "setMode(int)"})
  public void testSetModeDoesNotChangeModeIfInputIsInvalid() {
    assumeThat(this.receiver.getMode(), is(equalTo(0)));
    assumeThat(this.receiver.setMode(-1), is(false));
    assertThat(this.receiver.getMode(), is(equalTo(0)));
    assumeThat(this.receiver.setMode(2), is(false));
    assertThat(this.receiver.getMode(), is(equalTo(0)));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName("should be able to get and set mode parameter")
  @Deps({"getModeParameter()", "setModeParameter(long)"})
  public void testModeParameter() {
    this.receiver.setModeParameter(512);
    assertThat(this.receiver.getModeParameter(), is(equalTo(512l)));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName(
      "getModeParameter() should return 256 as the default mode parameter if the mode parameter has"
          + " not been set")
  public void testGetModeParameterReturnsDefaultModeParameter() {
    assertThat(this.receiver.getModeParameter(), is(equalTo(256l)));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName("setModeParameter(long) should return true after setting the mode parameter")
  public void testSetModeParameterReturnsTrue() {
    assertThat(this.receiver.setModeParameter(512), is(true));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName("setModeParameter(long) should return false if the input parameter is leq 0")
  public void testSetModeParameterReturnsFalseIfInputIsInvalid() {
    assertThat(this.receiver.setModeParameter(0), is(false));
    assertThat(this.receiver.setModeParameter(-1), is(false));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName(
      "setModeParameter(long) should not alter the mode parameter if the input parameter is leq 0")
  @Deps({"getModeParameter()", "setModeParameter(long)"})
  public void testSetModeParameterDoesNotChangeParameterIfInputIsInvalid() {
    assumeThat(this.receiver.setModeParameter(512), is(true));
    assumeThat(this.receiver.setModeParameter(0), is(false));
    assertThat(this.receiver.setModeParameter(512), is(true));
    assumeThat(this.receiver.setModeParameter(-1), is(false));
    assertThat(this.receiver.setModeParameter(512), is(true));
  }

  @Test
  @TestGroup("receiveFile")
  @DisplayName("receiveFile() should receive the file using stop-and-wait")
  public void testReceiveFileWithStopAndWait() {
    assumeThat(this.file.exists(), is(true));
    try {
      String[] lines = Files.readString(Path.of("unet.properties")).trim().split("\n");
      final int frameSize = Integer.parseInt(lines[lines.length - 1].split("=")[1].trim());
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  try {
                    DatagramSocket sender = new DatagramSocket(23459);
                    LinkedList<byte[]> frames = new LinkedList<>();
                    String content = RReceiveUDPTest.MONSTER_MASH;
                    for (int k = 0; !content.isEmpty(); k++) {
                      byte[] buffer =
                          RSendUDP.formatPacket(RReceiveUDPTest.MONSTER_MASH, k, frameSize);
                      String diff = RReceiveUDPTest.diff(content, new String(buffer));
                      assumeThat(diff, is(not(equalTo(content))));
                      content = diff;
                      frames.add(buffer);
                    }
                    while (!frames.isEmpty()) {
                      boolean acked = false;
                      int timeouts = 0;
                      while (!acked) {
                        DatagramPacket packet =
                            new DatagramPacket(
                                frames.peek(),
                                frameSize,
                                new InetSocketAddress("localhost", 32459));
                        sender.send(packet);
                        Thread.sleep(100);
                        try {
                          sender.setSoTimeout(3_000);
                          DatagramPacket ack = new DatagramPacket(new byte[frameSize], frameSize);
                          sender.receive(ack);
                          acked = true;
                        } catch (SocketTimeoutException e) {
                          timeouts++;
                          if (timeouts == 3) {
                            sender.close();
                            throw new AssumptionViolatedException(e.getMessage());
                          }
                        }
                      }
                      frames.poll();
                    }
                    sender.close();
                  } catch (InterruptedException | IOException e) {
                    throw new AssumptionViolatedException(e.getMessage());
                  }
                }
              });
      thread.start();
      receiver.setMode(0);
      receiver.setFilename(this.file.getAbsolutePath());
      receiver.setLocalPort(32459);
      receiver.receiveFile();
      thread.join();
      assertThat(
          Files.readString(Path.of(this.file.getAbsolutePath())).trim(),
          is(equalTo(RReceiveUDPTest.MONSTER_MASH)));
    } catch (InterruptedException | IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
  }

  @Test
  @TestGroup("receiveFile")
  @DisplayName("receiveFile() should receive the file using sliding window")
  public void testReceiveFileWithSlidingWindow() {
    assumeThat(this.file.exists(), is(true));
    try {
      String[] lines = Files.readString(Path.of("unet.properties")).trim().split("\n");
      final int frameSize = Integer.parseInt(lines[lines.length - 1].split("=")[1].trim());
      final int windowSize = 1024;
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  try {
                    DatagramSocket sender = new DatagramSocket(23460);
                    LinkedList<byte[]> frames = new LinkedList<>();
                    String content = RReceiveUDPTest.MONSTER_MASH;
                    for (int k = 0; !content.isEmpty(); k++) {
                      byte[] buffer =
                          RSendUDP.formatPacket(RReceiveUDPTest.MONSTER_MASH, k, frameSize);
                      String diff = RReceiveUDPTest.diff(content, new String(buffer));
                      assumeThat(diff, is(not(equalTo(content))));
                      content = diff;
                      frames.add(buffer);
                    }
                    while (!frames.isEmpty()) {
                      boolean acked = false;
                      int timeouts = 0;
                      while (!acked) {
                        for (int i = 0; i < Math.min(frames.size(), windowSize / frameSize); i++) {
                          DatagramPacket packet =
                              new DatagramPacket(
                                  frames.get(i),
                                  frameSize,
                                  new InetSocketAddress("localhost", 32460));
                          sender.send(packet);
                          Thread.sleep(250);
                        }
                        try {
                          sender.setSoTimeout(3_000);
                          DatagramPacket ack = new DatagramPacket(new byte[frameSize], frameSize);
                          sender.receive(ack);
                          acked = true;
                        } catch (SocketTimeoutException e) {
                          timeouts++;
                          if (timeouts == 3) {
                            sender.close();
                            throw new AssumptionViolatedException(e.getMessage());
                          }
                        }
                      }
                      frames.poll();
                    }
                    sender.close();
                  } catch (InterruptedException | IOException e) {
                    throw new AssumptionViolatedException(e.getMessage());
                  }
                }
              });
      thread.start();
      receiver.setMode(1);
      receiver.setModeParameter(windowSize);
      receiver.setFilename(this.file.getAbsolutePath());
      receiver.setLocalPort(32460);
      receiver.receiveFile();
      thread.join();
      assertThat(
          Files.readString(Path.of(this.file.getAbsolutePath())).trim(),
          is(equalTo(RReceiveUDPTest.MONSTER_MASH)));
    } catch (InterruptedException | IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
  }
}