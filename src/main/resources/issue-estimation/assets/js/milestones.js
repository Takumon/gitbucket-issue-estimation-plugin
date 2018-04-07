/**
 * マイルストーン画面の初期化処理
 */
$(() => {
  'use strict';


  //***********************************
  // マイルストーン別作業量取得処理
  //***********************************

  var baseUrl = location.pathname.replace('/issues/milestones', '/milestones_estimates');

  /**
   * マイルストーン別の作業量消化率を取得する.
   *
   * @param issueIds issue番号の配列
   */
  function fetchProgressBaseOnEstimatesByMilestone() {
    // 他とURL形式が異なる owner/repository/issues_estimates
    return  $.ajax(baseUrl, {
              method: 'GET',
              dataType: 'json',
            });
  }


  //***********************************
  // 初期表示時の処理
  //***********************************
  fetchProgressBaseOnEstimatesByMilestone().then(function(progressList) {

    var $titles = $('.milestone-title');
    progressList.forEach(function(progress) {

      var $title = $titles.filter(function() { return this.text === progress.title}).eq(0);
      var $progressArea = $title.parent().next();
      $progressArea.prepend('<h6 class="progress-title_count">Count</h6>');


      $progressArea.prepend(createProgressBaseOnEstimateArea(progress.open, progress.closed));
    });
  })





  /**
   * Issueの作業量でみた進捗状況のDOM要素を返す.
   *
   * @param openIssueEstimates openなIssueの作業量合計
   * @param closedIssueEstimates closedなIssueの作業量合計
   */
  function createProgressBaseOnEstimateArea(openIssueEstimates, closedIssueEstimates) {

    var closedIssueRate = openIssueEstimates + closedIssueEstimates === 0
                              ? 0
                              : Math.round(closedIssueEstimates / (openIssueEstimates + closedIssueEstimates) * 100);

    return [
      '<h6 class="progress-title_estimate">Estimate</h6>',
      '<div class="progress" style="height: 12px; margin-bottom: 8px;">',
      '  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="' + closedIssueEstimates + '" aria-valuemin="0" aria-valuemax="100" style="width: ' + closedIssueRate + '%">',
      '  </div>',
      '</div>',
      '<div class="progress-detial_estimate">',
      '  <div>',
      '    ' + closedIssueRate + '% <span class="muted">complete</span> &nbsp;&nbsp;',
      '    ' + openIssueEstimates + '<span class="muted">open</span> &nbsp;&nbsp;',
      '    ' + closedIssueEstimates + ' <span class="muted">closed</span>',
      '  </div>',
      '</div>'].join('\n');
  }
});