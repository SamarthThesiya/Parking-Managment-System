package utils;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.Date;
import java.util.List;

public class JwtUtils {

    public static String getValueFromJwtToken(String jwtToken, String key) {
        JWT jwt = new JWT(jwtToken);
        return jwt.getClaim(key).asString();
    }

}
