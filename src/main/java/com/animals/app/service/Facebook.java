package com.animals.app.service;

import java.io.Serializable;

import com.restfb.*;
import com.restfb.types.FacebookType;


/**
 * Created by aquarius on 8/28/2015.
 */
public class Facebook implements Serializable {

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

            Facebook facebook = (Facebook) o;

            if (!message.equals(facebook.message)) return false;
            return !(media != null ? !media.equals(facebook.media) : facebook.media != null);

        }

        @Override
        public int hashCode() {
            int result = message.hashCode();
            result = 31 * result + (media != null ? media.hashCode() : 0);
            return result;
        }


        @Override
        public String toString() {
            return "Facebook{" +
                    "message='" + message + '\'' +
                    ", media='" + media + '\'' +
                    '}';
        }

        public boolean sendFacebook(String accessToken, String wallId, String message){
            FacebookClient facebookClient;
            facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_4);
            FacebookType publishMessageResponse =
                    facebookClient.publish(wallId + "/feed", FacebookType.class, Parameter.with("message", message));

            System.out.println("Published message ID: " + publishMessageResponse.getId());

            //attach any media, if you want to
           //if (!media.equals(null)) **.setMedia(new File(media));

                if (!publishMessageResponse.getId().equals(null))
                    return true;
                else
                    return false;
        }
    }

