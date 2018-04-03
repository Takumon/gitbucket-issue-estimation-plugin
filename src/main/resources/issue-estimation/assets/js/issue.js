// IE11を考慮してES5の記法で書く


/**
 * イシュー画面の初期化処理
 */
$(() => {
  // Plugin.scalaの変数を受け取る
  var issueEstimateUrl = ISSUE_ESTIMATE_URL;

  /**
   * issueの作業量を取得する.
   *
   * @param issueId issue番号
   */
  function fetchIssueEstimate() {
    return  $.ajax(issueEstimateUrl, {
              method: 'GET',
              dataType: 'json'
            });
  }

  /**
   * issueの作業量を登録または更新する.
   *
   * @param estimate 作業量
   */
  function upsertEstimate(estimate) {
    return  $.ajax(issueEstimateUrl, {
              method: 'POST',
              dataType: 'json',
              data: { estimate: estimate }
            });
  }

  /**
   * issueの作業量を削除する.
   *
   */
  function deleteEstimate() {
    return  $.ajax(issueEstimateUrl + '/delete', {
              method: 'POST',
              dataType: 'json'
            });
  }


  // issueの作業量を取得してUIに反映
  fetchIssueEstimate()
  .then(function(data) {
    appendEstimaiteSelect(data.estimate);
  })
  .catch(({status, responseJSON}) => {
    if (status !== 404) return;
    appendEstimaiteSelect();
  });


  /**
   * 指定した作業量で作業量設定エリアを画面を追加する.
   *
   * @param estimate 作業量
   */
  function appendEstimaiteSelect(estimate) {

    var $lableEstimation = $('#label-estimation');

    // 作業量の指定があればUIに反映
    if (estimate) {
      $('.estimation-dropdown-option[data-id="' + estimate + '"] i.octicon').addClass('octicon-check');
      $lableEstimation.addClass('selected').text(estimate);
    } else {
      $lableEstimation.text('No estimate');
    }

    $('#estimation-dropdown-memu').on('click', '.estimation-dropdown-option', function() {
      var selectedEstimate = $(this).data('id');
      var $selectedOction = $(this).find('i.octicon');

      // data-idが存在しない場合は値をクリアする
      if (!selectedEstimate) {
        return deleteEstimate().then(function() {
          var $oldSelectedOption = $('.estimation-dropdown-option i.octicon-check');
          $oldSelectedOption.removeClass('octicon-check');
          $lableEstimation.removeClass('selected').text('No estimation');
        })
        // TODO 削除失敗時のエラーハンドリング
      }

      // 値が変換しない場合はなにもしない
      if ($lableEstimation.text() === selectedEstimate) return;

      // 選択した作業量を更新
      upsertEstimate(selectedEstimate).then(function() {
        // UIを更新
        var $oldSelectedOption = $('.estimation-dropdown-option i.octicon-check');
        $oldSelectedOption.removeClass('octicon-check');
        $selectedOction.addClass('octicon-check');
        $lableEstimation.addClass('selected').text(selectedEstimate);
      });
      // TODO 更新失敗時のエラーハンドリング
    });
  }


});