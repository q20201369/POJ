use strict;
use warnings;

my $n = 40;
my $m = 4;

my @deltas;

for my $i (1..$n) {
	my $delta = int(rand(41)) - 20;

	push @deltas, $delta;
}

my @sorted_deltas = sort { $a <=> $b } @deltas;

print join(" ", @sorted_deltas) . "\n";

my $min_sum = 99999999;
my @path;
my %sums;
sub find_min_sum {
	my $offset = shift;
	my $sum = shift;
	my $depth = shift;

	if ($depth == $m) {
		if (abs($sum) <= $min_sum) {
			$min_sum = abs($sum);
			$sums{$min_sum} = [] if not defined $sums{$min_sum};
			push @{$sums{$min_sum}}, [@path];
		}
	}

	return if ($offset >= @deltas-1);
	return if ($depth > $m);

	for my $i (($offset+1)..@sorted_deltas) {
		push @path, $i-1;
		find_min_sum($i, $sum + $sorted_deltas[$i-1], $depth+1);
		pop @path;
	}
}

find_min_sum(0, 0, 0);

print "min: $min_sum\n";
my @min_paths = @{$sums{$min_sum}};
for my $i (1..@min_paths) {
	print join(" ", @{$min_paths[$i-1]}) . "\n";
}
