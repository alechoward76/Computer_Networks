package test.java;

/**
 * DO NOT DISTRIBUTE.
 *
 * This code is intended to support the education of students associated with the Tandy School of
 * Computer Science at the University of Tulsa. It is not intended for distribution and should
 * remain within private repositories that belong to Tandy faculty, students, and/or alumni.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/** An auto-grader for assignments delivered by the Tandy School of Computer Science at TU. */
public class TUGrader {

  public static final String VERSION = "2022.10.07";

  public static void main(String[] args) {
    System.exit((new TUGrader().run(args).wasSuccessful()) ? 0 : 1);
  }

  private JUnitCore junit;
  private TUGraderListener listener;
  private Logger logger;
  private HashMap<String, Method> testCases;
  private HashMap<String, Class<?>> tests;

  public TUGrader(Class<?>... tests) {
    this(null, tests);
  }

  public TUGrader(Logger logger, Class<?>... tests) {
    this.initJUnit();
    this.initLogger(logger);
    this.initTests(tests);
  }

  public Method[] getTestCases() {
    return this.getTestCasesToArray(this.testCases.values());
  }

  public Class<?>[] getTests() {
    return this.getTestsToArray(this.tests.values());
  }

  public Result run() {
    return this.run(new String[0]);
  }

  public Result run(String[] args) {
    this.log(
        "\n"
            + "                %==========================================%\n"
            + "                            .  o ..\n"
            + "                            o . o o.o\n"
            + "                                 ...oo\n"
            + "                                   __[]__\n"
            + "                                __|_o_o_o\\__\n"
            + "                                \"\"\"\"\"\"\"\"\"\"/\n"
            + "                                 \\. ..  . /\n"
            + "                            ^^^^^^^^^^^^^^^^^^^^\n"
            + "                          TUG - The TU Auto-Grader\n"
            + "                %==========================================%\n");
    return this.runArgs(args, true);
  }

  private String getDisplayName(Method method) {
    if (method == null) {
      throw new RuntimeException("Unrecognized test case `null`");
    }
    if (method.isAnnotationPresent(DisplayName.class)) {
      return method.getAnnotation(DisplayName.class).value();
    }
    if (method.getName().contains("_")) {
      return method.getName().replace("_", " ");
    }
    return String.format("#%s", method.getName());
  }

  private Method[] getTestCasesToArray(Collection<Method> testCases) {
    if (testCases == null || testCases.isEmpty()) {
      return new Method[0];
    }
    return testCases.toArray(Method[]::new);
  }

  private Class<?>[] getTestsToArray(Collection<Class<?>> tests) {
    if (tests == null || tests.isEmpty()) {
      return new Class<?>[0];
    }
    return tests.toArray(Class<?>[]::new);
  }

  private void initJUnit() {
    this.junit = new JUnitCore();
    this.listener = new TUGraderListener();
    this.junit.addListener(this.listener);
  }

  private void initLogger(Logger logger) {
    if (logger != null) {
      this.logger = logger;
      return;
    }
    this.logger = Logger.getLogger(TUGrader.class.getName());
    this.logger.setLevel(Level.ALL);
    this.logger.addHandler(
        new Handler() {
          @Override
          public void close() {}

          @Override
          public void flush() {}

          @Override
          public void publish(LogRecord record) {
            System.out.println(record.getMessage());
          }
        });
  }

  private void initTests(Class<?>... tests) {
    if (this.tests == null) {
      this.tests = new HashMap<>();
    }
    if (this.testCases == null) {
      this.testCases = new HashMap<>();
    }
    for (Class<?> test : tests) {
      this.tests.put(test.getSimpleName(), test);
      for (Method method : test.getDeclaredMethods()) {
        if (!method.isAnnotationPresent(Test.class)) {
          continue;
        }
        if (!this.testCases.containsKey(method.getName())) {
          this.testCases.put(method.getName(), method);
        } else {
          throw new RuntimeException(
              String.format("Duplicate method name `%s` @ %s", method.getName(), test.getName()));
        }
      }
    }
  }

