package com.github.Requu1.CampaignManager.util;

import com.github.Requu1.CampaignManager.exception.NoPermissionException;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public class SessionUtil {
    private SessionUtil(){}

    public static UUID getSellerIdFromSession(HttpSession session){
        UUID sellerId=(UUID)session.getAttribute("LOGGED_IN_SELLER_ID");
        if(sellerId==null){
            throw new NoPermissionException("User is not logged in");
        }
        return sellerId;
    }
}
