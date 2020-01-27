This example works best on big numbers. For 10000 threads and 10000 incrementations in each you may get those results: 

	expectation: 100000000
	reality:      51563849

or: 

	expectation: 100000000
	reality:      49104583

But even for 2 threads and 1000 incrementations the result can be seen sometimes:

	expectation: 2000
	reality:     1703

The reason for this is `count++` being several atomic operations, even when `count` is `volatile`. So, several threads incrementing value at the «same» time would read from the same source, change result locally and write it back. But, because they did so in parallel, the resulting value might be less than expected.