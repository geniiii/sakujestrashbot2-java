package com.geni.sakujestrashbot2;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {
    public class Config {
        public String consumer_key;
        public String consumer_secret;
        public String access_key;
        public String access_secret;
        public String[] words_start;
        public String[] words_end;
    }

    public static Config getConfig() throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader("./config.sakujes"));
        Config conf = new Gson().fromJson(reader, Config.class);
        return conf;
    }

    public static void main(String[] args) throws IOException {
        Config conf = getConfig();

        String consumerKey = conf.consumer_key;
        String consumerSecret = conf.consumer_secret;
        String accessToken = conf.access_key;
        String accessSecret = conf.access_secret;

        ConfigurationBuilder clientConfig = new ConfigurationBuilder();
        clientConfig.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessSecret);
        TwitterFactory clientFac = new TwitterFactory(clientConfig.build());

        Twitter twitter = clientFac.getInstance();

        Status status;

        try {
            BufferedReader br = new BufferedReader(new FileReader("./stuff.sakujes"));
            ArrayList<String> array = new ArrayList<>();

            int randomIndex = 0;
            String line;

            while (true) {
                while ((line = br.readLine()) != null)
                    array.add(br.readLine());

                Random rand = new Random();
                randomIndex = rand.nextInt(array.size());

                String cvolton = conf.words_start[rand.nextInt(conf.words_start.length)] + array.get(randomIndex) + conf.words_end[rand.nextInt(conf.words_end.length)];

                System.out.println("sending tweet: " + cvolton);
                status = twitter.updateStatus(cvolton);

                System.out.println("sent tweet: " + status.getText());
                Thread.sleep(900000);
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
