
public class CodilityCobaltum2018 {

	public int solution(int[] A, int[] B) {
        int smallestA = -1000000000;
        int smallestB = -1000000000;
        int swapCount = 0;
		for (int i = 0; i < B.length; i++) {
			if (A[i] <= smallestA || B[i] <= smallestB) {
				swapCount = -1;
				break;
			}
			int next = i + 1;
			if (next >= B.length) {
				break;
			}
			if (A[i] > A[next] || B[i] > B[next]) {
				if ((A[i] > B[i] && A[next] < B[next]) || (A[i] < B[i] && A[next] > B[next])) {
					int temp = A[i];
					A[i] = B[i];
					B[i] = temp;
					temp = A[next];
					A[next] = B[next];
					B[next] = temp;
					swapCount++;
				}
			}
			if (smallestA < A[i]) {
				smallestA = A[i];
			}
			if (smallestB < B[i]) {
				smallestB = B[i];
			}
		
		}
		return swapCount;
    }
	public static void main(String[] args) {
		System.out.println(new CodilityCobaltum2018().solution(new int[] {5, 3, 7, 7, 10}, new int[] {1, 6, 6, 9, 9}));
	}
}
