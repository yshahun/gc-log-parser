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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Extractors {

  private static final List<LogExtractor> PRIMARY_EXTRACTORS =
      Collections.unmodifiableList(Arrays.asList(
          new SimpleMinorCollectionExtractor(),
          new SimpleFullCollectionExtractor(),
          new DetailedMinorCollectionExtractor(),
          new DetailedFullCollectionExtractor(),
          new CmsCollectorExtractor(),
          new G1CollectorExtractor()));

  private static final List<LogExtractor> SECONDARY_EXTRACTORS =
      Collections.unmodifiableList(Arrays.asList(
          new SerialYoungCollectorExtractor(),
          new ParallelYoungCollectorExtractor(),
          new ParallelNewYoungCollectorExtractor(),
          new SerialOldCollectorExtractor(),
          new ParallelOldCollectorExtractor(),
          new PermGenCollectorExtractor(),
          new MetaspaceCollectorExtractor()));

  private Extractors() {}

  public static List<LogExtractor> primaryExtractors() {
    return PRIMARY_EXTRACTORS;
  }

  public static List<LogExtractor> secondaryExtractors() {
    return SECONDARY_EXTRACTORS;
  }
}
