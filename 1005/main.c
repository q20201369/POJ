#include <stdio.h>
int main(){int N,i;float X,Y;scanf ("%d\n", &N);for (i=1; i<= N; ++i){scanf ("%f %f\n", &X, &Y);printf ("Property %d: This property will begin eroding in year %d.\n", i, 1+(int)((X*X+Y*Y)*3.14159/100));}puts ("END OF OUTPUT.\n");}
