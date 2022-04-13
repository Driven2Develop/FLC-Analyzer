$ ->
  ws = new WebSocket $("#search_projects_main").data("ws-url")
  ws.onopen = (event) ->
    ws.send(JSON.stringify({keyword: window.location.pathname.split("/").pop()}))
  ws.onmessage = (event) ->
    readability = JSON.parse event.data
    $("#user_projects_data").html '' #reset table data
    for x in [0..readability.data.length-1]
      row1=$('<tr>')
      row1.append $('<td/>').append readability.data[x].score
      row1.append $('<td/>').append readability.data[x].education_level
      row1.append $('<tr />')
      $('#user_projects_data').append row1