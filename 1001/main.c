#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <string.h>

typedef struct
{
        char    caBase[10];
        int     iexp;
        char    caLeftValue[10];
        int     iLeftRightZeros;  
        char    caRightValue[10];
        int     iRightLeftZeros;  
        int     iDecimalDigit;  
        char    caResult[200];
}ST_Exp;

#define STRUCT_SIZE 100
#define true 1
#define false 0

int HandeRealNumNew ( ST_Exp * pstExp);
int checkInput (char * pcaString, int n, ST_Exp * pstExp);
void StringMultifply(char *pstA, char *pstB, char * pstResult) ;
void ExpCalculateNew ( ST_Exp  * pstExp);
void PrintPst ( ST_Exp  *pstExp);


int HandeRealNumNew ( ST_Exp * pstExp)
{

        char caTemp[100]={'\0'}  ;
        char *ptr= strchr(pstExp->caBase,'.' );
        int i,iDotPos = 0;
        int iLeftValue = 0;
        if (NULL != ptr)
        {
                int iDotPos = ptr - pstExp->caBase;
                memcpy(caTemp, pstExp->caBase,iDotPos  );
        }else
        {
                sprintf(caTemp, "%s", pstExp->caBase);
        }



        // 去掉左值前面的0.比如010 = 10
        iLeftValue = atoi(caTemp);
        memset(caTemp, 0, sizeof(caTemp));
        sprintf(pstExp->caLeftValue, "%d", iLeftValue);

        if(NULL != ptr)
        {
                memset(caTemp, 0, sizeof(caTemp));
                sprintf(caTemp, "%s", ptr+1 );

                //去掉右值 右边的0.比如。0.10 = 0.1
                for( i = strlen(caTemp); i>0; i--)
                {
                        if( '0' == caTemp[i -1])
                                caTemp[i-1]= '\0'; 
                        else
                                break;
                }
                sprintf(pstExp->caRightValue, "%s", caTemp);
        }
        pstExp->iDecimalDigit = strlen(pstExp->caRightValue);

        return true;
}

