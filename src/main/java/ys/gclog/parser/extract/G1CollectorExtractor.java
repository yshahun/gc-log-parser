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
package ys.gclog.parser.extract;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class G1CollectorExtractor implements LogExtractor {

  private static final Pattern PATTERN_PAUSE = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC pause (?:\\(.+?\\) )?\\((young|mixed)\\)(?: \\(.+?\\))?(?: %s)?, %s secs]"
          + "(?:(?:.|\\s)+\\[Eden: %s Survivors: %s Heap: %s]\\s+%s)?\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_MEMORY_FROM_TO_MB, REGEX_TIME,
          REGEX_MEMORY_WITH_TOTAL_FROM_TO, REGEX_MEMORY_WITHOUT_TOTAL_FROM_TO,
          REGEX_MEMORY_WITH_TOTAL_FROM_TO, REGEX_TIMES));

  private static final Pattern PATTERN_REMARK = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC remark.*, %s secs](?:\\s+%s)?\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_TIME, REGEX_TIMES));

  private static final Pattern PATTERN_CLEANUP = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC cleanup %s, %s secs](?:\\s+%s)?\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_MEMORY_FROM_TO_MB, REGEX_TIME, REGEX_TIMES));

  private static final Pattern PATTERN_CONCURRENT_PHASE = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC concurrent-.+]\\s*", REGEX_DATE, REGEX_TIME));

  private static final Pattern PATTERN_FULL_GC = Pattern.compile(
      String.format("(?:%s: )?%s: \\[Full GC(?: \\(.+?\\))?\\s+%s, %s secs](?:\\s+"
          + "\\[Eden: %s Survivors: %s Heap: %s](?:, \\[Metaspace: %s])?\\s+%s)?\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_MEMORY_FROM_TO_MB, REGEX_TIME,
          REGEX_MEMORY_WITH_TOTAL_FROM_TO, REGEX_MEMORY_WITHOUT_TOTAL_FROM_TO,
          REGEX_MEMORY_WITH_TOTAL_FROM_TO, REGEX_MEMORY_FROM_TO_KB, REGEX_TIMES));

  private static final Pattern PATTERN_ALL = Pattern.compile(String.format("%s|%s|%s|%s|%s",
      PATTERN_PAUSE, PATTERN_REMARK, PATTERN_CLEANUP, PATTERN_CONCURRENT_PHASE, PATTERN_FULL_GC));

  @Override
  public boolean accept(String log) {
    return PATTERN_ALL.matcher(log).matches();
  }

  @Override
  public Optional<Map<String, String>> extract(String log, ExtractContext context) {
    Map<String, String> values = new LinkedHashMap<>();

    if (!extractPauseEvent(log, values))
      if (!extractRemark(log, values))
        if (!extractCleanup(log, values))
          if (!extractFullCollection(log, values));

    return (values.isEmpty() && !skipConcurrentPhase(log)) ? Optional.empty() : Optional.of(values);
  }

  private static boolean extractPauseEvent(String log, Map<String, String> values) {
    Matcher matcher = PATTERN_PAUSE.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.EVENT.toString(), getEvent(matcher.group(3)));
    LogExtractor.putIfNotNull(values, LogField.MEM_BEFORE.toString(), matcher.group(4));
    LogExtractor.putIfNotNull(values, LogField.MEM_AFTER.toString(), matcher.group(5));
    LogExtractor.putIfNotNull(values, LogField.MEM_TOTAL.toString(), matcher.group(6));
    values.put(LogField.GC_TIME.toString(), matcher.group(7));

    LogExtractor.putIfNotNull(values, LogField.MEM_EDEN_BEFORE.toString(), matcher.group(8));
    LogExtractor.putIfNotNull(values, LogField.MEM_EDEN_AFTER.toString(), matcher.group(10));
    LogExtractor.putIfNotNull(values, LogField.MEM_EDEN_TOTAL.toString(), matcher.group(11));
    LogExtractor.putIfNotNull(values, LogField.MEM_SURVIVOR_BEFORE.toString(), matcher.group(12));
    LogExtractor.putIfNotNull(values, LogField.MEM_SURVIVOR_AFTER.toString(), matcher.group(13));
    LogExtractor.putIfNotNull(values, LogField.MEM_BEFORE.toString(), matcher.group(14));
    LogExtractor.putIfNotNull(values, LogField.MEM_AFTER.toString(), matcher.group(16));
    LogExtractor.putIfNotNull(values, LogField.MEM_TOTAL.toString(), matcher.group(17));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_USER.toString(), matcher.group(18));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_SYS.toString(), matcher.group(19));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_REAL.toString(), matcher.group(20));

    return true;
  }

  private static boolean extractRemark(String log, Map<String, String> values) {
    Matcher matcher = PATTERN_REMARK.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    values.put(LogField.EVENT.toString(), "O");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.GC_TIME.toString(), matcher.group(3));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_USER.toString(), matcher.group(4));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_SYS.toString(), matcher.group(5));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_REAL.toString(), matcher.group(6));

    return true;
  }

  private static boolean extractCleanup(String log, Map<String, String> values) {
    Matcher matcher = PATTERN_CLEANUP.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    values.put(LogField.EVENT.toString(), "O");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.MEM_BEFORE.toString(), matcher.group(3));
    values.put(LogField.MEM_AFTER.toString(), matcher.group(4));
    values.put(LogField.MEM_TOTAL.toString(), matcher.group(5));
    values.put(LogField.GC_TIME.toString(), matcher.group(6));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_USER.toString(), matcher.group(7));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_SYS.toString(), matcher.group(8));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_REAL.toString(), matcher.group(9));

    return true;
  }

  private static boolean extractFullCollection(String log, Map<String, String> values) {
    Matcher matcher = PATTERN_FULL_GC.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    values.put(LogField.EVENT.toString(), "F");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.MEM_BEFORE.toString(), matcher.group(3));
    values.put(LogField.MEM_AFTER.toString(), matcher.group(4));
    values.put(LogField.MEM_TOTAL.toString(), matcher.group(5));
    values.put(LogField.GC_TIME.toString(), matcher.group(6));

    LogExtractor.putIfNotNull(values, LogField.MEM_EDEN_BEFORE.toString(), matcher.group(7));
    LogExtractor.putIfNotNull(values, LogField.MEM_EDEN_AFTER.toString(), matcher.group(9));
    LogExtractor.putIfNotNull(values, LogField.MEM_EDEN_TOTAL.toString(), matcher.group(10));
    LogExtractor.putIfNotNull(values, LogField.MEM_SURVIVOR_BEFORE.toString(), matcher.group(11));
    LogExtractor.putIfNotNull(values, LogField.MEM_SURVIVOR_AFTER.toString(), matcher.group(12));
    LogExtractor.putIfNotNull(values, LogField.MEM_METASPACE_BEFORE.toString(), matcher.group(17));
    LogExtractor.putIfNotNull(values, LogField.MEM_METASPACE_AFTER.toString(), matcher.group(18));
    LogExtractor.putIfNotNull(values, LogField.MEM_METASPACE_TOTAL.toString(), matcher.group(19));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_USER.toString(), matcher.group(20));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_SYS.toString(), matcher.group(21));
    LogExtractor.putIfNotNull(values, LogField.GC_TIME_REAL.toString(), matcher.group(22));

    return true;
  }

  private static boolean skipConcurrentPhase(String log) {
    return PATTERN_CONCURRENT_PHASE.matcher(log).matches();
  }

  private static String getEvent(String token) {
    switch (token) {
      case "young":
        return "N";
      case "mixed":
        return "M";
      default:
        return "U";
    }
  }

  public static void main(String[] args) {
    System.out.println(PATTERN_FULL_GC);
  }
}
