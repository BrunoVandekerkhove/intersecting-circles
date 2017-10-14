/**
 * A class to facilitate QuickSorting arrays of comparables.
 * 
 * @author Bruno Vandekerkhove
 * @version 1.0
 */
public class QuickSort {

	/**
	 * QuickSort the given array of comparables.
	 * 
	 * @param 	array
	 * 			An array of comparable items.
	 * @post	The given array is sorted in ascending order.
	 * 			| for each index in 1..array.length
	 * 			|	array[index].less(array[index-1]) == false
	 */
	public static <T extends Comparable<T>> void quicksort(T[] array) {
		quicksort(array, 0, array.length - 1);
	}
	
	/**
	 * QuickSort the given array of comparables in between the given indices.
	 * 
	 * @param 	array
	 * 			An array of comparables.
	 * @param 	low
	 * 			The minimum index of the array segment that is to be sorted.
	 * @param 	high
	 * 			The maximum index of the array segment that is to be sorted.
	 */
	private static <T extends Comparable<T>> void quicksort(T[] array, int low, int high) {
		if (high <= low)
			return;
		int j = partition(array, low, high);
		quicksort(array, low, j-1);
		quicksort(array, j+1, high);
	}
	
	/**
	 * Partition the given array of comparables in between the given indices
	 * 	and return an index as such that all elements before this index are smaller
	 * 	and all elements after this index are bigger than the element at that index.
	 * 
	 * @param 	array
	 * 			An array of comparables.
	 * @param 	low
	 * 			The minimum index of the array segment that is to be partitioned.
	 * @param 	high
	 * 			The maximum index of the array segment that is to be partitioned.
	 * @return	An index such that the element at that index is in its final place in the array.
	 */
	private static <T extends Comparable<T>> int partition(T[] array, int low, int high) {
		int i = low, j = high + 1;
		T v = array[low];
		while (true) {
			while (less(array[++i], v))
				if (i == high)
					break;
			while (less(v, array[--j]))
				if (j == low)
					break;
			if (i >= j)
				break;
			exchange(array, i, j);
		}
		exchange(array, low, j);
		return j;
	}
	
	/**
	 * Checks whether the first comparable is less than the second one.
	 * 
	 * @param 	first
	 * 			The first comparable.
	 * @param 	second
	 * 			The second comparable.
	 * @return	True if and only if the first comparable is less than the second one.
	 * 			| result == first.compareTo(second) < 0
	 */
	private static <T extends Comparable<T>> boolean less(T first, T second) {
		return first.compareTo(second) < 0;
	}
	
	/**
	 * Exchange the item at the first index with the item at the second index in the given array of comparables.
	 * 
	 * @param 	array
	 * 			An array of comparables.
	 * @param 	first
	 * 			The index of the first item to exchange.
	 * @param	second
	 * 			The index of the second item to exchange.
	 */
	private static <T extends Comparable<T>> void exchange(T[] array, int first, int second) {
		T intermediate = array[first];
		array[first] = array[second];
		array[second] = intermediate;
	}
	
}
