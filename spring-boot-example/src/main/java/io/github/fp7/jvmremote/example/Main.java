package io.github.fp7.jvmremote.example;

public class Main {

  public Main() {
    System.out.println(String.format("main const %s", this.getClass().getClassLoader()));
    System.out.println(this.getClass());
    System.out.println("real const");
  }

  public String foo() {
    return "horsti";
  }

  public static void main(String[] args) {
    Main main = new Main();
    System.out.println("main");
  }
}
