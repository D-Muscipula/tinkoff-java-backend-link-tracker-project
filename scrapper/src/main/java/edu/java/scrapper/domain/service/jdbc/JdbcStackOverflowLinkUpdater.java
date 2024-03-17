package edu.java.scrapper.domain.service.jdbc;

import dto.request.LinkUpdate;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.LinkUpdater;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.StackOverflowQuestionDTO;
import edu.java.scrapper.dto.TgUser;
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

            Long answerCount = null;
            String message = "link: %s is updated";

            long countInDb = 0L;
            if (link.answersCount() != null) {
                countInDb = link.answersCount();
            }

            if (stackOverflowQuestionDTO.items().getFirst().answerCount() != countInDb) {
                answerCount = (long) stackOverflowQuestionDTO.items().getFirst().answerCount();
                message += " new answer was added";
            }
            Link newtimeLink = new Link(link.id(),
                link.url(), lastActivityDate, OffsetDateTime.now(), null, answerCount
            );
            //возможно следует убрать эту строку вниз, чтобы
            //база не обновлялась без включенного бота
            linkService.update(newtimeLink);
            List<TgUser> userList = linkService.listAllUsers(link.id());
            botClient.sendUpdate(
                new LinkUpdate(
                    link.id(),
                    link.url(),
                    String.format(message, link.url()),
                    userList.stream().map(TgUser::userChatId).toList()
                )
            );
        } else {
            Long answerCount = null;
            String message = "";
            boolean flag = false;

            if (link.answersCount() == null
                || stackOverflowQuestionDTO.items().getFirst().answerCount() != link.answersCount()) {
                answerCount = (long) stackOverflowQuestionDTO.items().getFirst().answerCount();
                message += "new answer was added";
                flag = true;
            }
            Link newtimeLink = new Link(link.id(),
                link.url(), link.updatedAt(), OffsetDateTime.now(), null, answerCount
            );
            linkService.update(newtimeLink);
            if (flag) {
                List<TgUser> userList = linkService.listAllUsers(link.id());
                botClient.sendUpdate(
                    new LinkUpdate(
                        link.id(),
                        link.url(),
                        String.format(message, link.url()),
                        userList.stream().map(TgUser::userChatId).toList()
                    ));
            }

        }
    }
}
