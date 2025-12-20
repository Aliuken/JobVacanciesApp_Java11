# Spring Boot log expressions

## 1. Regular expression:
```txt
^(?<timestamp>[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\.[0-9]{3})  ?(?<level>[^ ]+) (?<processId>\d+) --- \[ *(?<thread>[^ ]+)\] (?<class>[^ ]+) *: (?<submessage>.*)
```

## 2. Grok expression:
```txt
(?<timestamp>%{YEAR}-%{MONTHNUM}-%{MONTHDAY} %{TIME})  ?%{LOGLEVEL:level} %{NUMBER:processId} --- \[ *%{DATA:thread}\] %{DATA:class} *: %{GREEDYDATA:submessage}
```