  private void log(String format, Object... args) {
    if (args.length == 0) {
      this.logger.log(this.logger.getLevel(), format);
    } else {
      this.logger.log(this.logger.getLevel(), String.format(format, args));
    }
  }

  private void logFailure(String format, Object... args) {
    this.log(String.format("\u2717 \u001B[31m%s\u001B[0m", format), args);
  }

  private void logInfo(String format, Object... args) {
    this.log(String.format("\u001B[36m%s\u001B[0m", format), args);
  }

  private void logSuccess(String format, Object... args) {
    this.log(String.format("\u2714 \u001B[32m%s\u001B[0m", format), args);
  }

  private void logWarning(String format, Object... args) {
    this.log(String.format("\u2717 \u001B[33m%s\u001B[0m", format), args);
  }

  private void report(Result result) {
    this.log("%=============%\n% TEST REPORT %\n%=============%");
    this.log("%d passing (%dms)", result.getPassingCount(), result.getRunTime());
    if (result.wasSuccessful()) {
      this.logSuccess(("ALL TESTS PASSED!\n"));
    } else {
      this.logFailure("%d TEST FAILURES\n", result.getFailureCount());
    }
  }

  private void reset() {
    this.listener.clear();
  }

  private Result run(Class<?> test, boolean report) {
    return this.run(Request.classes(test), report);
  }

  private Result run(Class<?>[] tests, boolean report) {
    if (tests.length == 0) {
      return new Result();
    }
    return this.run(Request.classes(tests), report);
  }

  private Result run(Method testCase, boolean report) {
    return this.run(Request.method(testCase.getDeclaringClass(), testCase.getName()), report);
  }

  private Result run(Request request, boolean report) {
    Result result = Result.from(this.junit.run(request));
    if (report) {
      this.report(result);
    }
    this.reset();
    return result;
  }

  private Result runArgs(String[] args, boolean report) {
    if (args.length == 0) {
      return this.runTestConfiguration();
    } else if (args[0].toLowerCase().equals("all")) {
      return this.runAllTests(report);
    } else if (args[0].equals("-f") || args[0].equals("--file")) {
      if (args.length != 2) {
        throw new RuntimeException(
            String.format("Unrecognized grader configuration `%s`", Arrays.toString(args)));
      }
      return this.runTestConfiguration(args[1]);
    } else if (args[0].charAt(0) == '#' || args[0].charAt(0) == '.' || args[0].charAt(0) == '!') {
      return this.runTestsWithFilter(args, report);
    } else if (args[0].equals("-help") || args[0].equals("--help")) {
      return this.runHelp();
    } else if (args[0].equals("-i") || args[0].equals("--interactive")) {
      return this.runInteractively(args);
    } else if (args[0].equals("-v") || args[0].equals("--version")) {
      return this.runVersion();
    } else if (args[0].substring(0, 2).equals("--")) {
      return this.runCustomTests(args, report);
    } else {
      return this.runAllTests(args, report);
    }
  }

  private Result runAllTests(boolean report) {
    return this.runAllTests(new String[0], report);
  }

