use strict;
use warnings;

my @patternX = ('1', '2', '1', '2', '2', '1', '2', '1', '2', '2', '1', '2', '2');
#21
my @patternY = ('1', '2', '1', '2', '2', '1', '2', '2');
#13
my @patternA = ('X', 'X', 'Y', 'X', 'X', 'Y', 'X', 'Y');
#5 * 21 + 13 * 3 = 144
my @patternB = ('X', 'X', 'Y', 'X', 'Y');
#3 * 21 + 12 * 2 = 87
my @patternM = ('A', 'A', 'B', 'A', 'A', 'B', 'A', 'B');
# 5 * 144 + 3 * 87 = 981
my @patternN = ('A', 'A', 'B', 'A', 'B');
# 3 * 144 + 2 * 87 = 606
my @patternP = ('M', 'M', 'N', 'M', 'M', 'N', 'M', 'N');
# 5 * 981 + 3 * 606 = 6723
my @patternQ = ('M', 'M', 'N', 'M', 'N');
# 3 * 981 + 2 * 606 = 4155
my @patternJ = ('P', 'P', 'Q', 'P', 'P', 'Q', 'P', 'Q');
# 5 * 6723 + (3 * 4155) = 46080
my @patternK = ('P', 'P', 'Q', 'P', 'Q');
# 3 * 6723 + (2 * 4155) = 28479
my @patternC = ('J', 'J', 'K', 'J', 'J', 'K', 'J', 'K');
# 5 * 46080 + (3 * 28479) = 315837
my @patternD = ('J', 'J', 'K', 'J', 'K');
# 3 * 46080 + (2 * 28479) = 195198
# 5 * 315837 + (3 * 195198) = 2165779
# 3 * 315837 + (2 * 195198) = 1337907
# 5 * 2165779 + (3 * 1337907) = 14842616
# 3 * 2165779 + (2 * 1337907) = 9173151
# 5 * 14842616 + (3 * 9173151) = 101732533 
# 3 * 14842616 + (2 * 9173151) = 62874150 
# 5 * 101732533  + (3 * 62874150) = 697285115
# 3 * 101732533  + (2 * 62874150) = 430945899
# 5 * 697285115  + (3 * 430945899) = 4779263272
#

sub matchPattern {
	my $buffer = shift;
	my $patternX = shift;
	my $pattern = shift;

	if (@$buffer >= @$patternX) {
		my $isPatternX = 1;
		for (my $i = 0; $i < @$patternX; ++$i) {
			if ($buffer->[$i] ne $patternX->[$i]) {
				$isPatternX = 0;
				last;
			}
		}

		if ($isPatternX) {
			for (my $i = 0; $i < @$patternX; ++$i) {
				shift @$buffer;
			}
			print "$pattern\n";
		}
	}
}

my @buffer;
while (<STDIN>) {
	chomp;
	push(@buffer, "$_");
#	for (my $i = 0; $i < @buffer; ++$i) {
#		print "$buffer[$i] ";
#	}
#	print "\n";

	matchPattern(\@buffer, \@patternX, "X");
	matchPattern(\@buffer, \@patternY, "Y");
	matchPattern(\@buffer, \@patternA, "A");
	matchPattern(\@buffer, \@patternB, "B");
	matchPattern(\@buffer, \@patternM, "M");
	matchPattern(\@buffer, \@patternN, "N");
	matchPattern(\@buffer, \@patternP, "P");
	matchPattern(\@buffer, \@patternQ, "Q");
	matchPattern(\@buffer, \@patternJ, "J");
	matchPattern(\@buffer, \@patternK, "K");
	matchPattern(\@buffer, \@patternC, "C");
	matchPattern(\@buffer, \@patternD, "D");
}

