package io.github.fp7.jvmremote.deps;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class AddDepsToClasspath {

  public static void premain(String agentArgs, Instrumentation inst)
      throws IOException, InterruptedException {
    Path depsDir = Paths.get(agentArgs);
    System.out.println(String.format("Searching for deps.edn in %s", depsDir));

    if (!Files.isReadable(depsDir.resolve("deps.edn"))) {
      return;
    }

    String classpath = classpath(agentArgs);

    Path jarFile = writeClasspathJar(classpath);

    System.out.println(String.format("Adding %s with embedded classpath to classpath", jarFile));
    inst.appendToSystemClassLoaderSearch(new JarFile(jarFile.toFile()));
  }

  private static String classpath(String dir) throws InterruptedException, IOException {
    Process clojure = new ProcessBuilder("clojure", "-Spath").directory(new File(dir)).start();

    boolean b = clojure.waitFor(1, TimeUnit.MINUTES);
    if (!b) {
      throw new IllegalStateException(
          String.format("Classpath for %s could not be built in a minute", dir));
    }

    InputStream inputStream = clojure.getInputStream();

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    int length;
    byte[] bytes = new byte[4096];
    while ((length = inputStream.read(bytes)) != -1) {
      os.write(bytes, 0, length);
    }

    return Arrays.stream(os.toString(StandardCharsets.UTF_8.name()).split(":"))
        .map(
            p -> {
              String tmp = p;
              if (!p.startsWith("/")) {
                tmp = Paths.get(dir, p).toAbsolutePath().toString();
              }
              if (Files.isDirectory(Paths.get(tmp)) && !tmp.endsWith("/")) {
                tmp = tmp + "/";
              }
              return tmp;
            })
        .collect(Collectors.joining(" "));
  }

  private static Path writeClasspathJar(String foo) throws IOException {
    Manifest manifest = new Manifest();
    manifest.getMainAttributes().put(Name.MANIFEST_VERSION, "1.0");
    manifest.getMainAttributes().put(Name.CLASS_PATH, String.join(" ", foo.split(":")));

    Path deps = Files.createTempFile("deps", ".jar");

    JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(deps), manifest);
    jarOutputStream.close();

    return deps;
  }

  public static void main(String[] args) {
    Path src =
        Paths.get("src")
            .relativize(Paths.get("/home/finn/dev/projects/io.github.fp7/fp7/fp7-core/"))
            .normalize()
            .toAbsolutePath();

    System.out.println(src);
  }
}
