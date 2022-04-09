$ ->
  $("#search_form").submit (event) ->
    event.preventDefault()
    # send the message to watch projects
    callback = (data) -> $('#result_data').prepend $(data)
    $.get '/project/' + $("#search_input").val(), callback