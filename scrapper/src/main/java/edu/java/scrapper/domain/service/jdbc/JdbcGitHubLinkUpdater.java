package edu.java.scrapper.domain.service.jdbc;

import dto.request.LinkUpdate;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.LinkUpdater;
import edu.java.scrapper.dto.GitHubRepositoryDTO;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcGitHubLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final GitHubClient gitHubClient;
    private final BotClient botClient;
    private final Logger logger = LoggerFactory.getLogger(JdbcGitHubLinkUpdater.class);

    @Autowired
    public JdbcGitHubLinkUpdater(
        LinkService linkService,
        GitHubClient gitHubClient,
        BotClient botClient
    ) {
        this.linkService = linkService;
        this.gitHubClient = gitHubClient;
        this.botClient = botClient;
    }

    @Override
    public void update(Link link) {
        String[] path = link.url().getPath().split("/");
        String username = path[path.length - 2];
        String repositoryName = path[path.length - 1];
        GitHubRepositoryDTO gitHubRepositoryDTO = gitHubClient.getRepository(username, repositoryName);
        if (gitHubRepositoryDTO.updatedAt().isAfter(link.updatedAt())) {
            Link newtimeLink = new Link(link.id(),
                link.url(), gitHubRepositoryDTO.updatedAt(), OffsetDateTime.now()
            );
            linkService.update(newtimeLink);
            List<TgUser> userList = linkService.listAllUsers(link.id());
            LinkUpdate linkUpdate = new LinkUpdate(
                link.id(),
                link.url(),
                String.format("link: %s is updated", link.url()),
                userList.stream().map(TgUser::userChatId).toList());
            logger.info(linkUpdate.toString());
            botClient.sendUpdate(
                linkUpdate
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
        return "github.com";
    }
}
