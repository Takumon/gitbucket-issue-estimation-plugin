package gitbucket.plugin.controller


import gitbucket.core.api.ApiError
import gitbucket.core.controller.ControllerBase
import gitbucket.core.service.{IssueCreationService, _}
import gitbucket.core.util.StringUtil._
import gitbucket.core.util.Implicits._
import gitbucket.core.util.ReferrerAuthenticator
import gitbucket.plugin.service.IssueEstimateService



class IssueEstimateController
  extends JenkinsResultCommentControllerBase
    with IssueEstimateService
    with IssueCreationService
    with WebHookIssueCommentService
    with LabelsService
    with ActivityService
    with RepositoryService
    with AccountService
    with IssuesService
    with ReferrerAuthenticator
    with PullRequestService
    with CommitsService
    with WebHookPullRequestService



trait JenkinsResultCommentControllerBase extends ControllerBase {
  self: IssueEstimateService
    with IssueCreationService
    with WebHookIssueCommentService
    with LabelsService
    with ActivityService
    with RepositoryService
    with CommitsService
    with IssuesService
    with ReferrerAuthenticator
    with AccountService
    with WebHookPullRequestService
    with PullRequestService =>



  ajaxGet("/:owner/:repository/issues/:issueId/estimate")(referrersOnly { repository =>
    println("get")

    val issueId = params("issueId")

    if ( !isInteger(issueId)) {
      println("get issueId not found")
      NotFound()
    } else {

      println("get issueId found")

      getIssueEstimate(repository.owner, repository.name, issueId.toInt).map { e =>
        println("get issue estimation get")

        org.json4s.jackson.Serialization.write(Map(
          "userName" -> e.userName,
          "repositoryName" -> e.repositoryName,
          "issueId" -> e.issueId,
          "estimate" -> e.estimate
        ))
      } getOrElse NotFound()
    }
  })


  ajaxPost("/:owner/:repository/issues/:issueId/estimate")(referrersOnly { repository =>
    println("post")
    if (isIssueEditable(repository)) {

      val userName = params("owner")
      val repositoryName = params("repository")
      val issueId = params("issueId")

      val estimate = params("estimate")

      if ( !isInteger(issueId)) {

        ApiError( s"Invalid issueId = $issueId" )

      } else if ( !isInteger(estimate)) {

        ApiError( s"Invalid estimate = $estimate" )

      } else {
        upsertIssueEstimate(userName, repositoryName, issueId.toInt, estimate.toInt)

        org.json4s.jackson.Serialization.write(Map(
          "message" -> "updated issue estimate"
        ))
      }

    } else Unauthorized

  })


  ajaxPost("/:owner/:repository/issues/:issueId/estimate/delete")(referrersOnly { repository =>
    if (isIssueEditable(repository)) {

      val userName = params("owner")
      val repositoryName = params("repository")
      val issueId = params("issueId")


      if ( !isInteger(issueId)) {

        ApiError( s"Invalid issueId = $issueId" )

      } else {
        deleteIssueEstimate(userName, repositoryName, issueId.toInt)

        org.json4s.jackson.Serialization.write(Map(
          "message" -> "deleted issue estimate"
        ))
      }

    } else Unauthorized

  })


}
