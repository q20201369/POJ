#include <stdio.h>

int main()
{
int i;
float sum=0.0, rec;
for (i=0; i<12; ++i)
{
scanf ("%f\n", &rec);
sum+= rec;
}
printf ("$%.2f\n", sum/12.0);
}
