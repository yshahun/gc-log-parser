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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailedFullCollectionExtractor implements LogExtractor {

  private static final Logger logger =
      Logger.getLogger(DetailedFullCollectionExtractor.class.getName());

  private static final Pattern PATTERN = Pattern.compile(
      String.format("(?:%s: )?%s: \\[(?:Full )?GC(?: \\(.+?\\) )?(?:%s: )?(?:%s:)?\\s*\\[.+?](?:%s: )?(?:%s:)? \\[.+?] %s,? \\[.+?], %s secs] %s\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_DATE, REGEX_TIME, REGEX_DATE, REGEX_TIME,
          REGEX_MEMORY_FROM_TO_KB, REGEX_TIME, REGEX_TIMES));

  @Override
  public boolean accept(String log) {
    return PATTERN.matcher(log).matches();
  }

  @Override
  public Optional<Map<String, String>> extract(String log, ExtractContext context) {
    Matcher matcher = PATTERN.matcher(log);
    if (!matcher.matches()) {
      return Optional.empty();
    }

    Map<String, String> values = new LinkedHashMap<>();
    values.put(LogField.EVENT.toString(), "F");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.MEM_BEFORE.toString(), matcher.group(7));
    values.put(LogField.MEM_AFTER.toString(), matcher.group(8));
    values.put(LogField.MEM_TOTAL.toString(), matcher.group(9));
    values.put(LogField.GC_TIME.toString(), matcher.group(10));
    values.put(LogField.GC_TIME_USER.toString(), matcher.group(11));
    values.put(LogField.GC_TIME_SYS.toString(), matcher.group(12));
    values.put(LogField.GC_TIME_REAL.toString(), matcher.group(13));

    subExtract(log, context, values);

    return Optional.of(values);
  }

  private static void subExtract(String line, ExtractContext context, Map<String, String> values) {
    if (!context.getExtractors().isEmpty()) {
      for (LogExtractor extractor : context.getExtractors()) {
        Optional<Map<String, String>> option = extractor.extract(line, context);
        if (option.isPresent()) {
          values.putAll(option.get());
        }
      }
      return;
    }

    for (LogExtractor extractor : context.getAvailableExtractors()) {
      Optional<Map<String, String>> option = extractor.extract(line, context);
      if (option.isPresent()) {
        logger.fine(() -> "Adding " + extractor);
        context.getExtractors().add(extractor);
        values.putAll(option.get());
      }
    }
  }

  public static void main(String[] args) {
    System.out.println(PATTERN);
  }
}
