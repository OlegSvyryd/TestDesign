package com.animals.app.service;

/**
 * Created by aquaneo on 8/19/2015.
 */

import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;

public class Twitt implements Serializable {

    private String message;
    private String media;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Twitt twitt = (Twitt) o;

        if (!message.equals(twitt.message)) return false;
        return !(media != null ? !media.equals(twitt.media) : twitt.media != null);

    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + (media != null ? media.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Twitt{" +
                "message='" + message + '\'' +
                ", media='" + media + '\'' +
                '}';
    }

    public boolean sendTwitt(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){

        //Instantiate a re-usable and thread-safe factory
        TwitterFactory twitterFactory = new TwitterFactory();

        //Instantiate a new Twitter instance
        Twitter twitter = twitterFactory.getInstance();

        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        //setup OAuth Access Token
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

        //Instantiate and initialize a new twitter status update
        StatusUpdate statusUpdate = new StatusUpdate(message);   //your tweet or status message

        //attach any media, if you want to
        if (!(media == null)) statusUpdate.setMedia(new File(media));

        //tweet or update status
        Status status = null;
        try {
            status = twitter.updateStatus(statusUpdate);
            //response from twitter server
            System.out.println("status.toString() = " + status.toString());

            long id = status.getId();
            if (id != 0) return true;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
            return false;
    }
}
