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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ys.gclog.parser.extract.ExtractContext;
import ys.gclog.parser.extract.Extractors;
import ys.gclog.parser.extract.LogExtractor;

public class GCLogParser {

  private static final Logger logger = Logger.getLogger(GCLogParser.class.getName());

  private static final Pattern PATTERN_IDENT = Pattern.compile("^\\s+");

  public List<String> parse(InputStream input) throws IOException {
    return parse(input, false);
  }

  public List<String> parse(InputStream input, boolean isVerbose) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    List<LogExtractor> extractors = new ArrayList<LogExtractor>();
    Map<LogExtractor, ExtractContext> contexts = new HashMap<>();
    Set<String> fields = new LinkedHashSet<>();
    List<String> csvRows = new ArrayList<>();
    Deque<CsvDimension> dimensions = new ArrayDeque<>();

    List<String> buffer = new ArrayList<>();
    String line = readLogLine(reader, buffer);
    while (line != null) {
      Optional<Map<String, String>> fieldOption = extract(line, extractors, contexts);

      if (!fieldOption.isPresent()) {
        if (isVerbose) {
          System.out.println(String.format("Line can't be parsed: %s", line));
        }
      } else if (!fieldOption.get().isEmpty()) {
        fields.addAll(fieldOption.get().keySet());

        if (dimensions.isEmpty()) {
          dimensions.push(new CsvDimension(fields.size(), csvRows.size()));
        } else if (dimensions.peek().fieldCount < fields.size()) {
          dimensions.push(new CsvDimension(fields.size(), csvRows.size()));
        }

        String s = fields.stream()
            .map(f -> fieldOption.get().getOrDefault(f, ""))
            .collect(Collectors.joining(","));
        logger.fine(s);
        csvRows.add(s);
      }

      line = readLogLine(reader, buffer);
    }

    completeCsvMissingValues(csvRows, dimensions);

    // Add CSV header.
    String header = fields.stream().collect(Collectors.joining(","));
    csvRows.add(0, header);

    return csvRows;
  }

  private static String readLogLine(BufferedReader reader, List<String> buffer) throws IOException {
    if (buffer.isEmpty()) {
      String startLine = reader.readLine();
      if (startLine == null) {
        return null;
      }
      buffer.add(startLine);
    }

    String nextLine = reader.readLine();
    while (nextLine != null && PATTERN_IDENT.matcher(nextLine).find()) {
      buffer.add(nextLine);
      nextLine = reader.readLine();
    }

    String result = buffer.stream().collect(Collectors.joining("\n"));
    buffer.clear();
    if (nextLine != null) {
      buffer.add(nextLine);
    }
    return result;
  }

  private static Optional<Map<String, String>> extract(
      String line, List<LogExtractor> extractors, Map<LogExtractor, ExtractContext> contexts) {
    for (LogExtractor extractor : extractors) {
      Optional<Map<String, String>> option = extractor.extract(line, contexts.get(extractor));
      if (option.isPresent()) {
        return option;
      }
    }

    ExtractContext context = new ExtractContext(Extractors.secondaryExtractors());

    for (LogExtractor extractor : Extractors.primaryExtractors()) {
      Optional<Map<String, String>> option = extractor.extract(line, context);
      if (option.isPresent()) {
        logger.fine(() -> "Adding " + extractor);
        extractors.add(extractor);
        contexts.put(extractor, context);
        return option;
      }
    }

    return Optional.empty();
  }

  private static void completeCsvMissingValues(List<String> csvRows, Deque<CsvDimension> dimensions) {
    if (dimensions.isEmpty()) {
      return;
    }

    CsvDimension lastDimension = dimensions.poll();
    int maxFieldCount = lastDimension.fieldCount;
    int lastRowNum = lastDimension.rowNum;

    while (!dimensions.isEmpty()) {
      CsvDimension dimension = dimensions.poll();
      int delta = maxFieldCount - dimension.fieldCount;
      String postfix = Stream.generate(() -> ",").limit(delta).collect(Collectors.joining());
      for (int i = dimension.rowNum; i < lastRowNum; i++) {
        csvRows.set(i, csvRows.get(i) + postfix);
      }
      lastRowNum = dimension.rowNum;
    }
  }

  private static class CsvDimension {
    final int fieldCount;
    final int rowNum;

    public CsvDimension(int fieldCount, int rowNum) {
      this.fieldCount = fieldCount;
      this.rowNum = rowNum;
    }
  }
}
