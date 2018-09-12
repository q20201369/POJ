my $a = 13;
my $b = 8;
for my $i (0..9) {
	my $x = 5 * $a + 3 * $b;
	my $y = 3 * $a + 2 * $b;
	print "{$x, $y}\n";
	$a = $x; $b = $y;
}