int checkInput (char * pcaString, int n, ST_Exp * pstExp)
{

        int i,idot = 0;
        int iDotPos = 0;
        char *ptr= NULL;
        int iValue = 0;

/*
        if(n < 0 || n > 25) 
        {
                printf( "n is an integer such that 0 < n <= 25 \n");
                return false;
        }
        */
        /*
        if(strlen(pcaString) >8)
        {
                printf( "the input invalid,R is a real number  length is less than 6 (%s) \n", pcaString);
                return false;
        }
        */
        for (i = 0; i<strlen(pcaString); i++)
        {

                if (*(pcaString + i) > '9'|| *(pcaString +i) < '0' )
                {
                        if ('.' == *(pcaString + i) )
                        {
                                idot ++;  
                                if(idot > 1)
                                {

                                        printf( "the dot less than 1  \n");
                                        return false;
                                }
                        }else
                        {
                                printf( "R is should be numer %c \n", *(pcaString+i));
                                return false;
                        }
                }

        }

        iValue = atoi(pcaString);
        /*
        if (iValue >= 100)
        {
                printf( "the real number is too big  \n");
                return false;
        }
        */

        sprintf(pstExp->caBase,"%s", pcaString);
        pstExp->iexp = n; 
        HandeRealNumNew(pstExp);

//      if(0==atoi(pstExp->caLeftValue) && 0== atoi(pstExp->caRightValue))
//      {
//              printf( "R can not equal 0 \n");
//              return false;
//      }

        return true;
}
void StringMultifply(char *pstA, char *pstB, char * pstResult) 
{


        int iALen = strlen(pstA);
        int iBLen = strlen(pstB);
        char caResult[200] = {'\0'};
        int usable_sizeof_caResult = sizeof(caResult) - 1;


        int iMulti = 0;
        int iRemain = 0;
        int iCarry = 0;
        int iResultTemp = 0;
        int iResult = 0;
        int iPos = 0;
        int iCarryMul = 0;

        int i ,j = 0;
        int isizeRe = usable_sizeof_caResult - 1;
        for(i=iALen-1; i>=0; i--)
        {
                for(j=iBLen-1; j>=0; j--)
                {

                        iMulti = (pstA[i] - '0') * (pstB[j] -'0');
                        iRemain = iMulti % 10;


                        iPos = isizeRe - (iALen -1 - i) - (iBLen -1 - j);
                        if ('\0'!=  caResult[iPos]  )
                                iResultTemp = caResult[iPos] - '0';
                        else
                                iResultTemp = 0; 

                        iResult = (iRemain + iCarry + iResultTemp) % 10 ;
                        iCarry = iMulti/10 + (iRemain + iCarry + iResultTemp )/10;

                        caResult[iPos] = iResult + '0';

                }
                if(iCarry > 0)
                        caResult[--iPos] = iCarry + '0';
                iCarry = 0;
        // printf("pstA: %s, pstB: %s, caResult: %s\n", pstA, pstB, caResult + iPos); // XXX



        }

        // printf("pstA: %s, pstB: %s, caResult: %s\n", pstA, pstB, caResult + iPos); // XXX
        for( i= 0; i<usable_sizeof_caResult; i++ )
        {
                if(caResult[i] >= '0')
                        break;
                // printf("%c", caResult[i]);
        }

        // memcpy(pstResult, caResult + i, sizeof(caResult) - i );
        // memcpy(pstResult, caResult + i, strlen(caResult + i) + 1 );
        memcpy(pstResult, caResult + i, usable_sizeof_caResult - i + 1);
        // pstResult[sizeof(caResult) - i + 1] = '\0';

}
void ExpCalculateNew ( ST_Exp  * pstExp)
{

        char caRealNumber[200] = {'\0'};
        char caResultValue[200] = {'\0'};
        char caTempValue[200] = {'\0'};
        int i = 0;
        int iResultLen = 0;
        int iExpDecimalNum = 0;

        if(1==pstExp->iexp)
        {
                if(0!=atoi(pstExp->caLeftValue) && 0!=atoi(pstExp->caRightValue))
                        sprintf(pstExp->caResult,"%s.%s", pstExp->caLeftValue, pstExp->caRightValue);
                else if(0==atoi(pstExp->caLeftValue)&& 0!=atoi(pstExp->caRightValue))
                        sprintf(pstExp->caResult,".%s",  pstExp->caRightValue);
                else if (0!=atoi(pstExp->caLeftValue)&& 0==atoi(pstExp->caRightValue))
                        sprintf(pstExp->caResult,"%s",  pstExp->caLeftValue);
                else if(0==atoi(pstExp->caLeftValue)&& 0==atoi(pstExp->caRightValue))
                        sprintf(pstExp->caResult,"%s",  "1");

                printf("%s\n", pstExp->caResult);
                return;
        }

        if(0 ==pstExp->iexp)
        {
                sprintf(pstExp->caResult,"%s", "1");
                printf("%s\n", pstExp->caResult);
                return;
        }





        sprintf(caRealNumber, "%s%s", pstExp->caLeftValue, pstExp->caRightValue);
        sprintf(caTempValue, "%s", caRealNumber);
        for (i = 1; i< pstExp->iexp ; i++)
        {

                StringMultifply(caRealNumber, caTempValue,caResultValue);
                memset(caTempValue, 0, sizeof(caTempValue));
                sprintf(caTempValue, "%s",caResultValue);
                if(i != (pstExp->iexp -1))
                        memset(caResultValue, 0, sizeof(caResultValue));
        }
        iResultLen = strlen(caResultValue);


        iExpDecimalNum = pstExp->iDecimalDigit * pstExp->iexp;

/*
        printf("caRealNumber %s, iExpDecimalNum = %d, iResultLen = %d, caResultValue = %s\n", caRealNumber, iExpDecimalNum, iResultLen, caResultValue); // XXX
        printf("***");
        for (i = 0; i < strlen(caResultValue); ++i)
        {
            printf("%02x ", caResultValue[i]);
        }
        printf("\n");
        */

        if (iExpDecimalNum > 0)
        {
                if(iResultLen > iExpDecimalNum + 1)
                {
                        memcpy(pstExp->caResult, caResultValue, iResultLen - iExpDecimalNum);
                        pstExp->caResult[iResultLen - iExpDecimalNum] = '.';
                        memcpy(pstExp->caResult + iResultLen - iExpDecimalNum+1, caResultValue + iResultLen - iExpDecimalNum, iExpDecimalNum + 1);
                }else if(iResultLen == iExpDecimalNum + 1)
                {
                        if('0'== caResultValue[0])
                        {
                                caResultValue[0] ='.';
                                sprintf(pstExp->caResult, "%s", caResultValue);
                        }else
                        {
                                memcpy(pstExp->caResult, caResultValue, iResultLen - iExpDecimalNum);
                                pstExp->caResult[iResultLen - iExpDecimalNum] = '.';
                                memcpy(pstExp->caResult + iResultLen - iExpDecimalNum+1, caResultValue + iResultLen - iExpDecimalNum, iExpDecimalNum + 1);
                        }
                }
        }else if(0==iExpDecimalNum)
        {
                sprintf(pstExp->caResult, "%s", caResultValue);
        }

        printf("%s\n", pstExp->caResult);
}




int main()
{

        ST_Exp stExpList;  
        char cas[10] = {'\0'};
        int n = 0;
        memset((char *)&stExpList, 0, sizeof(stExpList));

        while(scanf("%s%d",cas,&n)!=EOF) 
        {

                memset((ST_Exp *)&stExpList, 0, sizeof(ST_Exp));
        
                if(!checkInput(cas,n,&stExpList))
                {
                        printf( "the input invalid,R is a real number ( 0.0 < R < 99.999 ) and n is an integer such that 0 < n <= 25 \n");
                        continue;

                }

                ExpCalculateNew((ST_Exp *)&stExpList);

        }


        return 0;

}
