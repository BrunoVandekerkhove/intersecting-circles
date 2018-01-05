A Java app to detect intersections in a collection of circles. It makes use of three different algorithms (brute force, simple sweep line algorithm and Bentley-Ottmann algorithm) to fetch all intersections. 
Speed is respectively of the order of `O(N²)`, `O((N+K)*log(N))` (worst-case `O(N²)`) and `O((N+K)*log(N))`.

## Algorithms

The brute force algorithm compares each circle with each other circle.
The sweep line algorithm deals with an imaginary sweep line hovering over all circles. It preprocesses the circles by sorting them on the basis of x-coordinate, then hovers the sweep line, activating (and deactivating) circles that are being hovered. This limits the number of comparisons for each (*active*) circle to those circles that are *active* at the same *time* (that is, x-coordinate).
The Bentley Ottman algorithm applies the same reasoning, but makes use of specialised data structures for ensuring logarithmic time complexity of the fundamental operations.
