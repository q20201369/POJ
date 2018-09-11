#include <stdio.h>
int mapLetter (char L)
{return L<'Q'?(L-'A')/3+2:(L-'Q')/3+7;}
int comp (void *a, void *b)
{return *(int*)a-*(int*)b;}
char *fmtPhone (int nb)
{
static char buf[9];
int i;
for (i= 0; i<4; ++i)
{buf[7-i]= (nb & 0xF)+ '0';nb>>=4;}
buf[3]= '-';
for (i= 0; i<3; ++i)
{buf[2-i]= (nb &0xF) + '0';nb>>=4;}
buf[8]= '\0';
return buf;
}
int main()
{
int N;
scanf ("%d\n", &N);
int *buffer= (int *)malloc ((N+1)*sizeof(buffer));

int hasDup= 0;
int n= N;
while (n>0)
{
char ch; int phone= 0;
while (ch= getchar(), (ch != '\n') && (ch != EOF))
{
int dig= -1;
if (ch=='-')
continue;
else if (ch>='A' && ch<='Z')
dig= mapLetter (ch);
else
dig= ch- '0';
phone<<=4; phone|= dig;
}
buffer[n]= phone;
--n;
}
buffer[0]= 0xFFFFFFF;
qsort (buffer, N+1, sizeof(int), comp);
int i,dupNb= 1;
for (i=0; i< N; ++i)
{
if(buffer[i]==buffer[i+1])++dupNb;
else{
if(dupNb>1){printf("%s %d\n", fmtPhone (buffer[i]), dupNb);hasDup=1;}
dupNb=1;}
}
free(buffer);
if(hasDup==0)printf("No duplicates.\n");
}
