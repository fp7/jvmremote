package io.github.fp7.jvmremote.spring_boot;

import io.github.fp7.jvmremote.core.NreplStarter;
import java.lang.instrument.Instrumentation;
import java.util.List;
import net.bytebuddy.agent.builder.AgentBuilder.Default;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.matcher.ElementMatchers;

public class SpringBootNreplStarter {

  public static String args;

  public static void premain(String agentArgs, Instrumentation inst) {
    args = agentArgs;

    new Default()
        .type(ElementMatchers.named("org.springframework.boot.loader.Launcher"))
        .transform(
            (builder, typeDescription, classLoader, module) ->
                builder.visit(
                    Advice.to(ClassLoaderFetcher.class)
                        .on(
                            ElementMatchers.named("createClassLoader")
                                .and(ElementMatchers.takesArgument(0, List.class)))))
        .installOn(inst);
  }

  public static class ClassLoaderFetcher {
    @Advice.OnMethodExit
    public static void intercept(@Return ClassLoader inst) {
      NreplStarter.start(args, inst);
    }
  }
}
