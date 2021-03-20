/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import org.apache.log4j.Logger;
import trongns.listener.MyContextListener;

/**
 *
 * @author TrongNS
 */
public class VerifyUtils implements Serializable {

    private static final String SITE_KEY = "6Lc3oV0aAAAAAJ6mBR9sBfaB9tNxrUrc0JTnXZle";
    private static final String SECRET_KEY = "6Lc3oV0aAAAAAL2aldlwkN7bazQJHAFpDCMnKtmA";
    private static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    static final Logger LOGGER = Logger.getLogger(VerifyUtils.class);

    public static boolean verify(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }
        OutputStream outStream = null;
        JsonReader jsonReader = null;

        try {
            URL verifyUrl = new URL(SITE_VERIFY_URL);

            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String postParams = "secret=" + SECRET_KEY
                    + "&response=" + gRecaptchaResponse;

            conn.setDoOutput(true);

            outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());

            outStream.flush();
            outStream.close();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode=" + responseCode);

            InputStream inStream = conn.getInputStream();

            jsonReader = Json.createReader(inStream);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            System.out.println("Response: " + jsonObject);

            boolean success = jsonObject.getBoolean("success");
            return success;
        } catch (IOException ex) {
            String message = ex.getMessage();
            LOGGER.debug("Error at VerifyUtils _ IOException : " + message);
            return false;
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }

                if (jsonReader != null) {
                    jsonReader.close();
                }
            } catch (IOException ex) {
                String message = ex.getMessage();
                LOGGER.debug("Error at VerifyUtils _ IOException : " + message);
            }
        }
    }

}
