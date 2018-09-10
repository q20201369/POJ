use strict;
use warnings;

my $cmd = $ARGV[0];

my $tests;

open X, "<tests";

while (<X>) {
	$tests .= $_;
}

close X;

my @lines = split("\n", $tests);

my $separator = $lines[0];

sub read_file {
	my $file = shift;

	my $file_content = "";
	open FILE, "<$file";
	while (<FILE>) {
		$file_content .= $_;
	}
	close FILE;

	return $file_content;
}

sub compare_two_files {
	my $f1 = shift;
	my $f2 = shift;

	my $f1_content = read_file($f1);
	my $f2_content = read_file($f2);

	return $f1_content eq $f2_content;
}

sub process_test {
	my $test_index = shift;
	my $i = shift;

	if ($i >= @lines) {
		return;
	}

	open X, ">tests.$test_index.in";
	while ($lines[$i] ne $separator) {
		print X "$lines[$i]\n";
		$i++;
	}
	close X;
	$i++; # skip separator

	open X, ">tests.$test_index.out";
	while ($lines[$i] ne $separator) {
		print X "$lines[$i]\n";
		$i++;
	}
	close X;
	$i++;

	# run test
	system("$cmd <tests.$test_index.in >tests.$test_index.result");
	my $result = compare_two_files("tests.$test_index.out", "tests.$test_index.result");
	if ($result) {
		print ".";
	}
	else {
		print "X";
	}

	process_test($test_index+1, $i);
}

process_test(0, 1);
