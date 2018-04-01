package gitbucket.plugin.model


trait IssueEstimateComponent { self: gitbucket.core.model.Profile =>
  import profile.api._

  lazy val IssueEstimates = TableQuery[IssueEstimates]

  class IssueEstimates(tag: Tag) extends Table[IssueEstimate](tag, "ISSUE_ESTIMATE") {
    val userName        = column[String]  ("USER_NAME")
    val repositoryName  = column[String]  ("REPOSITORY_NAME")
    val issueId         = column[Int] ("ISSUE_ID")
    val estimate        = column[Int] ("ESTIMATE")
    def * = (
      userName,
      repositoryName,
      issueId,
      estimate
    ) <> (IssueEstimate.tupled, IssueEstimate.unapply)
  }
}

case class IssueEstimate (
  userName        : String,
  repositoryName  : String,
  issueId         : Int,
  estimate        : Int
)
