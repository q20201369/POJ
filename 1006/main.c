#include <stdio.h>

void dioph(int a, int b, int c, int* x, int* y)
{
	if(b==0)
	{
		*x= c/a;
		*y= 0;
		return;
	}

	if(abs(a)<abs(b))
	{
		dioph(b,a,c,x,y);
		return;
	}

	int d= a%b;
	int xx= 0, zz= 0;

	dioph(b,d,c,&zz,&xx);
	*x= xx;
	*y= (c-a**x)/b;
}

int biorhythms(int p, int e, int i, int d)
{
	int kp, ke, ki, kpe;

	if(p==-1 && e==-1 && i==-1 && d==-1)
		return -1; // invalid input

	dioph(28,-23,p-e,&ke,&kp);
	int c_pe= (23*kp+p)%(28*23); if(c_pe<0) c_pe+= 28*23;

	dioph(28*23,-33,i-c_pe,&kpe,&ki);
	int c_pei= (33*ki+i)%(28*23*33); if(c_pei<0) c_pei+= 28*23*33;

	int ret= c_pei-d;
	while(ret<=0)
		ret+= 28*23*33;
	return ret;
}

int main()
{
	int p,e,i,d;
	int peak;
	int case_no= 1;

	while(1)
	{
		scanf("%d%d%d%d",&p,&e,&i,&d);
		peak= biorhythms(p,e,i,d);
		if(peak==-1)
			break;
		printf("Case %d: the next triple peak occurs in %d days.\n",case_no,peak);
		++case_no;
	}
	return 0;
}
