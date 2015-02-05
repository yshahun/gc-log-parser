/*
 * Copyright 2015 Yauheni Shahun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ys.gclog.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ParserApp {

  private static final Pattern PATTERN_FILE_NAME = Pattern.compile("[.][^.]*$");

  private static class Options {
    final List<String> files;
    final boolean isVerbose;

    public Options(List<String> files, boolean isVerbose) {
      this.files = files;
      this.isVerbose = isVerbose;
    }
  }

  public static void main(String[] args) {
    Options options = parseOptions(args);
    if (options == null) {
      printUsage();
      return;
    }

    GCLogParser parser = new GCLogParser();

    for (String file : options.files) {
      String outputFile = getOutputFileName(file);
      if (Files.exists(Paths.get(outputFile))) {
        System.out.println(String.format("Warning: output file %s already exists", outputFile));
        continue;
      }

      try (InputStream input = new FileInputStream(file);
          PrintWriter writer = new PrintWriter(outputFile)) {
        List<String> csvData = parser.parse(input, options.isVerbose);

        for (String line : csvData) {
          writer.println(line);
        }
        System.out.println(String.format("%d lines written to %s", csvData.size(), outputFile));
      } catch (IOException e) {
        System.out.println(
            String.format("Error: can't parse the file %s due to: %s", file, e.getMessage()));
      }
    }

    System.out.println("Done");
  }

  private static Options parseOptions(String[] args) {
    List<String> files = new ArrayList<>();
    boolean isVerbose = false;

    for (String arg : args) {
      if (arg.startsWith("-")) {
        if ("-v".equals(arg)) {
          isVerbose = true;
        } else {
          System.out.println(String.format("Unknown option: %s", arg));
        }
      } else {
        if (Files.exists(Paths.get(arg))) {
          files.add(arg);
        } else {
          System.out.println(String.format("File doesn't exist: %s", arg));
        }
      }
    }

    if (files.isEmpty()) {
      return null;
    }

    return new Options(files, isVerbose);
  }

  private static void printUsage() {
    System.out.println(String.format(
        "Usage (command line args): [option] file [file]...", ParserApp.class.getName()));
    System.out.println("Options:");
    System.out.println("  -v\t verbose output");
  }

  private static String getOutputFileName(String file) {
    return PATTERN_FILE_NAME.matcher(file).replaceFirst("") + ".csv";
  }
}
