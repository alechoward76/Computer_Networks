package main.java;

/** A thread-runnable http handler for http requests. */
public class HttpHandler implements Runnable {

  /** Spawns a new thread to run a handler. */
  public static void spawn(HttpServer server) {
    Thread thread = new Thread(new HttpHandler(server));
    thread.start();
  }

  private HttpServer server;

  /** Constructs a handler for http requests to the server. */
  public HttpHandler(HttpServer server) {
    this.server = server;
  }

  /** The work to run in a thread. */
  public void run() {
    // TODO: implement run()
    throw new UnsupportedOperationException("run() not yet implemented");
  }
}
