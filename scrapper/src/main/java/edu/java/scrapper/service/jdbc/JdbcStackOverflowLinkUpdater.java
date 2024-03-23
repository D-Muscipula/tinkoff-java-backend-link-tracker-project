package edu.java.scrapper.service.jdbc;

import dto.request.LinkUpdate;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.StackOverflowQuestionDTO;
import edu.java.scrapper.dto.TgUser;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcStackOverflowLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final StackOverflowClient stackOverflowClient;
    private final BotClient botClient;

    @Autowired
    public JdbcStackOverflowLinkUpdater(
        LinkService linkService,
        StackOverflowClient stackOverflowClient,
        BotClient botClient
    ) {
        this.linkService = linkService;
        this.stackOverflowClient = stackOverflowClient;
        this.botClient = botClient;
    }

    @Override
    public void update(Link link) {
        String[] path = link.url().getPath().split("/");
        String question = path[path.length - 2];
        StackOverflowQuestionDTO stackOverflowQuestionDTO = stackOverflowClient.getQuestion(Long.parseLong(question));
        OffsetDateTime lastActivityDate = stackOverflowQuestionDTO.items().getFirst()
            .lastActivityDate();
        if (lastActivityDate
            .isAfter(link.updatedAt())) {
            Link newtimeLink = new Link(link.id(),
                link.url(), lastActivityDate, OffsetDateTime.now()
            );
            linkService.update(newtimeLink);
            List<TgUser> userList = linkService.listAllUsers(link.id());
            botClient.sendUpdate(
                new LinkUpdate(
                    link.id(),
                    link.url(),
                    String.format("link: %s is updated", link.url()),
                    userList.stream().map(TgUser::userChatId).toList()
                )
            );
        } else {
            Link newtimeLink = new Link(link.id(),
                link.url(), link.updatedAt(), OffsetDateTime.now()
            );
            linkService.update(newtimeLink);
        }
    }

    @Override
    public String getHost() {
        return "stackoverflow.com";
    }
}
