$ ->
  ws = new WebSocket $("#search_projects_main").data("ws-url")
  ws.onopen = (event) ->
    ws.send(JSON.stringify({jobId: window.location.pathname.split("/").pop()}))
  ws.onmessage = (event) ->
    $('#loading_spinner').hide()
    repo = JSON.parse event.data
    header=$('<tr>
      <th id="count">#</th>
      <th id="owner_id">Owner ID</th>
      <th id="time_submitted">Time Submitted</th>
      <th id="title">Title</th>
      <th id="type">Type</th>
      <th id="skills">Skills</th>
      <th id="stats">Stats</th>
      <th id="project_readability">Project Readability</th>
    </tr>')
    $('#search_result').append header
    for x in [0..repo.data.length-1]
      row1=$('<tr>')
      row1.append $('<td/>').text(x + 1)
      row1.append $('<td/>').html '<a href="/user/' + repo.data[x].owner_id + '">' + repo.data[x].owner_id + '</a>'
      row1.append $('<td/>').text(repo.data[x].time_submitted)
      row1.append $('<td/>').text(repo.data[x].title)
      row1.append $('<td/>').text(repo.data[x].type)
      skillElement = '<td>'
      for j in [0..repo.data[x].jobs.length - 1]
#condition to append comma
        if j != 0
          skillElement += ', '
        skillElement += '<a href="/project/job/' + repo.data[x].jobs[j].id + '">' + repo.data[x].jobs[j].name + '</a>'
      skillElement += '</td>'
      row1.append $(skillElement)
      row1.append $('<td/>').html '<a href="/stats/single/' + repo.data[x].preview_description + '">View Stats</a>'
      row1.append $('<td/>').html '<a href="/project/readability/' + repo.data[x].preview_description + '">View Readability</a>'
      row1.append $('<tr />')
      $('#search_result').append row1
