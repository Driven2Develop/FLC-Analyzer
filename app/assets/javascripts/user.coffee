$ ->
  ws = new WebSocket $("body").data("ws-url")
  ws.onopen = (event) ->
  	ws.send(JSON.stringify({keyword: window.location.pathname.split("/").pop()}))
  ws.onmessage = (event) ->
    repo = JSON.parse event.data
    $('#display_name').text(repo.data.display_name)