use strict;
use warnings;

my $n = 28;

sub findMax {
	my $n = shift;

	my $max_combination = 0;
	my $max_delegates = 0;
	my $max_remainingDelegates = 0;
	my $max_delegatesGroups = 0;
	my @max_numbers;
	for my $groups (1..$n) {
		my $extra = ($groups * ($groups - 1)) / 2;
		my $base = int(($n - $extra) / $groups);
		my $remainder = $n - $base * $groups - $extra;
		my $remainderIndexFromStart = $remainder > 0 ? $groups - $remainder : 999999;

		next if ($remainder < 0);

		my $combination = 1;
		my @xs;
		for my $x ($base..($base+$groups-1)) {
			my $multiplier = $x;
			#print "x: $x $remainder $remainderIndexFromStart $base $extra $groups\n";
			if ($x >= $remainderIndexFromStart + $base) {
				$multiplier = $x+1;
			}

			push @xs, $multiplier;
			$combination *= $multiplier;
		}

		if ($combination > $max_combination)
		{
			$max_combination = $combination;
			@max_numbers = @xs;
		}
	}

	print "$n: $max_combination @" . join(" ", @max_numbers) . "\n";
}

#findMax 10;
for my $n (5..70) {
	findMax $n;
}



