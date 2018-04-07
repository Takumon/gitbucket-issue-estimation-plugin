package gitbucket.plugin.service

import gitbucket.plugin.model.IssueEstimate
import gitbucket.plugin.model.Profile._
import gitbucket.plugin.model.Profile.profile.blockingApi._


trait IssueEstimateService {

  def getIssueEstimates(userName: String, repositoryName: String, issueIds: Seq[Int])(implicit session: Session) =
    IssueEstimates.filter(t => (t.userName === userName) && (t.repositoryName === repositoryName) && (t.issueId inSet issueIds)).list


  def getIssueEstimate(userName: String, repositoryName: String, issueId: Int)(implicit session: Session): Option[IssueEstimate] =
    IssueEstimates.filter(t => (t.userName === userName.bind) && (t.repositoryName === repositoryName.bind) && (t.issueId === issueId.bind)).firstOption


  def getMilestonesWithEstimateCount(userName: String, repositoryName: String)(implicit s: Session) = {
    val issues = Issues
      .filter { t =>
        t.byRepository(userName, repositoryName) && (t.milestoneId.? isDefined)
      }.list

    val issueIds = issues.map(_.issueId)
    val estimations = getIssueEstimates(userName, repositoryName, issueIds)

    issues.groupBy { t =>
        t.milestoneId.get -> t.closed
      }
      .map { case (t1, t2) =>
        val estimates = t2.map(issue => estimations.find(e => e.issueId == issue.issueId).map(_.estimate).getOrElse(0)).sum
        t1._1 -> t1._2 -> estimates
      }
      .toMap
  }

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
