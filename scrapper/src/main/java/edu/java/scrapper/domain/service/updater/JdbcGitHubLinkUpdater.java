package edu.java.scrapper.domain.service.updater;

import dto.request.LinkUpdate;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.LinkUpdater;
import edu.java.scrapper.dto.CommitDTO;
import edu.java.scrapper.dto.GitHubRepositoryDTO;
import edu.java.scrapper.dto.Link;
import edu.java.scrapper.dto.TgUser;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcGitHubLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final GitHubClient gitHubClient;
    private final UpdateSender updateSender;
    private final Logger logger = LoggerFactory.getLogger(JdbcGitHubLinkUpdater.class);

    @Autowired
    public JdbcGitHubLinkUpdater(
        LinkService linkService,
        GitHubClient gitHubClient,
        UpdateSender updateSender
    ) {
        this.linkService = linkService;
        this.gitHubClient = gitHubClient;
        this.updateSender = updateSender;
    }

    @Override
    public void update(Link link) {
        String[] path = link.url().getPath().split("/");
        String username = path[path.length - 2];
        String repositoryName = path[path.length - 1];
        GitHubRepositoryDTO gitHubRepositoryDTO = gitHubClient.getRepository(username, repositoryName);
        Optional<CommitDTO> commitDTO = gitHubClient.getCommit(username, repositoryName);
        List<TgUser> userList = linkService.listAllUsers(link.id());

        if (gitHubRepositoryDTO.updatedAt().isAfter(link.updatedAt())) {
            String lastCommitSha = null;
            String message = "link: %s is updated";
            if (commitDTO.isPresent() && !commitDTO.get().sha().equals(link.lastCommitSha())) {
                lastCommitSha = commitDTO.get().sha();
                message += " new commit" + commitDTO.get().commit().message();
            }
            Link newtimeLink = new Link(link.id(),
                link.url(), gitHubRepositoryDTO.updatedAt(), OffsetDateTime.now(), lastCommitSha, null
            );
            linkService.update(newtimeLink);
            LinkUpdate linkUpdate = new LinkUpdate(
                link.id(),
                link.url(),
                String.format(message, link.url()),
                userList.stream().map(TgUser::userChatId).toList()
            );
            logger.info(linkUpdate.toString());
            updateSender.sendUpdate(
                linkUpdate
            );
        } else {

            //поле updated_at может не обновиться при новом коммите
            String lastCommitSha = null;
            String message = "";
            if (commitDTO.isPresent() && !commitDTO.get().sha().equals(link.lastCommitSha())) {
                lastCommitSha = commitDTO.get().sha();
                message = "new commit " + commitDTO.get().commit().message();
            }
            Link newtimeLink = new Link(link.id(),
                link.url(), link.updatedAt(), OffsetDateTime.now(), lastCommitSha, null
            );
            linkService.update(newtimeLink);
            LinkUpdate linkUpdate = new LinkUpdate(
                link.id(),
                link.url(),
                String.format(message, link.url()),
                userList.stream().map(TgUser::userChatId).toList()
            );
            logger.info(linkUpdate.toString());
            updateSender.sendUpdate(
                linkUpdate
            );
        }
    }

    @Override
    public String getHost() {
        return "github.com";
    }
}
