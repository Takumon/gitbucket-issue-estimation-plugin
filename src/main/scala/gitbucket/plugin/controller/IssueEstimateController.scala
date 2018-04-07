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
    with CommitsService
    with IssuesService
    with ReferrerAuthenticator
    with AccountService
    with WebHookPullRequestService
    with PullRequestService
    with MilestonesService




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
    with PullRequestService
    with MilestonesService =>

  ajaxGet("/:owner/:repository/issues_estimates")(referrersOnly { repository =>

    val issueIds = multiParams("issueIds")

    if ( !issueIds.isEmpty ) {

      val issues = getIssueEstimates(repository.owner, repository.name, issueIds.map(_.toInt))
      org.json4s.jackson.Serialization.write(issues.map( i =>
        Map(
          "userName" -> i.userName,
          "repositoryName" -> i.repositoryName,
          "issueId" -> i.issueId,
          "estimate" -> i.estimate
        )
      ))
    } else NotFound()
  })

  ajaxGet("/:owner/:repository/milestones_estimates")(referrersOnly { repository =>

    val counts = getMilestonesWithEstimateCount(repository.owner, repository.name)

    val estimatesCountByMilestone = getMilestones(repository.owner, repository.name).map { milestone =>
      Map(
        "milestoneId" -> milestone.milestoneId,
        "title"       -> milestone.title,
        "open"        -> counts.getOrElse((milestone.milestoneId, false), 0),
        "closed"      -> counts.getOrElse((milestone.milestoneId, true), 0)
      )
    }

    org.json4s.jackson.Serialization.write(estimatesCountByMilestone)

  })


  ajaxGet("/:owner/:repository/issues/:issueId/estimate")(referrersOnly { repository =>

    val issueId = params("issueId")

    if (isInteger(issueId)) {

      getIssueEstimate(repository.owner, repository.name, issueId.toInt).map { e =>

        org.json4s.jackson.Serialization.write(Map(
          "userName" -> e.userName,
          "repositoryName" -> e.repositoryName,
          "issueId" -> e.issueId,
          "estimate" -> e.estimate
        ))
      } getOrElse NotFound()
    } else NotFound()
  })


  ajaxPost("/:owner/:repository/issues/:issueId/estimate")(referrersOnly { repository =>
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
