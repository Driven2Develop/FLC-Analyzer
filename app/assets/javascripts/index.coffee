$ ->
  $("#search_form").submit (event) ->
    event.preventDefault()
    # send the message to watch projects
    callback = (data) -> $('#result_data').append $(data)
    $.get '/project/' + $("#search_input").val(), callback