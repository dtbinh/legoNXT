/**
 * 
 * This is a doctor to imitate the scout (robot) to  search for victims.
 * There are two method to check:
 * 		1) Location: if victim has been added before.
 * 		2) Priority: which number of the victim color
 * 
 * At the beginning, the belief is to set emply of list and there is 
 * no victims for three kinds. 
 * Then, get information (pair of color and location) from each victim.  
 * Depending on the levels of victims, the doctor will sort them as a rule:
 * 		1) Red (9) is very urgent
 * 		2) Blue (8) is urgent
 * 		3) Green (7) is not urgent
 *      4) Same colors depend on "First come, First rescue"
 * When there is no victims, which means the scout has finished the searching,
 * the doctor will stop also. So, in the belief, there will be the number of
 * different victims (colors) and the sorted list.
 * 
 * @author Robot 13 (Yiming Li, Jhand Jaspal and Thomas, James)
 * @version 2.0
 * 
 */

/* Initial beliefs and rules */

// emply list for storing victims
list([]).

// the number of different victims is set to 0
red(0).
blue(0).
green(0).

/* Initial goals */

!start.

/* Plans */

// once the doctor starts, it will call the method victim to imitate
// connecting with robot scout to get next pair of color and location,
// then, to evaluate the different colors by goal checkPriority.
// because there is empty list, we do not check if location is the same
+!start 
<- robot.send("Start");
   robot.receiver(Priority, Location);
   !checkPriority(Priority, Location).
                  

// next time to receive the color number and location, in this case, it
// need to check if the location has been detected before and if not add
// continue to chek priority
+!next
<- robot.receiver(Priority, Location);
   ?list(L);
   !checkLocation(Priority, Location, L).
   

// if there is empty, it means we have complemented the checking,
// there is no the same location in the list, we can check the priority now
+!checkLocation(Priority, Location, [])
<- !checkPriority(Priority, Location).
 

// if there is the same location, it means we do not add again and only
// print sentence and wait for next victim
+!checkLocation(Priority, Location, [pair(Pri, Loc)|T]) :
Location == Loc
<- .print("**********************************************");
   .print("The same victim has been added before.");
   .print("**********************************************");
   !next.


// if the heading of pair is not the same location, we can check next
// one, and then until T is empty or there is the same location
+!checkLocation(Priority, Location, [pair(Pri, Loc)|T]) :
Location \== Loc
<- !checkLocation(Priority, Location, T).


// if priority is "99" sent by scout, it means that map has been finished
// then, only print sorted list again and stop.
+!checkPriority(Priority, Location) :
Priority == "99"
<- .print("**********************************************************************");
   .print("Searching for victims is completed and there is no more victims.");
   .print("**********************************************************************");
   !printList.


// if priority is "9", it means that the scout has detected the victim (Red one),
// we should add it to the end of other red colors (first come, first rescue) and 
// increase the number of red colors by 1. Depending on three levels of colors,
// adding position is "R" and print list. After it, restart to find next scout. 
+!checkPriority(Priority, Location) :
Priority == "9"
<- ?red(R);
   -+red(R+1);
   ?list(L);
   !addList(Priority, Location, R, L);
   !printList;
   !next.
   

// if priority is "8", it means that the scout has detected the victim (Blue one),
// we should add it to the end of other blue colors (first come, first rescue) and 
// increase the number of blue colors by 1.Depending on three levels of colors,
// adding position is "R+B" and print list. After it, restart to find next scout.
+!checkPriority(Priority, Location) :
Priority == "8"
<- ?red(R);
   ?blue(B);
   -+blue(B+1);
   ?list(L);
   !addList(Priority, Location, R+B, L);
   !printList;
   !next.


// if priority is "7", it means that the scout has detected the victim (Green one),
// we should add it to the end of other green colors (first come, first rescue) and 
// increase the number of green colors by 1. Depending on three levels of colors,
// adding position is "R+B+G" and print list. After it, restart to find next scout.
+!checkPriority(Priority, Location) :
Priority == "7"
<- ?red(R);
   ?blue(B);
   ?green(G);
   -+green(G+1);
   ?list(L);
   !addList(Priority, Location, R+B+G, L);
   !printList;
   !next.


// when N is equal to 0, it means that we should add at the heading of list
// replace original one (remove and add).
+!addList(Priority, Location, N, L) :
N == 0
<- -+list([pair(Priority, Location)|L]).


// Recursive Function:
// when N is larger than 0, it means that we should add at the heading of list
// at the position of "N-1" using recursive functions until N==0, after it,
// add the other pairs before this position.
// We can also think it is like a "stack", pop all the things before special position,
// then, add the new pair, next to push those things that we have pop.
+!addList(Priority, Location, N, [pair(Pri, Loc)|T]) :
N > 0
<- !addList(Priority, Location, N-1, T);
   ?list(L);
   -+list([pair(Pri, Loc)|L]).
   

// print the list as a form
// it also call the sub method of printList, which has parameters
// print from the first one
+!printList
<- ?list(L);
   ?red(R);
   ?blue(B);
   ?green(G);
   .print("===========================================");
   .print("Priprity: (1)Very urgent  (2)Urgent  (3)Not urgent");
   .print("In each priprity: First come, First rescue.");
   .print("---------------------------------------------------------------------------");
   !printList(L, 1);
   .print("Total[",R+B+G,"]: (1)Very urgent[",R,"] (2)Urgent[",B,"]  (3)Not urgent[",G,"]");
   .print("===========================================\n\n").
   

// if list is empty, do nothing.
+!printList([], N).


// if the priority of heading of list is "9", it means that this victim is 
// very urgent one (Red) and print the corresponding sentence and call next one
+!printList([pair(Priority, Location)|T], N) :
Priority == "9"
<- .print("[",N,"] Very urgent victim (red):      ", Location);
   !printList(T, N+1).


// if the priority of heading of list is "8", it means that this victim is 
// urgent one (Blue) and print the corresponding sentence and call next one
+!printList([pair(Priority, Location)|T], N) :
Priority == "8"
<- .print("[",N,"] Urgent victim (blue):            ", Location);
   !printList(T, N+1).
   

// if the priority of heading of list is "7", it means that this victim is 
// urgent one (Green) and print the corresponding sentence and call next one
+!printList([pair(Priority, Location)|T], N) :
Priority == "7"
<- .print("[",N,"] Not urgent victim (green):   ", Location);
   !printList(T, N+1).
 
