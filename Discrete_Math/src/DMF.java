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

	public long[] CRT (long [][] par) {
		long M =1 ,x=0;
				int sz = par.length;
		long [][] ans = new long[par.length][3];
		for(int i =0 ;i<sz; i++) {
			M*=par[i][1];
		}
		for(int i =0 ;i<sz; i++) {
			long mi  = par[i][1];
			ans[i][0]   = par[i][0];
			ans[i][1] = M/par[i][1];
			ans[i][2] = EGCD(ans[i][1]%mi, mi)[2];
			x+= (ans[i][0]*ans[i][1]*ans[i][2])%M;
		}
		while (x<0)x+=M;
		//System.out.println("Answer is "+ x + " (mod "+M+")");
		return new long[] {x,M};
		
	}

	public static void main(String[] args) {
		DMF e = new DMF();
		long []ans;
		//Fast Expontiation (Iterative)
		System.out.println(e.fastExpIte(3185, 2753, 3233) + " From Iterative");	
		
		//Fast Expontiation (Recursive)
		System.out.println(e.fastExpRec(3185, 2753, 3233) + " From Recursive");	
		
		//Random Number Generation
		System.out.println("Your Random number is " + e.primeNumberGen());
		
		//Extended Euclidian Theorem 
		ans = e.EGCD(21, 44);
		System.out.println(ans[0] + " = (" + 44 + "*" + ans[1] + ") + (" + 21 + "*" + ans[2] + ")");

		//Chinese Reminder Theroem
		long [][] test = {{2 , 3},{1,4},{3,5}};
		ans = e.CRT(test);
		System.out.println("Answer is "+ ans[0] + " (mod "+ans[1]+")");

		
	}

}
