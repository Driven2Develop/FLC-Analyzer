$ ->
  ws = new WebSocket $("#stats_project").data("ws-url")
  ws.onopen = (event) ->
    ws.send(JSON.stringify({projectDesc: window.location.pathname.split("/").pop()}))
  ws.onmessage = (event) ->
    $('#loading_spinner').hide()
    $("#search_result").html ''
    repo = JSON.parse event.data
    for x in [0..repo.data.length-1]
      if (!!repo.data[x].word)
        row1=$('<tr>')
        row1.append $('<td/>').text(repo.data[x].word)
        row1.append $('<td/>').text(repo.data[x].occurrence)
        row1.append $('<tr/>')
        $('#search_result').append row1
