package edu.java.service.jdbc;

import dto.request.LinkUpdate;
import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.dto.GitHubRepositoryDTO;
import edu.java.dto.Link;
import edu.java.dto.TgUser;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcGitHubLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final GitHubClient gitHubClient;
    private final BotClient botClient;

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
                link.url(), gitHubRepositoryDTO.updatedAt(), link.lastCheckedAt()
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
