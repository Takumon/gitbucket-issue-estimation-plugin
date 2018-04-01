package gitbucket.plugin.service

import gitbucket.plugin.model.IssueEstimate
import gitbucket.plugin.model.Profile._
import gitbucket.plugin.model.Profile.profile.blockingApi._


trait IssueEstimateService {

  def getIssueEstimates(userName: String, repositoryName: String) =
    IssueEstimates.filter(t => (t.userName === userName.bind) && (t.repositoryName === repositoryName.bind))

  def getIssueEstimate(userName: String, repositoryName: String, issueId: Int)(implicit session: Session): Option[IssueEstimate] =
    IssueEstimates.filter(t => (t.userName === userName.bind) && (t.repositoryName === repositoryName.bind) && (t.issueId === issueId.bind)).firstOption

  def upsertIssueEstimate(userName: String, repositoryName: String, issueId: Int, estimate: Int)(implicit session: Session): Int =
    IssueEstimates.insertOrUpdate(IssueEstimate(
      userName        = userName,
      repositoryName  = repositoryName,
      issueId         = issueId,
      estimate        = estimate
    ))

  def deleteIssueEstimate(userName: String, repositoryName: String, issueId: Int)(implicit session: Session): Int =
    IssueEstimates.filter(t => (t.userName === userName.bind) && (t.repositoryName === repositoryName.bind) && (t.issueId === issueId.bind)).delete
}
