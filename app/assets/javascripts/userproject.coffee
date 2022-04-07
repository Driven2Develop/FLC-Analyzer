$ ->
  ws = new WebSocket $("#user_projects_main").data("ws-url")
  ws.onopen = (event) ->
  	ws.send(JSON.stringify({keyword: window.location.pathname.split("/").pop()}))
  ws.onmessage = (event) ->
    repo = JSON.parse event.data
    for x in [0..repo.data.length-1]
      row1=$('<tr>')
      row1.append $('<td/>').append repo.data[x].time_submitted
      row1.append $('<td/>').append repo.data[x].title
      row1.append $('<tr />')
      $('#user_projects_data').append row1