#include <stdio.h>

int cmp (void *a, void *b)
{
	return ((short*)a)[0]- ((short*)b)[0];
}

int main ()
{
	int n, m;
	scanf ("%d %d\n", &n, &m);

	char *pool= (char*)malloc ((n+3)*m);

	int i,j,k;
	for (i=0; i<m; ++i)
	{
		//Read strings into pool
		char ch; char *ptr= pool+ i*(n+3)+ 2;
		while (ch= getchar (), ch!='\n' && ch!=EOF)
			*ptr++= ch;
		*ptr= 0;

		//Cal order
		short ord= 0;
		ptr= pool+ i*(n+3)+ 2;
		for (j= 0; j< n; ++j)
			for (k= j+1; k< n; ++k)
				if (ptr[j]> ptr[k]) 
					++ord;
		*(short*)(ptr-2)= ord;
	}
	qsort (pool, m, n+3, cmp);
	for (i= 0; i< m; ++i)
		printf ("%s\n", pool+ i*(n+3)+ 2);
	free (pool);
}
