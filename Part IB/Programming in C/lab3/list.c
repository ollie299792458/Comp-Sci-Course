#include <stdio.h>
#include <stdlib.h>
#include "list.h"

List *cons(int head, List *tail) {
  /* malloc() will be explained in the next lecture! */
  List *cell = malloc(sizeof(List));
  cell->head = head;
  cell->tail = tail;
  return cell;
}

/* Functions for you to implement */

int sum(List *list) {
  if (list) {
    return list->head + sum(list->tail);
  } else {
    return 0;
  }
}

void iterate(int (*f)(int), List *list) {
  if (list) {
    list->head = (*f)(list->head);
    iterate((*f), list->tail);
  }
}

void print_list(List *list) {
  if (list) {
    if (list->tail) {
      printf("%d,", list->head);
      print_list(list->tail);
    } else {
      printf("%d\n", list->head);
    }
  }
}

/**** CHALLENGE PROBLEMS ****/

//Doesn't work
List *merge(List *list1, List *list2) {
  if (list1&&list2) {
    int list1true = (list1->head) < (list2->head);
    List *temp = list1;
    list1 = list1true ? list1 : list2;
    list2 = list1true ? list2 : temp;

    list1->tail = merge(list1->tail, list2);
    return list1;
  } else {
    return list1 ? list1 : list2;
  }
}

//Doesn't work
void split(List *list, List **list1, List **list2) {
  if (list) {
    (*list1) = list;
    split(list->tail, list2, &((*list1)->tail));
  }
}

/* You get the mergesort implementation for free. But it won't
   work unless you implement merge() and split() first! */

List *mergesort(List *list) {
  if (list == NULL || list->tail == NULL) {
    return list;
  } else {
    List *list1;
    List *list2;
    split(list, &list1, &list2);
    list1 = mergesort(list1);
    list2 = mergesort(list2);
    return merge(list1, list2);
  }
}
