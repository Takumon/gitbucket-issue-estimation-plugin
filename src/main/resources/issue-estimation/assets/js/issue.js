// IE11を考慮してES5の記法で書く


/**
 * イシュー画面のJS
 */
$(() => {
  'use strict';

  //***********************************
  // issue作業量操作処理
  //***********************************


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


  //***********************************
  // 初期表示時の処理
  //***********************************

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

    var $lableEstimate = $('#label-estimate');

    // 作業量の指定があればUIに反映
    if (estimate) {
      $('.estimate-dropdown-option[data-id="' + estimate + '"] i.octicon').addClass('octicon-check');
      $lableEstimate.addClass('selected').text(estimate);
    } else {
      $lableEstimate.text('No estimate');
    }

    $('#estimate-dropdown-memu').on('click', '.estimate-dropdown-option', function() {
      var selectedEstimate = $(this).data('id');
      var $selectedOction = $(this).find('i.octicon');

      // data-idが存在しない場合は値をクリアする
      if (!selectedEstimate) {
        // TODO 削除失敗時のエラーハンドリング
        return deleteEstimate().then(function() {
          var $oldSelectedOption = $('.estimate-dropdown-option i.octicon-check');
          $oldSelectedOption.removeClass('octicon-check');
          $lableEstimation.removeClass('selected').text('No estimation');
        })
      }

      // 値が変換しない場合はなにもしない
      if ($lableEstimate.text() === selectedEstimate) return;

      // 選択した作業量を更新
      // TODO 更新失敗時のエラーハンドリング
      upsertEstimate(selectedEstimate).then(function() {
        // UIを更新
        var $oldSelectedOption = $('.estimate-dropdown-option i.octicon-check');
        $oldSelectedOption.removeClass('octicon-check');
        $selectedOction.addClass('octicon-check');
        $lableEstimate.addClass('selected').text(selectedEstimate);
      });
    });
  }


});