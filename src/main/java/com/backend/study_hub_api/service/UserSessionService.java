package com.backend.study_hub_api.service;

public interface UserSessionService {

    boolean checkUserSession(String tokenId);
    void removeExpiredSession(String tokenId);

}
