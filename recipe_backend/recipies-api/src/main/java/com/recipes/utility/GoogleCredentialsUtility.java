package com.recipes.utility;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@UtilityClass
public class GoogleCredentialsUtility {

    public static GoogleCredentials getCredentials(final String filename,ClassLoader classLoader) throws IOException {
        GoogleCredentials credentials;
        final File credentialsPath = new File(classLoader.getResource(filename).getFile());
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        }
        credentials=getCredentials();
        return credentials;
    }

}
