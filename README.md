# jenkins-stub

A stub to be used in place of the real Jenkins API for testing purposes.

## Response Table
Real Jenkins responses to be recreated in the stub.

Notes
1. API key could also refer to password for that user
2. Curl requires escape characters before characters `&` and `=`. This is not required if the whole url (including parameters) is written as a string (inside `'` or `"` characters). E.g.

    `curl -X POST https://jenkins-url/job/job-name/buildWithParameters?MESSAGE=Hello&MESSAGE_TWO=Hi` 
    
    will fail to connect, whereas the following two will work as intended
    
    `curl -X POST "https://jenkins-url/job/job-name/buildWithParameters?MESSAGE=Hello&MESSAGE_TWO=Hi" `
    
    `curl -X POST https://jenkins-url/job/job-name/buildWithParameters\?MESSAGE\=Hello\&MESSAGE_TWO\=Hi`
    
3. If the parameters passed to the build are incorrect or missing, the HTTP status will still be OK. However the jenkins job will fail, but no further messages are sent via they API.

<br />

| Request    | Response | Example |
| ----------- | ----------- | ----------- |
| Correct Build, no params| HTTP/2 201| curl -X POST <'URL'>/job/<'JOBNAME'>/build --user <'USERNAME'>:<'API-KEY'>
| Correct Build, params.  | HTTP/2 201| curl -X POST <'URL'>/job/<'JOBNAME'>/buildWithParameters\?<PARAM_KEY>\=<PARAM_VALUE>&<param_two>=<value_two> --user <'USERNAME'>:<'API-KEY'>
| Failing Build, unexpected params| HTTP/2 500 | curl -X POST <'URL'>/job/<'JOBNAME'>/buildWithParameters\?<PARAM_KEY>\=<PARAM_VALUE> --user <'USERNAME'>:<'API-KEY'>
| Failing build, missing params  | HTTP/2 201| curl -X POST <'URL'>/job/<'JOBNAME'>/buildWithParameters\?<PARAM_KEY>\=<BAD_PARAM_VALUE --user <'USERNAME'>:<'API-KEY'>
| Failing build, misspelt params  | HTTP/2 201| curl -X POST <'URL'>/job/<'JOBNAME'>/buildWithParameters\?<PARAM_KEY>\=<BAD_PARAM_VALUE --user <'USERNAME'>:<'API-KEY'>
| Failing build, job doesn't exist  | Error 404 Not Found| curl -X POST <'URL'>/job/<'BAD_JOBNAME'>/build --user <'USERNAME'>:<'API-KEY'>
| Failing build, invalid username  | HTTP ERROR 401 Invalid password/token for user| curl -X POST <'URL'>/job/<'JOBNAME'>/build --user <'BAD_USERNAME'>:<'API-KEY'>
| Failing build, invalid password/api-key  | HTTP ERROR 401 Invalid password/token for user| curl -X POST <'URL'>/job/<'JOBNAME'>/build --user <'USERNAME'>:<'BAD_API-KEY'>