  private Result runAllTests(String[] args, boolean report) {
    if (args.length == 0) {
      return this.run(this.getTests(), report);
    }
    ArrayList<Class<?>> tests = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      if (!this.tests.containsKey(args[i])) {
        try {
          Class<?> test = Class.forName(args[i]);
          tests.add(test);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(String.format("Unrecognized test `%s`", args[i]));
        }
      } else {
        tests.add(this.tests.get(args[i]));
      }
    }
    return this.run(this.getTestsToArray(tests), report);
  }

  private Result runCustomTests(String[] args, boolean report) {
    Result result = new Result();
    int i = 0;
    while (i < args.length) {
      if (!args[i].substring(0, 2).equals("--")) {
        throw new RuntimeException(String.format("Unrecognized test `%s`", args[i]));
      }
      String testName = args[i].substring(2);
      Class<?> test;
      if (this.tests.containsKey(testName)) {
        test = this.tests.get(testName);
      } else {
        try {
          test = Class.forName(testName);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(String.format("Unrecognized test `%s`", testName));
        }
      }
      int j = i + 1;
      while (j < args.length && !args[j].substring(0, 2).equals("--")) {
        j++;
      }
      if (j == i + 1) {
        result.add(this.run(test, false));
      } else {
        result.add(this.runTestsWithFilter(Arrays.copyOfRange(args, i + 1, j), test, false));
      }
      i = j;
    }
    if (report) {
      this.report(result);
    }
    return result;
  }

  private Result runHelp() {
    System.out.println(
        "TUG [all | [<tests>]+ | [#<testCase> | .<testGroup>]+ | [--<test> [#<testCase> |"
            + " \\.<testGroup>]*]+ | (-help | --help) | (-i | --interactive) | ((-f | --filename)"
            + " <filename>)]");
    return new Result();
  }

  private Result runInteractively(String[] args) {
    ArrayList<Class<?>> tests = new ArrayList<>();
    for (int i = 1; i < args.length; i++) {
      try {
        tests.add(Class.forName(args[i]));
      } catch (Exception e) {
        this.logFailure(e.getMessage());
      }
    }
    this.initTests(this.getTestsToArray(tests));
    try {
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
      Result result = new Result();
      while (true) {
        System.out.print(">> ");
        String input = stdin.readLine().trim();
        if (input.toLowerCase().equals("quit")) {
          break;
        }
        try {
          result.add(this.runArgs(input.split(" "), true));
        } catch (Exception e) {
          this.logFailure(e.getMessage());
        }
      }
      this.report(result);
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  private Result runTestConfiguration() {
    return this.runTestConfiguration(".grader.conf");
  }

  private Result runTestConfiguration(String filename) {
    try {
      String[] lines = Files.lines(Path.of(filename)).toArray(String[]::new);
      Result result = new Result();
      for (int i = 0; i < lines.length; i++) {
        result.add(this.runArgs(lines[i].trim().split(" "), false));
        if (result.wasSuccessful()) {
          this.logSuccess("COMPLETED STEP %d of %d\n", i + 1, lines.length);
        } else {
          this.logFailure("FAILED STEP %d of %d\n", i + 1, lines.length);
          break;
        }
      }
      this.report(result);
      return result;
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }
  }

  private Result runTestsWithFilter(String[] args, boolean report) {
    return this.runTestsWithFilter(args, this.getTests(), report);
  }

  private Result runTestsWithFilter(String[] args, Class<?> test, boolean report) {
    return this.runTestsWithFilter(args, new Class<?>[] {test}, report);
  }

  private Result runTestsWithFilter(String[] args, Class<?>[] tests, boolean report) {
    ArrayList<String> testCases = new ArrayList<>();
    ArrayList<String> testGroups = new ArrayList<>();
    ArrayList<String> ignoreCases = new ArrayList<>();
    ArrayList<String> ignoreGroups = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      if (args[i].isEmpty() || args[i].isBlank()) {
        throw new RuntimeException("Unrecognized test case/group because arg is blank");
      }
      if (args[i].charAt(0) == '#') {
        testCases.add(args[i].substring(1));
      } else if (args[i].charAt(0) == '.') {
        testGroups.add(args[i].substring(1));
      } else if (args[i].length() >= 2 && args[i].substring(0, 2).equals("!#")) {
        ignoreCases.add(args[i].substring(2));
      } else if (args[i].length() >= 2 && args[i].substring(0, 2).equals("!.")) {
        ignoreGroups.add(args[i].substring(2));
      } else {
        throw new RuntimeException(String.format("Unrecognized test case/group `%s`", args[i]));
      }
    }
    Result result = new Result();
    Arrays.stream(tests)
        .flatMap(
            (Class<?> test) -> {
              return Arrays.stream(test.getDeclaredMethods());
            })
        .filter(
            (Method testCase) -> {
              if (ignoreCases.contains(testCase.getName())) {
                return false;
              }
              if (testCases.contains(testCase.getName())) {
                return true;
              }
              if (!testCase.isAnnotationPresent(TestGroup.class)) {
                return testCases.isEmpty() && testGroups.isEmpty();
              }
              if (ignoreGroups.contains(testCase.getAnnotation(TestGroup.class).value())) {
                return false;
              }
              if (testGroups.contains(testCase.getAnnotation(TestGroup.class).value())) {
                return true;
              }
              return testCases.isEmpty() && testGroups.isEmpty();
            })
        .forEach(
            (Method testCase) -> {
              result.add(this.run(testCase, false));
            });
    if (report) {
      this.report(result);
    }
    return result;
  }

  private Result runVersion() {
    System.out.println(String.format("TUGrader v%s", TUGrader.VERSION));
    return new Result();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public static @interface Deps {
    String[] value();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public static @interface DisplayName {
    String value();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public static @interface TestGroup {
    String value();
  }

  public static class Eval {

    private int checks = 0;

    public int check() {
      this.checks++;
      return this.checks;
    }

    public void done() {
      this.done("Assessment was not successfully completed");
    }

    public void done(String reason) {
      if (this.checks <= 0) {
        throw new AssertionError(reason);
      }
      this.checks = 0;
    }

    public void expect(int checks) {
      this.expect(
          String.format("Expected %d checks but had %d checks", checks, this.checks), checks);
    }

    public void expect(String reason, int checks) {
      if (this.checks != checks) {
        throw new AssertionError(reason);
      }
    }

    public void success() {
      if (this.checks != 0) {
        throw new AssertionError("Assessment cannot be marked successful if it has other checks");
      }
      this.checks++;
    }
  }

  public static class Result {

    private int failureCount;
    private int runCount;
    private long runTime;

    public static Result from(org.junit.runner.Result result) {
      return new Result(result.getRunCount(), result.getFailureCount(), result.getRunTime());
    }

    public Result() {
      this(0, 0, 0);
    }

    public Result(int runCount, int failureCount, long runTime) {
      if (runCount < failureCount) {
        throw new RuntimeException(
            String.format("Result expected %d failures to be leq %d runs", failureCount, runCount));
      }
      this.runCount = runCount;
      this.failureCount = failureCount;
      this.runTime = runTime;
    }

    public void add(Result result) {
      this.runCount += result.runCount;
      this.failureCount += result.failureCount;
      this.runTime += result.runTime;
    }

    public int getFailureCount() {
      return this.failureCount;
    }

    public int getPassingCount() {
      return this.runCount - this.failureCount;
    }

    public int getRunCount() {
      return this.runCount;
    }

    public long getRunTime() {
      return this.runTime;
    }

    public boolean wasSuccessful() {
      return this.failureCount == 0;
    }
  }

  private class TUGraderListener extends RunListener {

    HashSet<String> failures = new HashSet<>();

    public void clear() {
      this.failures = new HashSet<>();
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
      this.failures.add(failure.getDescription().getMethodName());
      TUGrader.this.logWarning("WARNING: %s\n", failure.getTrimmedTrace());
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
      this.failures.add(failure.getDescription().getMethodName());
      TUGrader.this.logFailure("FAILED: %s\n", failure.getTrimmedTrace());
    }

    @Override
    public void testFinished(Description description) throws Exception {
      if (!this.failures.contains(description.getMethodName())) {
        TUGrader.this.logSuccess("PASSED\n");
      }
    }

    @Override
    public void testStarted(Description description) throws Exception {
      try {
        Class<?> test = Class.forName(description.getClassName());
        Method testCase = test.getDeclaredMethod(description.getMethodName());
        if (testCase.isAnnotationPresent(Deps.class)) {
          TUGrader.this.logInfo(
              "Test: %s %s\ndependencies: %s",
              test.getSimpleName(),
              TUGrader.this.getDisplayName(testCase),
              Arrays.toString(testCase.getAnnotation(Deps.class).value()));
        } else {
          TUGrader.this.logInfo(
              "Test: %s %s", test.getSimpleName(), TUGrader.this.getDisplayName(testCase));
        }
      } catch (Exception e) {
        TUGrader.this.logInfo(
            "TEST: %s %s", description.getDisplayName(), description.getMethodName());
      }
    }
  }
}
