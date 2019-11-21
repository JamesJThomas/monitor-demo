This project demonstrates one approach to implement monitors in java. The monitor is then used to solve the following simple synchronization problem. There are three threads: t0, t1, and t2. The threads are required to take turns executing a method such that the following output is displayed:
I am thread number 0
I am thread number 1.
I am thread number 2.
...
To run this program:
1. Compile with javac TakeTurns.java.
2. Run with java TakeTurns.
