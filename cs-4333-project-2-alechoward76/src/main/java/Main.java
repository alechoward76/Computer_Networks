package main.java;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

  public static void main(String[] args) throws IOException {
    if (args.length >= 1 && args[0].equals("-h")) {
      Main.receiverMode();
      return;
    }
    if (args.length >= 1 && args[0].equals("-s")) {
      Main.senderMode();
      return;
    }
    System.exit(1);
  }

  private static void receiverMode() throws IOException {
    RReceiveUDP receiver = new RReceiveUDP();
    receiver.setMode(1);
    receiver.setModeParameter(512);
    receiver.setFilename("winter_novel.txt");
    receiver.setLocalPort(32456);
    receiver.receiveFile();
    receiver.close();
  }

  private static void senderMode() throws IOException {
    RSendUDP sender = new RSendUDP();
    sender.setMode(1);
    sender.setModeParameter(512);
    sender.setTimeout(10000);
    sender.setFilename("manuscript.txt");
    sender.setLocalPort(23456);
    sender.setReceiver(new InetSocketAddress("localhost", 32456));
    sender.sendFile();
    sender.close();
  }
}
