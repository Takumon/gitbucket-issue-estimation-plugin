// IE11を考慮してES5の記法で書く

/**
 * イシュー画面の初期化処理
 */
$(() => {
  /**  Issueの作業量を操作するためのURL */
  var issueEstimateUrl = location.pathname + '/estimate'

  /**
   * issueの作業量を取得する.
   *
   * @param issueId issue番号
   */
  function fetchIssueEstimation() {
    return  $.ajax(issueEstimateUrl, {
              method: 'GET',
              dataType: 'json'
            });
  }

  /**
   * issueの作業量を登録または更新する.
   *
   * @param estimation 作業量
   */
  function upsertEstimation(estimation) {
    return  $.ajax(issueEstimateUrl, {
              method: 'POST',
              dataType: 'json',
              data: { estimation: estimation }
            });
  }

  /**
   * issueの作業量を削除する.
   *
   */
  function deleteEstimation() {
    return  $.ajax(issueEstimateUrl, {
              method: 'DELETE',
              dataType: 'json'
            });
  }


  // issueの作業量を取得してUIに反映
  fetchIssueEstimation()
  .then(function(data) {
    appendEstimaitonSelect(data.estimation);
  })
  .catch(({status, responseJSON}) => {
    if (status !== 404) return;
    appendEstimaitonSelect();
  });


  /**
   * 指定した作業量で作業量設定エリアを画面を追加する.
   *
   * @param estimation 作業量
   */
  function appendEstimaitonSelect(estimation) {
    $('#label-priority').after(createEstimationSelect());

    var $lableEstimation = $('#label-estimation');

    // 作業量の指定があればUIに反映
    if (estimation) {
      $('.estimation-dropdown-option[data-id="${estimation}"] i.octicon-check').addClass('octicon-check');
      $lableEstimation.addClass('selected').text(estimation);
    } else {
      $lableEstimation.text('No estimation');
    }

    $('#estimation-dropdown-memu').on('click', '.estimation-dropdown-option', function() {
      var selectedEstimation = $(this).data('id');
      var $selectedOction = $(this).find('i.octicon');

      // data-idが存在しない場合は値をクリアする
      if (!selectedEstimation) {
        return deleteEstimation().then(function() {
          var $oldSelectedOption = $('.estimation-dropdown-option i.octicon-check');
          $oldSelectedOption.removeClass('octicon-check');
          $lableEstimation.removeClass('selected').text('No estimation');
        })
      }

      // 値が変換しない場合はなにもしない
      if ($lableEstimation.text() === selectedEstimation) return;

      // 選択した作業量を更新
      upsertEstimation(selectedEstimation).then(function() {
        // UIを更新
        var $oldSelectedOption = $('.estimation-dropdown-option i.octicon-check');
        $oldSelectedOption.removeClass('octicon-check');
        $selectedOction.addClass('octicon-check');
        $lableEstimation.addClass('selected').text(selectedEstimation);
      });
    });
  }


});