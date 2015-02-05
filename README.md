## Summary
This Java library provides a parser for the GC logs produced by Oracle JDK 1.7 and 1.8. It includes a command line utility to generate CSV files from the logs. Your can load them in your favorite tool for analysis then. See the following inspiring article as an example - [Using R to analyze Java G1 garbage collector log files](https://blogs.oracle.com/taylor22/entry/using_r_to_analyze_g1gc).

## Requirements
The library requires Java 1.8.

## Documentation
The library is able to parse GC logs produced by using the following major GC logging options:
* -XX:+PrintGC (or -verbose:gc)
* -XX:+PrintGCDetails
* -XX:+PrintGCTimeStamps
* -XX:+PrintGCDateStamps

The following Oracle JVM's garbage collectors are supported:

<table>
<thead>
<tr>
  <td><b>Generation</b></td>
  <td><b>Collector<b></td>
  <td><b>JVM Option</b></td>
</tr>
</thead>
<tbody>
  <tr><td rowspan="4">Young</td><td>Copy</td><td>-XX:+UseSerialGC</td></tr>
  <tr><td>PS Scavenge</td><td>-XX:+UseParallelGC</td></tr>
  <tr><td>ParNew</td><td>-XX:+UseParNewGC</td></tr>
  <tr><td>G1 Young</td><td>-XX:+UseG1GC</td></tr>
  <tr><td rowspan="4">Old</td><td>MarkSweepCompact</td><td>-XX:+UseSerialGC</td></tr>
  <tr><td>PS MarkSweep</td><td>-XX:+UseParallelOldGC</td></tr>
  <tr><td>ConcurrentMarkSweep</td><td>-XX:+UseConcMarkSweepGC</td></tr>
  <tr><td>G1 Mixed</td><td>-XX:+UseG1GC</td></tr>
</tbody>
</table>

## Installation
Run Maven command to build a JAR file:

`mvn clean install`

## License
Licensed under the Apache License 2.0.
