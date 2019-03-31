# Eurovision Song Contest Voting System

This application counts the votes for a country by other countries in the Eurovision Song Contest.

## Input file format

```
{"country":"Netherlands","votedFor":"Belgium"}
{"country":"Italy","votedFor":"Germany"}
{"country":"Netherlands","votedFor":"Germany"}
```

## Supported commands

```
gradle run --args="load file year"

gradle run --args="results country year"

```

## Assumption

The following assumptions are made while creating the application:

* The input file is typically large. Hence it is not possible to put all the data from the file into the memory.
* The country name is case sensitive.
* If the vote count for different countries is the same, then the points are randomly assigned. 
* If the vote list contains less than 10 elements, then the best points are assigned to the countries in the list.


## Approach

The application supports two tasks:
* Load - The load task gathers an input file and creates a hard link under the file name 'year'. By creating just the link to an existing file (input file), the time taken to load can be minimized.

* Results -  The results task is responsible for three main tasks:
  * Count votes - In the count vote subtask, the input file is read line by line from a buffer and filtered the country that we are interested in. The count for each country which voted is updated in a hash map.
  * Sort votes - The hash map is then sorted in descending order and the top 10 countries are selected and put into a list in ascending order.
  * Show results - This subtask checks the size of the list and, based on it, prints the points along with the country name in the requested format.

## Performance

In MacBook Pro with 2.8 GHz Intel Core i7 processor and 16 GB 1600 MHz DDR3 memory, the time taken by various tasks is shown below:

Tasks         | 10 million lines | 100 million lines | 1 billion lines
------------- | -------------    | -------------     | -------------
Load          | 21.29 ms         | 21.19 ms          | 21.06 ms
Count votes   | 1.67 s           | 15 s              | 150 s
Sort votes    | 7.7 ms           | 10.4 ms           | 18.2 ms
Show results  | 0.079 ms         | 0.061 ms          | 0.072 ms


The load task takes approximately 21 milliseconds for all files. The reason is that it takes the same time to create a link to different file types. In the results task, the count vote subtask is the most time consuming with 150 seconds for 1 billion lines. The count vote has two main tasks, 1) read the file and 2) update the hashmap. Since updating the hashmap is very cheap (O(1) for no collisions) the bottleneck is on reading the file. For the sorting task, although sorting is expensive, the number of countries is limited, therefore the time consumed for sorting is very low (18.2 milliseconds for 1 billion input).

## Possible improvement
Since reading a line from a buffer is a synchronized task, parallelizing it won't improve the time. Also, since the input file is huge, there is a possibility for OutOfMemoryError if we try to put all data into the memory. Hence, a possible way is to put large chunks of a file in the memory (e.g., using MappedByteBuffer) and read the contents in parallel. The downside is that multithreading makes the code more complex. After reading a line from the file, a concurrent hashmap can be used to update the vote count.






