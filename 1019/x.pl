use strict;
use warnings;

my $digits;
for my $i (1..100) {
	for my $j (1..$i) {
		$digits .= "$j";
	}
}

print "$digits\n";
