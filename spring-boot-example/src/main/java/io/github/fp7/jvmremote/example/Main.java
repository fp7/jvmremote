// Copyright (c) 2020 Finn Petersen
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at https://www.eclipse.org/legal/epl-2.0/
//
// SPDX-License-Identifier: EPL-2.0

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
