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

import java.util.ArrayList;
import java.util.List;

public class ExtractContext {

  private final List<LogExtractor> extractors = new ArrayList<>();
  private final List<LogExtractor> availableExtractors;

  public ExtractContext(List<LogExtractor> availableExtractors) {
    this.availableExtractors = availableExtractors;
  }

  public List<LogExtractor> getExtractors() {
    return extractors;
  }

  public List<LogExtractor> getAvailableExtractors() {
    return availableExtractors;
  }
}
