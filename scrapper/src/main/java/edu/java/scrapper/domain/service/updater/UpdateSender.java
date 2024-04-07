package edu.java.scrapper.domain.service.updater;

import dto.request.LinkUpdate;

public interface UpdateSender {
    void sendUpdate(LinkUpdate linkUpdate);
}
