#include <stdio.h>

int vector_size = 0;
int vector[3000];

int clear_from_0th_bit_until_nth_bit(int offset);

int clear_nth_with_previous_bits_already_cleared(int offset)
{
	printf("clear_nth_bit: %d\n", offset);
	return (2 << (offset+1)) - 1;
}

int set_nth_bit_and_clear_previous_bits(int offset)
{
	printf("set_nth_bit: %d\n", offset);
	if (offset < 0)
		return 0;

	if (offset == 0)
	{
		if (vector[offset] == 0)
			return 1;
		return 0;
	}

	if (vector[offset] == 0)
	{
		return set_nth_bit_and_clear_previous_bits(offset-1) + 1 + clear_nth_with_previous_bits_already_cleared(offset-1);
	}
	else
	{
		return clear_from_0th_bit_until_nth_bit(offset-1);
	}
}

int clear_from_0th_bit_until_nth_bit(int offset)
{
	printf("clear_from_0th %d\n", offset);
	if (offset < 0)
		return 0;
	if (offset == 0)
	{
		if (vector[offset] == 0)
			return 0;
		return 1;
	}

	if (vector[offset] == 0)
	{
		printf("vector[offset] == 0\n");
		return clear_from_0th_bit_until_nth_bit(offset-1);
	}
	else
	{
		printf("vector[offset] == 1\n");
		if (vector[offset-1] == 0)
		{
			printf("vector[offset-1] == 0\n");
			return set_nth_bit_and_clear_previous_bits(offset-2) + 1 + 1 + clear_nth_with_previous_bits_already_cleared(offset-1);
		}
		else
		{
			printf("vector[offset-1] == 1\n");
			return clear_from_0th_bit_until_nth_bit(offset-2) + 1 + 1 + clear_nth_with_previous_bits_already_cleared(offset-1);
		}
	}
}

int main()
{
	scanf("%d\n", &vector_size);

	for (int i = 0; i < vector_size; ++i)
	{
		scanf("%d", &vector[i]);
	}
	for (int i = 0; i < vector_size; ++i)
	{
		printf("%d ", vector[i]);
	}
	printf("X\n");

	int steps = clear_from_0th_bit_until_nth_bit(vector_size - 1);
	printf("%d\n", steps);
}

