$ ->
  ws = new WebSocket $("#average_readability").data("ws-url")
  ws.onopen = (event) ->
    ws.send(JSON.stringify({keyword: window.location.pathname.split("/").pop()}))
  ws.onmessage = (event) ->
    $('#loading_spinner').hide()
    $("#search_result").html ''
    read = JSON.parse event.data
    for x in [0..read.data.length-1]
      row1=$('<tr>')
      row1.append $('<td/>').append read.data[x].score
      row1.append $('<td/>').append read.data[x].education_level
      row1.append $('<tr />')
      $('#search_result').append row1