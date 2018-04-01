

import javax.servlet.ServletContext

import gitbucket.core.controller.Context
import gitbucket.core.model._
import gitbucket.core.plugin.PluginRegistry
import gitbucket.core.service.RepositoryService.RepositoryInfo
import gitbucket.core.service.SystemSettingsService.SystemSettings
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

  override val assetsMappings = Seq("/issue-estimation/assets" -> "/issue-estimation/assets")

  override def javaScripts(registry: PluginRegistry, context: ServletContext, settings: SystemSettings): Seq[(String, String)] = {

    val path = settings.baseUrl.getOrElse(context.getContextPath)

    Seq(
      ".*/issues/\\d+$" -> s"""
         |</script>
         |<script src="$path/plugin-assets/issue-estimation/assets/issue.js"></script>
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

      Some(Html(
        s"""
           |<hr />
           |<div style="margin-bottom: 14px;">
           |  <span class="muted small strong">Estimation</span>
           |  <div class="pull-right">
           |    <div class="btn-group">
           |      <button class="dropdown-toggle btn btn-default btn-sm" data-toggle="dropdown" aria-expanded="false">
           |        <span class="strong">Edit</span>
           |        <span class="caret"></span>
           |      </button>
           |      <ul id="estimation-dropdown-memu" class="dropdown-menu pull-right">
           |        <li class="estimation-dromdown-option" data-id ><a href="javascript:void(0);"><i class="octicon octicon-x"></i>  Clear estimation</a></li>
           |        <li class="estimation-dromdown-option" data-id="1" ><a href="javascript:void(0);"><i class="octicon"></i>1</a></li>
           |        <li class="estimation-dromdown-option" data-id="2" ><a href="javascript:void(0);"><i class="octicon"></i>2</a></li>
           |        <li class="estimation-dromdown-option" data-id="3" ><a href="javascript:void(0);"><i class="octicon"></i>3</a></li>
           |        <li class="estimation-dromdown-option" data-id="4" ><a href="javascript:void(0);"><i class="octicon"></i>4</a></li>
           |        <li class="estimation-dromdown-option" data-id="5" ><a href="javascript:void(0);"><i class="octicon"></i>5</a></li>
           |        <li class="estimation-dromdown-option" data-id="6" ><a href="javascript:void(0);"><i class="octicon"></i>6</a></li>
           |        <li class="estimation-dromdown-option" data-id="7" ><a href="javascript:void(0);"><i class="octicon"></i>7</a></li>
           |        <li class="estimation-dromdown-option" data-id="8" ><a href="javascript:void(0);"><i class="octicon"></i>8</a></li>
           |        <li class="estimation-dromdown-option" data-id="9" ><a href="javascript:void(0);"><i class="octicon"></i>9</a></li>
           |        <li class="estimation-dromdown-option" data-id="10"><a href="javascript:void(0);"><i class="octicon"></i>10</a></li>
           |        <li class="estimation-dromdown-option" data-id="11" ><a href="javascript:void(0);"><i class="octicon"></i>11</a></li>
           |        <li class="estimation-dromdown-option" data-id="12" ><a href="javascript:void(0);"><i class="octicon"></i>12</a></li>
           |        <li class="estimation-dromdown-option" data-id="13" ><a href="javascript:void(0);"><i class="octicon"></i>13</a></li>
           |        <li class="estimation-dromdown-option" data-id="14" ><a href="javascript:void(0);"><i class="octicon"></i>14</a></li>
           |        <li class="estimation-dromdown-option" data-id="15" ><a href="javascript:void(0);"><i class="octicon"></i>15</a></li>
           |        <li class="estimation-dromdown-option" data-id="16" ><a href="javascript:void(0);"><i class="octicon"></i>16</a></li>
           |        <li class="estimation-dromdown-option" data-id="17" ><a href="javascript:void(0);"><i class="octicon"></i>17</a></li>
           |        <li class="estimation-dromdown-option" data-id="18" ><a href="javascript:void(0);"><i class="octicon"></i>18</a></li>
           |        <li class="estimation-dromdown-option" data-id="19" ><a href="javascript:void(0);"><i class="octicon"></i>19</a></li>
           |        <li class="estimation-dromdown-option" data-id="20"><a href="javascript:void(0);"><i class="octicon"></i>20</a></li>
           |      </ul>
           |    </div>
           |  </div>
           |</div>
           |<span id="label-estimation"></span>
         """.stripMargin
      ))
    }
  )

}
