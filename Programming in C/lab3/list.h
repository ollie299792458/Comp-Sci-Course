#ifndef LIST_H
#define LIST_H 
 
struct List { 
  int head;
  struct List *tail;
};

typedef struct List List;

/* Functions you get */

List *cons(int head, List *tail);
List *mergesort(List *list);

/* Basic exercises */

int sum(List *list);

void iterate(int (*f)(int), List *list);

void print_list(List *list);

/* Challenge exercises */

List *merge(List *list1, List *list2);

void split(List *list, List **list1, List **list2);

#endif
