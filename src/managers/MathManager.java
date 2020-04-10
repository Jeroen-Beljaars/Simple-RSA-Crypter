package managers;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which will be responsible for all the maths.
 */
public class MathManager {
    /**
     * Get all the factors of a prime
     *
     * @param n The value of a number N which we want to factorize
     * @return list of the factorized primes of the provided value
     */
    public static List<Long> factorizePrime(long n) {
        List<Long> factors = new ArrayList<Long>();

        for (long i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }

        if (n > 1) {
            factors.add(n);
        }

        return factors;
    }

    public static boolean isNumberPrime(long n) {
        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;

        return true;
    }

    public static long eulerTotient(long p, long q) {
        return (p - 1) * (q - 1);
    }

    public static long gcdByEuclidsAlgorithm(long n1, long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1 % n2);
    }

    public static List<Long> getPrimesRange(long begin, long end) {
        // Declare the variables
        boolean isPrime;
        List<Long> primes = new ArrayList<Long>();

        // Traverse each number in the interval
        // with the help of for loop
        for (long i = begin; i <= end; i++) {

            // Skip 0 and 1 as they are
            // neither prime nor composite
            if (i == 1 || i == 0)
                continue;

            // flag variable to tell
            // if i is prime or not
            isPrime = true;

            for (long j = 2; j <= i / 2; ++j) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime)
                primes.add(i);
        }
        return primes;
    }

    public static long calculateExponentD(long e, long eulerTotient) {
        double k = 1;
        double result;
        while (true) {
            result = (1.0 + k * ((double) eulerTotient)) / (double) e;
            if (result % 1 == 0) {
                break;
            }
            k++;
        }
        return (long) result;
    }
}
