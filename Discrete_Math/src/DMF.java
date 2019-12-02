import java.util.ArrayList;
import java.util.Stack;

public class DMF {
	ArrayList<Integer> primes = new ArrayList<Integer>();
	Boolean[] b = new Boolean[(int) 10e6 + 1];

	// Overflows
	public long fastExpN1(long a, long b, long m) {
		long c = 1;
		for (long i = 1; i <= b; i++) {
			c *= a;
		}
		c %= m;
		return c;
	}

	public long fastExpN2(long a, long b, long m) {
		long c = 1;
		for (long i = 1; i <= b; i++) {
			c = (a * c) % m;
		}
		return c;
	}

	// slower but avoids memory overflow a little more
	public long fastExpN3(long a, long b, long m) {
		long c = 1;
		for (long i = 1; i <= b; i++) {
			c = ((a % m) * (c % m)) % m;
		}
		return c;
	}

	// Current Implementation is to avoid overflow by increasing time of execution
	// if we want to dec execution time will remove % from inside multiplication
	// process
	public long fastExpRec(long a, long b, long m) {
		if (b == 0)
			return 1;
		if (b % 2 == 1)
			return (a * fastExpRec((a * a) % m, (b - 1) / 2, m)) % m;
		return fastExpRec((a * a) % m, b / 2, m) % m;
	}

	public long fastExpIte(long a, long b, long m) {
		long ans = 1;
		a %= m;
		while (b > 0) {
			if (b % 2 == 1) {
				ans = (ans * a) % m;
				b--;
			} else {
				a *= a;
				a %= m;
				b /= 2;
			}
		}

		return ans;
	}

	public long[] EGCD(long a, long b) {
		long aTemp = Math.max(a, b), bTemp = Math.min(b, a);
		a = aTemp;
		b = bTemp;
		long s1 = 1, s2 = 0, t1 = 0, t2 = 1, temp = 0;
		long q = a / b, r = a % b, d = r;
		a = b;
		b = r;
		while (r != 0) {
			temp = s2;
			s2 = s1 - s2 * q;
			s1 = temp;
			temp = t2;
			t2 = t1 - t2 * q;
			t1 = temp;
			d = r;
			r = a % b;
			q = a / b;
			a = b;
			b = r;
		}
		long[] ans = { d, s2, t2 };
		return ans;
	}

	public void Seive(int n) {
		if (n <= 1 || n > (int) (10e6))
			return;
		for (int i = 2; i <= n; i++)
			b[i] = false;
		for (int i = 2; i * i <= n; i++) {
			if (!b[i]) {
				for (int j = i * i; j <= n; j += i) {
					b[j] = true;
				}
				primes.add(i);
			}
		}
	}

	public int primeNumberGen() {
		if (primes.size() == 0)
			Seive((int) 10e6);
		int random = (int) Math.round((primes.size()) * Math.random());
		// System.out.println("Your Random number is "+primes.get(random));
		return primes.get(random);
	}

	public long[] CRT(long[] mods,long[] a) {
		long M = 1, x = 0;
		int sz = a.length;
		long[][] ans = new long[a.length][3];
		for (int i = 0; i < sz; i++) {
			M *= mods[i];
		}
		for (int i = 0; i < sz; i++) {
			long mi = mods[i];
			ans[i][0] = a[i];
			ans[i][1] = M / mi;
			ans[i][2] = EGCD(ans[i][1] % mi, mi)[2];
			x += (ans[i][0] * ans[i][1] * ans[i][2]) % M;
			x%=M;
		}
		while (x < 0)
			x += M;
		// System.out.println("Answer is "+ x + " (mod "+M+")");
		return new long[] { x%M, M };

	}

	public long[][] CRTOperation(long[] mods, long A, long B) {
		int sz = mods.length;
		long[][] A_B = new long[2][sz];
			for (int i = 0; i < sz; i++) {
				A_B[0][i] = A % mods[i];
				A_B[1][i] = B % mods[i];
				//System.out.println(A_B[0][i]);
				//System.out.println(A_B[1][i]);
			}
		long[] add = CRTAdd(mods, A_B), mpy = CRTMultiply(mods, A_B);
		long[] addAns = CRT(mods, add), mpyAns = CRT(mods, mpy);
		System.out.println("C = A+B = " + A + " + " + B + " = " + addAns[0] + "(Mod " + addAns[1] + ")");
		System.out.println("D = A*B = " + A + " * " + B + " = " + mpyAns[0] + "(Mod " + mpyAns[1] + ")");
		return new long[][] { addAns, mpyAns };

	}

	private long[] CRTAdd(long[] mods, long[][] A_B) {
		int sz = mods.length;
		long[] ans = new long[sz];
		for (int i = 0; i < sz; i++)
			ans[i] = (A_B[0][i] + A_B[1][i]) % mods[i];
		return ans;
	}

	private long[] CRTMultiply(long[] mods, long[][] A_B) {
		int sz = mods.length;
		long[] ans = new long[sz];
		for (int i = 0; i < sz; i++)
			ans[i] = (A_B[0][i] * A_B[1][i]) % mods[i];
		return ans;
	}

	public static void main(String[] args) {
		DMF e = new DMF();
		long[] ans;
		// Fast Expontiation (Iterative)
		System.out.println(e.fastExpIte(3185, 2753, 3233) + " From Iterative");

		// Fast Expontiation (Recursive)
		System.out.println(e.fastExpRec(3185, 2753, 3233) + " From Recursive");

		// Random Number Generation
		System.out.println("Your Random number is " + e.primeNumberGen());

		// Extended Euclidian Theorem
		int A = 8,B=35;
		ans = e.EGCD(8, 35);
		System.out.println(ans[0] + " = (" + Math.max(A, B) + "*" + ans[1] + ") + (" + Math.min(A, B) + "*" + ans[2] + ")");

		// Chinese Reminder Theroem
		long[] a = { 2, 1, 3 };
		long[] mods =new long [] { 3, 4, 5 };
		ans = e.CRT(mods, a);
		System.out.println("Answer is " + ans[0] + " (mod " + ans[1] + ")");
		
		
		mods =new long [] {99,98,97,95};
		e.CRTOperation(mods , 123684,413456);
	}

}
