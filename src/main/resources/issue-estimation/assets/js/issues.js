// IE11を考慮してES5の記法で書く

/**
 * イシュー一覧画面のJS
 */
$(() => {

  //***********************************
  // issue作業量操作処理
  //***********************************

  // Plugin.scalaの変数を受け取る
  var issueEstimateBaseUrl = location.pathname;

  /**
   * issueの作業量を取得する.
   *
   * @param issueIds issue番号の配列
   */
  function fetchIssueEstimates(issueIds) {
    // 他とURL形式が異なる owner/repository/issuesestimates
    return  $.ajax(issueEstimateBaseUrl + 'estimates', {
              method: 'GET',
              dataType: 'json',
              data: {
                issueIds : issueIds,
              }
            });
  }

  /**
   * issueの作業量を登録または更新する.
   *
   * @param issueId issue番号
   * @param estimate 作業量
   */
  function upsertEstimate(issueId, estimate) {
    return  $.ajax(issueEstimateBaseUrl + '/' + issueId + '/estimate', {
              method: 'POST',
              dataType: 'json',
              data: { estimate: estimate }
            });
  }

  /**
   * issueの作業量を削除する.
   *
   * @param issueId issue番号
   */
  function deleteEstimate(issueId) {
    return  $.ajax(issueEstimateBaseUrl + '/' + issueId + '/estimate/delete', {
              method: 'POST',
              dataType: 'json'
            });
  }


  //***********************************
  // 初期表示時の処理
  //***********************************

  var $tableIssues = $('.table-issues');
  var $issueLinks = $('.issue-title');

  // 表示しているissueのidの配列を作成
  var issueIds = Array.from($issueLinks,  function(issueLink) {
    var $issueLink = $(issueLink);
    var link = $issueLink.attr('href');
    var issueId = Number(link.substring(link.lastIndexOf('/') + 1));

    // 作成ついでに作業量用selectタグを画面に埋め込み
    $issueLink.after(createEstimateSelect(issueId));

    return issueId;
  });

  // issueの作業量を取得してUIに反映
  fetchIssueEstimates(issueIds)
  .then(function(issueEstimates) {
    Array.from(issueEstimates,  function(issueEstimate) {
      $(`#issue-${issueEstimate.issueId}`).removeAttr('data-init').val(issueEstimate.estimate);
    });

    $('.estimate[data-init]').removeAttr('data-init').addClass('noEstimate').val('');
  });

  // 作業量のセレクトを変更したら、作業量をサーバー側に保存する
  $tableIssues.on('change', '.estimate', function(event) {
    var $select = $(event.target);
    var issueId = $select.data('issue-number');
    var estimate = $select.val();

    if ('' === estimate) {
      // 作業量を削除
      deleteEstimate(issueId);
      $select.addClass('noEstimate');
      return;
    }

    $select.removeClass('noEstimate');
    upsertEstimate(issueId, Number(estimate));
 });


  /**
   * Issueの作業量設定用のセレクトのDOM要素を返す.
   *
   * @param issueId Issue番号
   */
  function createEstimateSelect(issueId) {
    var lines = []
    lines.push('<select class="estimate" data-init id="issue-' + issueId + '" data-issue-number="' + issueId + '" >');
    lines.push('<option value="">-</option>');
    for (var i = 1; i <= 30; i++) {
      lines.push('<option value="' + i + '">' + i + '</option>');
    }
    lines.push('</select>');

    return lines.join('\n');
  }
});