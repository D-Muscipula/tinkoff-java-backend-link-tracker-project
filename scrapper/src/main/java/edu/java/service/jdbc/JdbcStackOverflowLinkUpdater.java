package edu.java.service.jdbc;

import dto.request.LinkUpdate;
import edu.java.client.BotClient;
import edu.java.client.StackOverflowClient;
import edu.java.dto.Link;
import edu.java.dto.StackOverflowQuestionDTO;
import edu.java.dto.TgUser;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
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
                link.url(), lastActivityDate, link.lastCheckedAt()
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
        }
    }
}
