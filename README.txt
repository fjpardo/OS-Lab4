# OS-Lab4
NYU Operating Systems Lab 4 - Demand Paging

To run the code,

If you are running the code on terminal, or on crackle, follow these steps:

1. Once you have my code uploaded to the crackle server, or have reached the designated directory where my program is you can begin.

2. type in "javac Pager.java" then press enter


3. then type in "java Pager <MachineSize> <PageSize> <SizeOfEachProcess> <JobMix> <NumOfRef> <ReplacementAlgo>" then press enter.

	For example: java Pager 20 10 10 2 10 random      (for input 5)


4. See your results!


An Example of my output (input 5):


[fjp265@crackle1 Lab4]$ java Pager 20 10 10 2 10 random
The machine size is 20
The page size is 10
The process size is 10
The job mix number is 2
The number of references per process is 10

The replacement algorithm is random.


Process 1 had 2 faults and 15.5 average residency.
Process 2 had 4 faults and 5.0 average residency.
Process 3 had 4 faults and 2.5 average residency.
Process 4 had 4 faults and 5.666666666666667 average residency.

The total number of faults is 14 and the overall average residency is 6.083333333333333.
[fjp265@crackle1 Lab4]$ 


Thank you so much!
Francisco Pardo 
