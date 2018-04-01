package gitbucket.plugin.controller


import gitbucket.core.controller.ControllerBase
import gitbucket.core.service._
import gitbucket.core.util.StringUtil._
import gitbucket.core.util.Implicits._
import gitbucket.core.util.OwnerAuthenticator
import gitbucket.plugin.model.IssueEstimate
import gitbucket.plugin.service.IssueEstimateService
import org.apache.http.util.EntityUtils

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal



class IssueEstimateController
  extends JenkinsResultCommentControllerBase
    with IssueEstimateService
    with RepositoryService
    with AccountService
    with IssuesService
    with OwnerAuthenticator
    with PullRequestService
    with CommitsService
    with WebHookPullRequestService



trait JenkinsResultCommentControllerBase extends ControllerBase {
  self: IssueEstimateService
    with RepositoryService
    with CommitsService
    with IssuesService
    with OwnerAuthenticator
    with AccountService
    with WebHookPullRequestService
    with PullRequestService =>



//  get("/:owner/:repository/settings/jenkins-result-comment")(ownerOnly { repository =>
//
//    val settingOptions = getIssueEstimate(repository.owner, repository.name, issueId)
//    val setting = settingOptions.
//
//
//    html.setting(repository, setting, flash.get("info"))
//  })
//
//  ajaxPost("/:owner/:repository/issues/:issueId/estimate")(ownerOnly { repository =>
//
//    val issueId = params("issueId")
//    val estimate = params("estimate")
//
//    if(!isInteger(issueId) || !isInteger(estimate)) {
//      // TODO エラー処理
//    }
//
//    upsertIssueEstimate(repository.owner, repository.name, issueId.toInt, estimate.toInt)
//  })


}
