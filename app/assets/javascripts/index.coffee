$ ->
  currentSearchCount = 0
  maxSearchCount = 10
  $("#search_form").submit (event) ->
    if currentSearchCount <= maxSearchCount
      event.preventDefault()
      # send the message to watch projects
      callback = (data) ->
        currentSearchCount += 1
        $('#search_count').text currentSearchCount
        $('#result_data').prepend $(data)
      $.get '/project/' + $("#search_input").val(), callback
