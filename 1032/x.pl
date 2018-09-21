use strict;
use warnings;

my $n = 28;

sub findMax {
	my $n = shift;

	my $max_combination = 0;
	my $max_delegates = 0;
	my $max_remainingDelegates = 0;
	my $max_delegatesGroups = 0;
	for my $groups (1..$n) {
		my $delegates = int($n / $groups);
		my $remainingDelegates = $n - $delegates * $groups;

		my $delegatesGroups = $groups - $remainingDelegates;
		my $remainingGroups = $remainingDelegates;

		my $combination = ($delegates ** $delegatesGroups) * ($remainingDelegates > 0 ? (($delegates+1) ** $remainingDelegates) : 1);

		#print "$n: $combination, $delegates**$delegatesGroups * {$delegates+1}**$remainingDelegates\n";

		if ($combination > $max_combination)
		{
			$max_combination = $combination;
			$max_delegates = $delegates;
			$max_remainingDelegates = $remainingDelegates;
			$max_delegatesGroups = $delegatesGroups;
		}
	}

	print "$n: $max_combination, $max_delegates**$max_delegatesGroups * {$max_delegates+1}**$max_remainingDelegates\n";
}

#findMax 6;
for my $n (5..30) {
	findMax $n;
}



