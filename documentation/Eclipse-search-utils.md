# Eclipse search utils

## 1. Replace "&nbsp;&nbsp;&nbsp;&nbsp;" by "\t" in java files

```txt
Search -> File Search ->
  Containing text: "    "
  Case sensitive
  Regular expression
  File name patterns: *.java, *.xml
  Scope: Workspace

Search
Replace... -> With: "\t"
```

## 2. Replace "\t" by "&nbsp;&nbsp;" in html and js files

```txt
Search -> File Search ->
  Containing text: "\t"
  Case sensitive
  Regular expression
  File name patterns: *.html, *.js, *.yaml
  Scope: Workspace

Search
Replace... -> With: "  "
```

## 3. Remove trailing spaces

```txt
Search -> File Search ->
  Containing text: "[ \t]+(\r|\n|$)"
  Case sensitive
  Regular expression
  File name patterns: *
  Scope: Workspace

Search
Replace... -> With: "$1"
```