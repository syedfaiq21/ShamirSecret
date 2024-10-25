import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.HashMap;

public class ShamirSecret {

    public static BigInteger decodeBase(String value, int base) {
        return new BigInteger(value, base);
    }

    public static BigInteger lagrangeInterpolation(HashMap<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;
        for (Integer i : points.keySet()) {
            BigInteger xi = BigInteger.valueOf(i);
            BigInteger yi = points.get(i);

            BigInteger term = yi;
            for (Integer j : points.keySet()) {
                if (!i.equals(j)) {
                    BigInteger xj = BigInteger.valueOf(j);
                    term = term.multiply(xj.negate()).divide(xi.subtract(xj));
                }
            }
            result = result.add(term);
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            FileReader reader = new FileReader("testcase.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            JSONObject keysObject = jsonObject.getJSONObject("keys");
            int n = keysObject.getInt("n");
            int k = keysObject.getInt("k");

            HashMap<Integer, BigInteger> points = new HashMap<>();
            for (String key : jsonObject.keySet()) {
                if (key.equals("keys")) continue;

                JSONObject pointObject = jsonObject.getJSONObject(key);
                int x = Integer.parseInt(key);
                int base = pointObject.getInt("base");
                String value = pointObject.getString("value");

                BigInteger y = decodeBase(value, base);
                points.put(x, y);
            }

            BigInteger secret = lagrangeInterpolation(points, k);
            System.out.println("The secret constant term (c) is: " + secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
