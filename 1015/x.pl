use strict;
use warnings;

sub generateTestCase {
	my $n = shift;
	my $m = shift;

	my @grades;

	for my $i (1..$n) {
		push @grades, int(rand(21)), int(rand(21));
	}

	return @grades;
}

my @selections;
sub findJury {
	my $grades = shift;
	my $n = shift;
	my $m = shift;
	my $juriesLeft = shift;
	my $currentJury = shift;
	my $selection = shift;

	if ($juriesLeft == 0) {
		if (@$selection == $m) {
			my @cloned = map { $_ } @$selection;
			push @selections, \@cloned;
		}
		return;
	}

	if ($currentJury > $n-1) {
		if (@$selection == $m) {
			my @cloned = map { $_ } @$selection;
			push @selections, \@cloned;
		}
		return;
	}

	push @$selection, $currentJury;
	findJury($grades, $n, $m, $juriesLeft-1, $currentJury+1, $selection);
	pop @$selection;

	findJury($grades, $n, $m, $juriesLeft, $currentJury+1, $selection);
}


my $n = 4;
my $m = 2;

my @grades = generateTestCase $n, $n;

print "===\n";
print "$n $m\n";
for my $i (1..(@grades/2)) {
	print $grades[($i-1)*2] . " " . $grades[($i-1)*2+1] . "\n";
}
print "0 0\n";

findJury(\@grades, $n, $m, $m, 0, []);

sub processSelection {
	my $grades = shift;
	my $selection = shift;

	my $delta = 0;
	my $sum = 0;
	for my $i (1..@$selection) {
		my $j = $selection->[$i-1];
		my $pg = $grades->[2*$j];
		my $dg = $grades->[2*$j+1];

		#print "$pg - $dg\n";

		$delta += abs($pg-$dg);
		$sum += $pg+$dg;
	}

	return $delta, $sum;
}

my @processed;
for my $i (1..@selections) {
	my @selection = @{$selections[$i-1]};
	# print join(" ", @selection) . "\n";

	push @processed, [\@selection, processSelection(\@grades, \@selection)];
}


my @sorted = sort {
	if ($a->[1] == $b->[1]) {
		return $a->[2] <=> $b->[2];
	}
	else {
		$a->[1] <=> $b->[1];
	}
} @processed;

#for my $i (1..@sorted) {
#	my @result = @{$sorted[$i-1]};
#	print join(" ", @{$result[0]}) . "; " . $result[1] . "; " . $result[2] . "\n";
#}

my $selected = $sorted[@sorted-1];
my $sumP = 0;
my $sumD = 0;
for my $i (1..@{$selected->[0]}) {
	my $j = $selected->[0]->[$i-1];
	$sumP += $grades[2*$j];
	$sumD += $grades[2*$j+1];
}

print "===\n";
print "Jury #1\n";
print "Best jury has value $sumP for prosecution and value $sumD for defence:\n";
print " " . join(" ", @{$selected->[0]}) . "\n";
