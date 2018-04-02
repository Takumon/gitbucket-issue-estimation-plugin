package gitbucket.plugin.controller


import gitbucket.core.controller.ControllerBase
import gitbucket.core.service._
import gitbucket.core.util.StringUtil._
import gitbucket.core.util.Implicits._
import gitbucket.core.util.ReferrerAuthenticator
import gitbucket.plugin.service.IssueEstimateService



class IssueEstimateController
  extends JenkinsResultCommentControllerBase
    with IssueEstimateService
    with RepositoryService
    with AccountService
    with IssuesService
    with ReferrerAuthenticator
    with PullRequestService
    with CommitsService
    with WebHookPullRequestService



trait JenkinsResultCommentControllerBase extends ControllerBase {
  self: IssueEstimateService
    with RepositoryService
    with CommitsService
    with IssuesService
    with ReferrerAuthenticator
    with AccountService
    with WebHookPullRequestService
    with PullRequestService =>



  get("/api/v3/repos/:owner/:repository/issues/:issueId/estimate")(referrersOnly { repository =>

    val issueId = params("issueId")

    if ( !isInteger(issueId)) {

      notFound()
    } else {

      getIssueEstimate(repository.owner, repository.name, issueId.toInt).map { e =>
        org.json4s.jackson.Serialization.write(Map(
          "userName" -> e.userName,
          "repositoryName" -> e.repositoryName,
          "issueId" -> e.issueId,
          "estimate" -> e.estimate
        ))
      } getOrElse notFound()
    }
  })

}
