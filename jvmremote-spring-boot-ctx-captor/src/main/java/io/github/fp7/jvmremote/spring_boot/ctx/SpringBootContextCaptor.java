package io.github.fp7.jvmremote.spring_boot.ctx;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.CompletableFuture;

import net.bytebuddy.agent.builder.AgentBuilder.Default;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.Return;
import net.bytebuddy.matcher.ElementMatchers;

public class SpringBootContextCaptor {

  public static final CompletableFuture<Object> ctx = new CompletableFuture<>();

  public static void premain(String agentArgs, Instrumentation inst) {
    new Default()
        .type(ElementMatchers.named("org.springframework.boot.SpringApplication"))
        .transform(
            (builder, typeDescription, classLoader, module) ->
                builder.visit(
                    Advice.to(ClassLoaderFetcher.class)
                        .on(
                            ElementMatchers.named("run")
                                .and(ElementMatchers.not(ElementMatchers.isStatic())))))
        .installOn(inst);
  }

  public static class ClassLoaderFetcher {
    @Advice.OnMethodExit
    public static void intercept(@Return Object inst) {
      System.out.println(String.format("Captured context: %s", inst));
      ctx.complete(inst);
    }
  }
}
