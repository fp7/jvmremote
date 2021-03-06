// Copyright (c) 2020 Finn Petersen
//
// This program and the accompanying materials are made
// available under the terms of the Eclipse Public License 2.0
// which is available at https://www.eclipse.org/legal/epl-2.0/
//
// SPDX-License-Identifier: EPL-2.0

package io.github.fp7.jvmremote.core;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.PersistentVector;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;

public class NreplStarter {

  public static void start(String agentArgs, ClassLoader classLoader) {
    new Thread(
            () -> {
              Thread.currentThread().setContextClassLoader(classLoader);
              System.out.println(String.format("Using args: '%s'", agentArgs));

              IFn require = Clojure.var("clojure.core", "require");
              require.invoke(Clojure.read("nrepl.cmdline"));

              IFn nreplMain = Clojure.var("nrepl.cmdline", "-main");

              nreplMain.applyTo(PersistentVector.create(Arrays.asList(agentArgs.split(","))).seq());
            },
            "nrepl-agent")
        .start();
  }

  public static void premain(String agentArgs, Instrumentation inst) {
    start(agentArgs, ClassLoader.getSystemClassLoader());
  }
}
