package com.socialworld.web.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseException;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init(){
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("C:\\Users\\Stoyanov22\\Documents\\Programming\\Android\\SocialWorld\\Common files\\socialworld-b49dd-firebase-adminsdk-pl26c-19360b1684.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://socialworld-b49dd.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e){
            throw new DatabaseException("Couldn't configure the database");
        }
    }
}
