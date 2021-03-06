# Overview of the Example Project
## File Layout

```
|-- lib
|   |-- commons-lang-2.3.jar
|   |-- junit-4.4.jar
|   +-- mockito-all-1.3.jar
|
|-- module_a
|   |-- src
|   |   +-- felix
|   |       +-- A.java
|   |-- src2
|   |   +-- felix
|   |       +-- B.java
|   +-- test
|       +-- felix
|           |-- ATest.java
|           +-- BTest.java
|
+-- module_b
    |-- src
    |   +-- felix
    |       +-- C.java
    +-- test
        +-- felix
            +-- CTest.java

```

## The Build Process
This would be built along these lines:

![](../example/docs/overview_scaled.png)

Looking closer reveals sleeping concepts:

![](../example/docs/overview_module_hinted.jpg)

## Separation of Concerns

![](../example/docs/build.png)

![](../example/docs/modul.png)
