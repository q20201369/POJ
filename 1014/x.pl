use strict;
use warnings;

sub canPack {
	my $target = shift;
	my @marbles = @_;

	my ($i1, $i2, $i3, $i4, $i5, $i6) = @marbles;

	for my $j1 (0..$i1) {
		for my $j2 (0..$i2) {
			for my $j3 (0..$i3) {
				for my $j4 (0..$i4) {
					for my $j5 (0..$i5) {
						for my $j6 (0..$i6) {
							return 1 if ($j1*1 + $j2*2 + $j3*3 + $j4*4 + $j5*5 + $j6*6 == $target)
						}
					}
				}
			}
		}
	}

	return 0;
}

sub run {
	my $target = shift;
	my @marbles = @_;

	print "===\n";
	print join(" ", @marbles) . "\n";
	#print "$marbles[0] $marbles[1] $marbles[2] $marbles[3] $marbles[4] $marbles[5]\n";
	print "0 0 0 0 0 0\n";
	print "===\n";
	print "Collection #1:\n";
	if (canPack($target, @marbles)) {
		print "Can be divided.\n";
	}
	else {
		print "Can't be divided.\n";
	}
}

for my $i1 (0..60) {
	for my $i2 (0..60) {
		for my $i3 (0..60) {
			for my $i4 (0..60) {
				for my $i5 (0..60) {
					for my $i6 (0..60) {
						my $sum = $i1*1 + $i2*2 + $i3*3 + $i4*4 + $i5*5 + $i6*6;
						if ($sum % 2 == 0) {
							run($sum/2, $i1, $i2, $i3, $i4, $i5, $i6);
						}
					}
				}
			}
		}
	}
}

