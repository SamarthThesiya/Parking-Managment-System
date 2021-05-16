package utils;

import com.auth0.android.jwt.JWT;

public class JwtUtils {

    public static String getValueFromJwtToken(String jwtToken, String key) {
        JWT jwt = new JWT(jwtToken);
        return jwt.getClaim(key).asString();
    }

}
