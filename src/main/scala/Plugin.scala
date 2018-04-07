

import javax.servlet.ServletContext

import gitbucket.core.controller.Context
import gitbucket.core.model._
import gitbucket.core.plugin.PluginRegistry
import gitbucket.core.service.RepositoryService.RepositoryInfo
import gitbucket.core.service.SystemSettingsService.SystemSettings
import gitbucket.plugin.controller.IssueEstimateController
import io.github.gitbucket.solidbase.migration.LiquibaseMigration
import io.github.gitbucket.solidbase.model.Version
import play.twirl.api.Html

class Plugin extends gitbucket.core.plugin.Plugin {

  override val pluginId = "issue-estimation"
  override val pluginName = "Issue estimation Plugin"
  override val description = "You can estimate issue in GitBucket"

  override val versions = List(
    new Version("0.1", new LiquibaseMigration("update/gitbucket-issue-estimation_0.1.xml"))
  )

  override val controllers = Seq(
    "/*" -> new IssueEstimateController()
  )

  override val assetsMappings = Seq("/issue-estimation/assets" -> "/issue-estimation/assets")

  override def javaScripts(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Seq[(String, String)] = {

    val path = settings.baseUrl.getOrElse(context.getContextPath)

    Seq(
      // イシュー一覧(ダッシュボードは除外)
      "^(?!.*dashboard).*/issues$" ->
        s"""|
            |</script>
            |<script></script>
            |<script src="$path/plugin-assets/issue-estimation/assets/js/issues.js"></script>
            |<link href="$path/plugin-assets/issue-estimation/assets/css/issues.css" rel="stylesheet">
            |<script>
        """.stripMargin,

      // マイルストーン一覧
      ".*/milestones$" ->
        s"""|
            |</script>
            |<script></script>
            |<script src="$path/plugin-assets/issue-estimation/assets/js/milestones.js"></script>
            |<link href="$path/plugin-assets/issue-estimation/assets/css/milestones.css" rel="stylesheet">
            |<script>
        """.stripMargin
    )
  }

  override def issueSidebars(
    registry: PluginRegistry,
    context: ServletContext,
    settings: SystemSettings
  ): Seq[(Issue, RepositoryInfo, Context) => Option[Html]] = Seq(
    (issue: Issue, repository: RepositoryInfo, context: Context) => {
      val url = s"${context.path}/${repository.owner}/${repository.name}/issues/${issue.issueId}/estimate"

      if (!issue.isPullRequest) {
        // Issue画面
        val options = (1 to 30).map(i => s"""<li class="estimate-dropdown-option" data-id="$i" ><a href="javascript:void(0);"><i class="octicon"></i>$i</a></li>""").mkString("\n")

        Some(Html(
          s"""
             |<hr />
             |<script> var ISSUE_ESTIMATE_URL = '$url'; </script>
             |<script src="${context.path}/plugin-assets/issue-estimation/assets/js/issue.js"></script>
             |<link href="${context.path}/plugin-assets/issue-estimation/assets/css/issue.css" rel="stylesheet">
             |<div style="margin-bottom: 14px;">
             |  <span class="muted small strong">Estimate</span>
             |  <div class="pull-right">
             |    <div class="btn-group">
             |      <button class="dropdown-toggle btn btn-default btn-sm" data-toggle="dropdown" aria-expanded="false">
             |        <span class="strong">Edit</span>
             |        <span class="caret"></span>
             |      </button>
             |      <ul id="estimate-dropdown-memu" class="dropdown-menu pull-right">
             |        <li class="estimate-dropdown-option" data-id ><a href="javascript:void(0);"><i class="octicon octicon-x"></i>  Clear estimate</a></li>
             |        $options
             |       </ul>
             |    </div>
             |  </div>
             |</div>
             |<span id="label-estimate"></span>
         """.stripMargin
        ))
      } else None
    }
  )

}
