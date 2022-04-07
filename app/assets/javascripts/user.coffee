$ ->
  ws = new WebSocket $("body").data("ws-url")
  ws.onopen = (event) ->
  	ws.send(JSON.stringify({keyword: window.location.pathname.split("/").pop()}))
  ws.onmessage = (event) ->
    repo = JSON.parse event.data
    callback = (data) -> $('#user_projects').html data
    $.get '/user/' + repo.data.id + '/projects', callback
    $('#id').text(repo.data.id)
    $('#display_name').text(repo.data.display_name)
    $('#username').text(repo.data.username)
    $('#registration_date').text(repo.data.registration_date)
    $('#country').text(repo.data.location.country.name)
    $('#primary_currency').text(repo.data.primary_currency.name)
    $('#role').text(repo.data.role)
    $('#chosen_role').text(repo.data.chosen_role)
    if(repo.data.limited_account? and repo.data.limited_account == true)
        $('#is_limited_account').html '<td><i class="fa fa-check-circle success"></i> Limited Account</td>'
    else
        $('#is_limited_account').html '<td><i class="fa fa-times-circle error"></i> Limited Account</td>'
    if(repo.data.status.email_verified? and repo.data.status.email_verified == true)
        $('#is_email_verified').html '<td><i class="fa fa-check-circle success"></i> Email Verified</td>'
    else
        $('#is_email_verified').html '<td><i class="fa fa-times-circle error"></i> Email Verified</td>'
