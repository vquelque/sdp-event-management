package ch.epfl.sweng.eventmanager.ticketing;

import android.content.SharedPreferences;
import android.util.Log;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;

import java.util.Date;

/**
 * A class that represents a way to store authentication tokens for an event's ticketing platform
 *
 * @author Louis Vialar
 */
public class TokenStorage {
    private final SharedPreferences preferences;
    private int eventId;
    private String token;

    TokenStorage(int eventId, SharedPreferences preferences) {
        this.eventId = eventId;
        this.preferences = preferences;

        readTokenFromPreferences();
    }

    protected void readTokenFromPreferences() {
        // Retrieve token if it exists
        if (preferences.contains(String.valueOf(eventId))) {
            this.token = preferences.getString(String.valueOf(eventId), null);
        }
    }

    protected void writeTokenToPreferences() {
        preferences.edit().putString(String.valueOf(eventId), token).apply();
    }

    /**
     * Check if this token storage holds a valid token
     * @return true if this storage holds a valid JWT token or a non-JWT token
     */
    public boolean isLoggedIn() {
        if (token == null) {
            return false; // No token == not logged in
        }

        try {
            JWT jwt = new JWT(token);
            Date exp = jwt.getExpiresAt();

            Log.i("TicketingService", "Logged in with token expiring at " + exp + " / " + jwt.toString());

            return exp == null || exp.after(new Date()); // token is not expired yet
        } catch (DecodeException exception) {
            // Not a JWT token
            // We consider the token as valid for ever
            return true;
        }
    }

    public void logout() {
        this.token = null;

        writeTokenToPreferences();
    }

    void setToken(String token) {
        this.token = token;

        writeTokenToPreferences();
    }

    String getToken() {
        return token;
    }
}
