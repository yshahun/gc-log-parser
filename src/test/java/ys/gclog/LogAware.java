package ys.gclog;

import java.io.InputStream;

public interface LogAware {

  static InputStream getLog(String name) {
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    if (stream == null) {
      throw new IllegalStateException(String.format("Resource %s is not found.", name));
    }
    return stream;
  }
}
