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
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
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

public class RSendUDPTest {

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
  private RSendUDP sender = null;
  private ByteArrayOutputStream stdoutCapture = new ByteArrayOutputStream();

  @Before
  public void init() {
    try {
      this.file = this.folder.newFile("__test__.txt");
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    this.sender = new RSendUDP();
    System.setOut(new PrintStream(this.stdoutCapture));
  }

  @After
  public void close() {
    System.setIn(RSendUDPTest.STDIN);
    System.setOut(RSendUDPTest.STDOUT);
    boolean ok = this.file.delete();
    String err = RSendUDPTest.safeClose(this.sender);
    if (!ok) {
      throw new AssumptionViolatedException("File could not be deleted");
    }
    if (err != null) {
      throw new AssumptionViolatedException(err);
    }
  }

  @Test
  @TestGroup("format")
  @DisplayName(
      "formatPacket(String, int, int) should return a byte buffer whose length is the input frame"
          + " size")
  public void testFormatPacketReturnsByteBufferWithLengthOfFrameSize() {
    assertThat(RSendUDP.formatPacket(RSendUDPTest.MONSTER_MASH, 0, 128).length, is(equalTo(128)));
  }

  @Test
  @TestGroup("format")
  @DisplayName("formatPacket(String, int, int) should format the first packet of the input string")
  public void testFormatPacketReturnsFirstPacketForString() {
    String data = new String(RSendUDP.formatPacket(RSendUDPTest.MONSTER_MASH, 0, 128));
    assertThat(
        RSendUDPTest.diff(RSendUDPTest.MONSTER_MASH, data),
        is(not(equalTo(RSendUDPTest.MONSTER_MASH))));
  }

  @Test
  @TestGroup("format")
  @DisplayName("formatPacket(String, int, int) should format all packets for string")
  public void testFormatPacketReturnsAllPacketsForString() {
    String content = RSendUDPTest.MONSTER_MASH;
    for (int i = 0; !content.isEmpty(); i++) {
      String data = new String(RSendUDP.formatPacket(RSendUDPTest.MONSTER_MASH, i, 128));
      String diff = RSendUDPTest.diff(content, data);
      assertThat(diff, is(not(equalTo(content))));
      content = diff;
    }
  }

  @Test
  @TestGroup("filename")
  @DisplayName("should be able to get and set filename")
  @Deps({"getFilename()", "setFilename(String)"})
  public void testFilename() {
    this.sender.setFilename("test.txt");
    assertThat(this.sender.getFilename(), is(equalTo("test.txt")));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("should be able to get and set local port")
  @Deps({"getLocalPort()", "setLocalPort(int)"})
  public void testLocalPort() {
    this.sender.setLocalPort(23456);
    assertThat(this.sender.getLocalPort(), is(equalTo(23456)));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName(
      "getLocalPort() should return 12987 as the default port if the port has not been set")
  public void testGetLocalPortReturnsDefaultPort() {
    assertThat(this.sender.getLocalPort(), is(equalTo(12987)));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should return true after setting a valid port")
  public void testSetLocalPortReturnsTrue() {
    assertThat(this.sender.setLocalPort(23456), is(true));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should return false if the input port is negative")
  public void testSetLocalPortReturnsFalseIfPortIsNegative() {
    assertThat(this.sender.setLocalPort(-1), is(false));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should return false if the input port is greater than 65,535")
  public void testSetLocalPortReturnsFalseIfPortIsTooHigh() {
    assertThat(this.sender.setLocalPort(66_000), is(false));
  }

  @Test
  @TestGroup("localPort")
  @DisplayName("setLocalPort(int) should not alter the port if the input port is invalid")
  @Deps({"getLocalPort()", "setLocalPort(int)"})
  public void testSetLocalPortDoesNotChangePortIfInputIsInvalid() {
    assumeThat(this.sender.getLocalPort(), is(equalTo(12987)));
    assumeThat(this.sender.setLocalPort(-1), is(false));
    assertThat(this.sender.getLocalPort(), is(equalTo(12987)));
    assumeThat(this.sender.setLocalPort(66_000), is(false));
    assertThat(this.sender.getLocalPort(), is(equalTo(12987)));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("should be able to get and set mode")
  @Deps({"getMode()", "setMode(int)"})
  public void testMode() {
    this.sender.setMode(1);
    assertThat(sender.getMode(), is(equalTo(1)));
  }

  @Test
  @TestGroup("mode")
  @DisplayName(
      "getMode() should return stop-and-wait (0) as the default mode if the mode has not been set")
  public void testGetModeReturnsDefaultMode() {
    assertThat(this.sender.getMode(), is(equalTo(0)));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("setMode(int) should return true after setting the mode to 0 or 1")
  public void testSetModeReturnsTrue() {
    assertThat(this.sender.setMode(0), is(true));
    assertThat(this.sender.setMode(1), is(true));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("setMode(int) should return false if the input mode is not 0 or 1")
  public void testSetModeReturnsFalseOnInvalidMode() {
    assertThat(this.sender.setMode(-1), is(false));
    assertThat(this.sender.setMode(2), is(false));
  }

  @Test
  @TestGroup("mode")
  @DisplayName("setMode(int) should not alter the mode if the input mode is not 0 or 1")
  @Deps({"getMode()", "setMode(int)"})
  public void testSetModeDoesNotChangeModeIfInputIsInvalid() {
    assumeThat(this.sender.getMode(), is(equalTo(0)));
    assumeThat(this.sender.setMode(-1), is(false));
    assertThat(this.sender.getMode(), is(equalTo(0)));
    assumeThat(this.sender.setMode(2), is(false));
    assertThat(this.sender.getMode(), is(equalTo(0)));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName("should be able to get and set mode parameter")
  @Deps({"getModeParameter()", "setModeParameter(long)"})
  public void testModeParameter() {
    this.sender.setModeParameter(512);
    assertThat(this.sender.getModeParameter(), is(equalTo(512l)));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName(
      "getModeParameter() should return 256 as the default mode parameter if the mode parameter has"
          + " not been set")
  public void testGetModeParameterReturnsDefaultModeParameter() {
    assertThat(this.sender.getModeParameter(), is(equalTo(256l)));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName("setModeParameter(long) should return true after setting the mode parameter")
  public void testSetModeParameterReturnsTrue() {
    assertThat(this.sender.setModeParameter(512), is(true));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName("setModeParameter(long) should return false if the input parameter is leq 0")
  public void testSetModeParameterReturnsFalseIfInputIsInvalid() {
    assertThat(this.sender.setModeParameter(0), is(false));
    assertThat(this.sender.setModeParameter(-1), is(false));
  }

  @Test
  @TestGroup("modeParameter")
  @DisplayName(
      "setModeParameter(long) should not alter the mode parameter if the input parameter is leq 0")
  @Deps({"getModeParameter()", "setModeParameter(long)"})
  public void testSetModeParameterDoesNotChangeParameterIfInputIsInvalid() {
    assumeThat(this.sender.setModeParameter(512), is(true));
    assumeThat(this.sender.setModeParameter(0), is(false));
    assertThat(this.sender.setModeParameter(512), is(true));
    assumeThat(this.sender.setModeParameter(-1), is(false));
    assertThat(this.sender.setModeParameter(512), is(true));
  }

  @Test
  @TestGroup("receiver")
  @DisplayName("should be able to get and set receiver")
  @Deps({"getReceiver()", "setReceiver(InetSocketAddress)"})
  public void testReceiver() {
    this.sender.setReceiver(new InetSocketAddress("localhost", 32456));
    assertThat(this.sender.getReceiver(), is(equalTo(new InetSocketAddress("localhost", 32456))));
  }

  @Test
  @TestGroup("receiver")
  @DisplayName(
      "getReceiver() should return receiver at \"localhost:12987\" as the default receiver if the"
          + " receiver has not been set")
  public void testGetReceiverReturnsDefaultReceiver() throws UnknownHostException {
    assertThat(
        this.sender.getReceiver(),
        is(
            anyOf(
                equalTo(new InetSocketAddress("localhost", 12987)),
                equalTo(new InetSocketAddress(InetAddress.getLocalHost(), 12987)),
                equalTo(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 12987)),
                equalTo(new InetSocketAddress(InetAddress.getLocalHost().getHostName(), 12987)))));
  }

  @Test
  @TestGroup("receiver")
  @DisplayName("setReceiver(InetSocketAddress) should return true after setting the receiver")
  public void testSetReceiverReturnsTrue() {
    assertThat(this.sender.setReceiver(new InetSocketAddress("localhost", 32456)), is(true));
  }

  @Test
  @TestGroup("sendFile")
  @DisplayName("sendFile() should send the file using stop-and-wait")
  public void testSendFileWithStopAndWait() {
    assumeThat(this.file.exists(), is(true));
    BufferedWriter fw;
    try {
      fw = new BufferedWriter(new FileWriter(this.file));
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    DatagramSocket receiver;
    try {
      receiver = new DatagramSocket(32457);
    } catch (IOException e) {
      RSendUDPTest.safeClose(fw);
      throw new AssumptionViolatedException(e.getMessage());
    }
    try {
      fw.write(RSendUDPTest.MONSTER_MASH);
      fw.close();
      String[] lines = Files.readString(Path.of("unet.properties")).trim().split("\n");
      final int frameSize = Integer.parseInt(lines[lines.length - 1].split("=")[1].trim());
      final String filepath = this.file.getAbsolutePath();
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  sender.setMode(0);
                  sender.setTimeout(5_000);
                  sender.setFilename(filepath);
                  sender.setLocalPort(23457);
                  sender.setReceiver(new InetSocketAddress("localhost", 32457));
                  sender.sendFile();
                }
              });
      thread.start();
      String content = RSendUDPTest.MONSTER_MASH;
      int k = 0;
      while (!content.isEmpty()) {
        byte[] buffer = new byte[frameSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        receiver.receive(packet);
        String diff = RSendUDPTest.diff(content, new String(packet.getData()));
        assertThat("did not receive next file content", diff, is(not(equalTo(content))));
        content = diff;
        byte[] ack = RReceiveUDP.formatACK(k);
        receiver.send(new DatagramPacket(ack, ack.length, packet.getAddress(), packet.getPort()));
        k++;
      }
      thread.join();
    } catch (InterruptedException | IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    } finally {
      RSendUDPTest.safeClose(fw);
      receiver.close();
    }
  }

  @Test
  @TestGroup("sendFile")
  @DisplayName("sendFile() should send the file using sliding window")
  public void testSendFileWithSlidingWindow() {
    assumeThat(this.file.exists(), is(true));
    BufferedWriter fw;
    try {
      fw = new BufferedWriter(new FileWriter(this.file));
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    DatagramSocket receiver;
    try {
      receiver = new DatagramSocket(32458);
    } catch (IOException e) {
      RSendUDPTest.safeClose(fw);
      throw new AssumptionViolatedException(e.getMessage());
    }
    try {
      final int windowSize = 1024;
      String[] lines = Files.readString(Path.of("unet.properties")).trim().split("\n");
      int frameSize = Integer.parseInt(lines[lines.length - 1].split("=")[1].trim());
      fw.write(RSendUDPTest.MONSTER_MASH);
      fw.close();
      final String filepath = this.file.getAbsolutePath();
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  sender.setMode(1);
                  sender.setModeParameter(windowSize);
                  sender.setTimeout(5_000);
                  sender.setFilename(filepath);
                  sender.setLocalPort(23458);
                  sender.setReceiver(new InetSocketAddress("localhost", 32458));
                  sender.sendFile();
                }
              });
      thread.start();
      String content = RSendUDPTest.MONSTER_MASH;
      int k = 0;
      int maxMisses = (windowSize * 3) / frameSize;
      int misses = 0;
      InetAddress senderAddr = null;
      int senderPort = -1;
      while (!content.isEmpty() && misses < maxMisses) {
        byte[] buffer = new byte[frameSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
          receiver.setSoTimeout(3_000);
          receiver.receive(packet);
        } catch (SocketTimeoutException e) {
          misses++;
          continue;
        }
        if (RSendUDPTest.isEmptyBuffer(buffer)) {
          misses++;
          continue;
        }
        if (senderAddr == null) {
          senderAddr = packet.getAddress();
          senderPort = packet.getPort();
        }
        String diff = RSendUDPTest.diff(content, new String(packet.getData()));
        if (diff.equals(content)) {
          misses++;
          continue;
        } else {
          misses = 0;
        }
        byte[] ack = RReceiveUDP.formatACK(k);
        receiver.send(new DatagramPacket(ack, ack.length, senderAddr, senderPort));
        content = diff;
        // System.out.printf("\n\n%s\n\n", content);
        k++;
      }
      assertThat("did not receive entire file content", content, isEmptyString());
      thread.join();
    } catch (InterruptedException | IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    } finally {
      RSendUDPTest.safeClose(fw);
      receiver.close();
    }
  }

  @Test
  @TestGroup("timeout")
  @DisplayName("should be able to get and set timeout")
  @Deps({"getTimeout()", "setTimeout(long)"})
  public void testTimeout() {
    sender.setTimeout(10_000);
    assertThat(sender.getTimeout(), is(equalTo(10_000l)));
  }

  @Test
  @TestGroup("timeout")
  @DisplayName(
      "getTimeout() should return 1000ms timeout as the default timeout if the timeout has not been"
          + " set")
  public void testGetTimeoutReturnsDefaultTimeout() {
    assertThat(sender.getTimeout(), is(equalTo(1_000l)));
  }

  @Test
  @TestGroup("timeout")
  @DisplayName("setTimeout(long) should return true after setting the timeout")
  public void testSetTimeoutReturnsTrue() {
    assertThat(sender.setTimeout(10_000), is(true));
  }

  @Test
  @TestGroup("timeout")
  @DisplayName("setTimeout(long) should return false if timeout is leq 0")
  public void testSetTimeoutReturnsFalseIfInputIsInvalid() {
    assertThat(sender.setTimeout(0), is(false));
    assertThat(sender.setTimeout(-1), is(false));
  }

  @Test
  @TestGroup("timeout")
  @DisplayName("setTimeout(long) should return true after setting the timeout")
  @Deps({"getTimeout()", "setTimeout(long)"})
  public void testSetTimeoutDoesNotChangeTimeoutIfInputIsInvalid() {
    assumeThat(sender.setTimeout(10_000), is(true));
    assumeThat(sender.setTimeout(0), is(false));
    assertThat(sender.setTimeout(10_000), is(true));
    assumeThat(sender.setTimeout(-1), is(false));
    assertThat(sender.setTimeout(10_000), is(true));
  }
}