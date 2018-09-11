#include <stdio.h>
int main () {
float overhang= -1.0;
while (scanf ("%f\n", &overhang), overhang!=0.0)
{
int i= 1; float sum= 0.0;
do sum+= 1.0/++i;
while (sum< overhang);
printf ("%d card(s)\n", --i);
}
}
